package com.pio.controller;

import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pio.model.CylinderTransactions;
import com.pio.model.ECRTransactionBean;
import com.pio.services.CylinderService;
import com.pio.services.ECRService;
import com.pio.util.DealerUtil;

@Controller
public class CylinderTransactionController {

	@Autowired
	private CylinderService cylinderS;
	
	@Autowired
	private DealerUtil dealerU;	
		
	@Autowired
	private ECRService ecrS;
	
	@RequestMapping(value="/CylinderBuy", method = RequestMethod.GET)
	public String buyCylinder (final Model model) {
		model.addAttribute("buy", new CylinderTransactions());
		return "CylinderBuy";
	}
	
	@RequestMapping(value = "/CylinderBuy", method = RequestMethod.POST)
	public ModelAndView processPurchase (
	@ModelAttribute("buy") final CylinderTransactions cylinder, HttpSession session) {
			ModelAndView result = new ModelAndView("CylinderBuy", "MSG",
					"Registration Failed");
			String dealerName = (String)session.getAttribute("dealerName");
			String dealerId = (String) session.getAttribute("dealerId");
			if(dealerName instanceof String) {
			session.setAttribute("dealerName", dealerName);
			session.setAttribute("dealerId", dealerId);
			cylinder.setDealerName(dealerName);
			cylinder.setDealerId(dealerId);
			Long cylinderId = cylinder.getCylinderId();
			String usageStatus = cylinderS.usageStatus(cylinderId);
			if(usageStatus.equals("0")) {
			String status = cylinderS.purchase(cylinder);
			System.out.println("status = "+status);
			if(status.equals("SUCCESS")){
				result = new ModelAndView("CylinderBuy", "MSG",
						"Purchase Successful!</center><br><center> TransactionID is:"
								+ cylinder.getTransactionId() + "Cylinder Number "+cylinder.getCylinderId());
				}
			}
			else if(usageStatus.equals("FAIL")) {
				result = new ModelAndView("CylinderBuy", "stats","This cylinder is either not registered or damaged");
			}
					else {
						result = new ModelAndView("CylinderBuy", "stats","This cylinder is in use by "+usageStatus);
					}
			}
			else {
				result = new ModelAndView("CylinderBuy", "nodeal","Visit Dealer Details and enter Dealer ID to buy or return cylinder");
			}
			return result;
		}
	@RequestMapping(value = "/CylinderCount", method = RequestMethod.GET)
	@ResponseBody
	public long countCylinder (@RequestParam(value = "cylinderType") String cylinderType) {
			long result = cylinderS.countCylinders(cylinderType);
			return result;
		}
	@RequestMapping(value = "/CylinderType", method = RequestMethod.GET)
	@ResponseBody
	public String typeOfCylinder (@RequestParam(value = "cylinderId") Long cylinderId) {
		String result = cylinderS.typeOfCylinders(cylinderId);
			return result;
		}
		
	
	@RequestMapping(value = "/CylinderReturn", method = RequestMethod.GET)
	public String returnCylinder (final Model model) {
		model.addAttribute("cr", new CylinderTransactions());
		return "CylinderReturn";
	}
	
	@RequestMapping(value = "/CylinderReturn", method = RequestMethod.POST)
	public ModelAndView processReturn (@RequestParam(value="cid")
	String cylId,@RequestParam(value="lorryNo")
	String lorryNo,@RequestParam(value="dealerId") String dealerId,
	final CylinderTransactions cylinder, HttpSession session) {
			ModelAndView result = new ModelAndView("CylinderReturn", "MSG",
					"Registration Failed");
			//String dealerId = cylinderS.getDealerFromCylinder(cylId);
			String c = cylId.replaceAll("\\s{2,}", " ").trim();
			String[] cno = c.split(" ");
			ArrayList<Long> cylinderNumbers = new ArrayList<>();
			for(String s:cno){
				Long l = Long.valueOf(s);
				if(l instanceof Long){
					cylinderNumbers.add(l);
				}
			}
			
			String validDealer = dealerU.login(dealerId);
			TreeSet<String> differentDealer = new TreeSet<>();
			TreeSet<Long> noBillCno = new TreeSet<>();
			TreeSet<Long> noRegisterno = new TreeSet<>();
			TreeSet<String> oldCylinder = new TreeSet<>();
			if(validDealer.equals(dealerId)){
				for(int i=0;i<cylinderNumbers.size();i++){
					Long cn = cylinderNumbers.get(i);
					cylinder.setCylinderId(cn);
					cylinder.setDealerId(dealerId);	
					String status = cylinderS.returnCylinder(cylinder);
					
					if(status.equals("SUCCESS")){
					ECRTransactionBean ecr = new ECRTransactionBean();
					ecr.setCylinderId(cn);
					ecr.setDealerId(dealerId);
					ecr.setEcrStatus("N");
					ecr.setLorryNo(lorryNo);
					ecrS.updateECRTx(ecr);//updates ECRTx table after returning cylinder
					}else if(status.startsWith("DIFF")){
						differentDealer.add(status.substring(4));
					}else if (status.startsWith("NOBILL")){
						noBillCno.add(cn);
					}else if(status.startsWith("NOREGISTER")){
						noRegisterno.add(cn);
					}else if(status.equals("FAIL")){
						oldCylinder.add(cno+" = Old Cylinder");
					}
					}
				dealerU.updateCylinderCount(dealerId);//work done
				}
				else{
						result = new ModelAndView("CylinderReturn", "bad","Dealer Id is incorrect! Check again");
					}
			
			return result;
	}	
}