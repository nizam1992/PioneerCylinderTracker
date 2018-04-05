package com.pio.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pio.util.DealerUtil;
import com.pio.util.SalesUtil;

@Controller
public class SalesController {

	@Autowired
	private SalesUtil saleU;
	
	@Autowired
	private DealerUtil dealerU;
	
	@RequestMapping(value="/dealerSales",method=RequestMethod.GET)
	public String getDealerWise(){
		return "DealerSales";
	}
	
	@RequestMapping(value="/dealerSales", method=RequestMethod.POST)
	public ModelAndView processDealerWise(@RequestParam(value="fromDate") String fromDate,
			@RequestParam(value="toDate") String toDate, @RequestParam(value="dealerId")
	String dealerId, HttpSession session){
		ModelAndView result = new ModelAndView("DealerSales","fail","Try again!");
		String dstatus = dealerU.login(dealerId);
		if(!dstatus.equals("FAIL")){
			Date frDate = Date.valueOf(fromDate);
			Date tDate = Date.valueOf(toDate);
			String status = saleU.fetchDealerSales(frDate,tDate,dealerId);
			if(!status.isEmpty()){
				String[] s = status.split("#");
				
				//Bill Nos starts here
				String[] bNos = s[0].split(";");
				ArrayList<Long> billNo = new ArrayList<>();
				for(String c:bNos){
					Long l = Long.valueOf(c);
					if(l instanceof Long){
						billNo.add(l);
					}
				}
				session.setAttribute("billNo", billNo);
				//Bill Nos ends here
				
				//Cylinder type starts here
				String[] cType = s[1].split(";");
				ArrayList<String> cylinderType = new ArrayList<>();
				for(String c:cType){
					cylinderType.add(c);
				}
				session.setAttribute("cylinderType", cylinderType);
				//Cylinder type ends here
				
				String[] tCyl = s[2].split(";");
				ArrayList<Integer> totalCylinders = new ArrayList<>();
				for(String c:tCyl){
					Integer i = Integer.valueOf(c);
					if(i instanceof Integer){
						totalCylinders.add(i);
					}
				}
				session.setAttribute("totalCylinders", totalCylinders);
				
				String[] q = s[3].split(";");
				ArrayList<Double> quantity = new ArrayList<>();
				for(String c:q){
					Double d = Double.valueOf(c);
					if(d instanceof Double){
						quantity.add(d);
					}
				}
				session.setAttribute("quantity", quantity);
				
				String[] sv = s[4].split(";");
				ArrayList<Double> saleValue = new ArrayList<>();
				for(String c:sv){
					Double d = Double.valueOf(c);
					if(d instanceof Double){
						saleValue.add(d);
					}
				}
				session.setAttribute("saleValue", saleValue);
				
				String[] cgst = s[5].split(";");
				ArrayList<Double> CGST = new ArrayList<>();
				for(String c:cgst){
					Double d = Double.valueOf(c);
					if(d instanceof Double){
						CGST.add(d);
					}
				}
				session.setAttribute("CGST", CGST);
				
				String[] sgst = s[6].split(";");
				ArrayList<Double> SGST = new ArrayList<>();
				for(String c:sgst){
					Double d = Double.valueOf(c);
					if(d instanceof Double){
						SGST.add(d);
					}
				}						
				session.setAttribute("SGST", SGST);
				
				
				String[] bv = s[7].split(";");
				ArrayList<Double> billValue = new ArrayList<>();
				for(String c:bv){
					Double d = Double.valueOf(c);
					if(d instanceof Double){
						billValue.add(d);
					}
				}
				session.setAttribute("billValue", billValue);
				
				String[] bd = s[8].split(";");
				ArrayList<LocalDate> billDate = new ArrayList<>();
				for(String c:bd){
					Timestamp d = Timestamp.valueOf(c);
					if(d instanceof Timestamp){
						LocalDate ld = d.toLocalDateTime().toLocalDate();
						billDate.add(ld);
					}
				}
				session.setAttribute("billDate", billDate);
				
			}
			
		}
		
		return result;
	}
	
	@RequestMapping(value="/companySales",method=RequestMethod.GET)
	public String getCompanySales(){
		return "DealerSales";
	}
	
	@RequestMapping(value="/companySales", method=RequestMethod.POST)
	public ModelAndView processCompanySales(@RequestParam(value="fromDate") String fromDate,
			@RequestParam(value="toDate") String toDate){
		ModelAndView result = new ModelAndView("DealerSales","fail","Try again!");
		
		
		return result;
	}
}
