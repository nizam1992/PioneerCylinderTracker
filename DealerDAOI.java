package com.pio.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pio.model.DealerCylinderTxn;

@Repository
public class DealerDAOI implements DealerDAO {

	@Autowired
	SessionFactory sessionFactory;
	@Override
	@Transactional
	public String login(String dealerId){
		String result="FAIL";
		   Session session = sessionFactory.getCurrentSession();
			try {
				String hql = "select dealerId from DealerDetails where dealerId=:dId";
				String dealer = (String)session.createQuery(hql).setParameter("dId", dealerId).uniqueResult();
				if(dealer instanceof String) {
					result = dealer;
				}
				else {
					result ="FAIL";
				}
				
			} catch (Exception e) {
				System.out.println("Exception occured");
				e.printStackTrace();
				result = "FAIL";
			}
		return result;
	}
	@Override
	@Transactional
	public TreeMap dealerList() {
		Session session = sessionFactory.getCurrentSession();
		TreeMap<String,String> result = new TreeMap<String,String>();
		try {
			String hql = "select dealerId,dealerName from DealerDetails";
			String dealerName="",dealerId="";
			Query query = session.createQuery(hql);
			List pwd =  query.list();
			for(Iterator i = pwd.iterator();i.hasNext();){
				Object[] object = (Object[]) i.next();
				dealerId = (String) object[0];
				dealerName = (String) object[1];
				result.put(dealerId, dealerName);
			}
				
		} catch (Exception e) {
			System.out.println("Query Exception! Check password/user Not authenticated");
			e.printStackTrace();
			result = null;
		}
		return result;
}
	@Override
	@Transactional
	public String getDealer(String dealerId) {
		Session session = sessionFactory.getCurrentSession();
		String result ="",dealerName="",address="",contactNo="",gstin="";
		double rateo2=0.0,raten2=0.0;
		Timestamp dateOfRegistration = null;
		Long o2No=(long)0,n2No=(long)0;
		try {
			String hql = "select dealerId,dealerName,gstin,rateo2,raten2,address,contactNo,o2No,n2No,dateOfRegistration from DealerDetails where dealerId=:Id";
			Query query = session.createQuery(hql).setParameter("Id", dealerId);
			List li =  query.list();
			for(Iterator i = li.iterator();i.hasNext();){
				Object[] object = (Object[]) i.next();
				dealerId = (String) object[0];
				dealerName = (String) object[1];
				gstin = (String) object[2];
				rateo2 = (Double) object[3];
				raten2 = (Double) object[4];
				address = (String) object[5];
				contactNo = (String) object[6];
				o2No = (Long) object[7];
				n2No = (Long) object[8];
				dateOfRegistration = (Timestamp) object[9];
			}
			result = dealerId+";"
			+dealerName+";"
					+gstin+";"
					+rateo2+";"
					+raten2+";"
			+address+";"
					+contactNo+";"
					+o2No+";"
			+n2No+";"
			+dateOfRegistration;
			System.out.println("o2No and n2No"+o2No+" "+n2No);
		} catch (Exception e) {
			System.out.println("DealerDAOImpl");
			e.printStackTrace();
			result = "FAIL";
	}
		return result;
}
	@Override
	@Transactional
	public String getDealerFCylinder(Long cylinderId) {
		Session session = sessionFactory.getCurrentSession();
		String result = "FAIL";
		try {
			String hql = "select usageStatus from Cylinder where cylinderId=:cId";
			String dealerId = (String) session.createQuery(hql).setParameter("cId", cylinderId).uniqueResult();
			if(dealerId instanceof String)
				result =dealerId;
			else result = "BAD";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	@Transactional
	public void updateCylinderCount(String dealerId) {
		Session session = sessionFactory.getCurrentSession();
		try {
			String hql ="select cylinderType from Cylinder where dealerId=:dId";
			Query query = session.createQuery(hql).setParameter("dId", dealerId);
			Long o2 =(long)0,n2=(long)0,air=(long)0;
			Long total = (long)0;
			String gasType ="";
			List list = query.list();
			for(Iterator i = list.iterator();i.hasNext();){
				Object ob = (Object)i.next();
				gasType = (String)ob;
				if(!gasType.isEmpty()){
				if(gasType.contains("OXYGEN")){
					o2++;
				}else if(gasType.contains("NITROGEN")){
					n2++;
				}else if(gasType.contains("AIR"))
					air++;
				}

			}
			
			total = o2+n2+air;
			hql = "update DealerDetails set o2No=:o2,n2No=:n2,air=:air,totalCylindersTaken=:total where dealerId=:dId";
			session.createQuery(hql).setParameter("o2", o2).setParameter("n2", n2).setParameter("total", total).setParameter("air", air).
			setParameter("dId", dealerId).executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();;
		}
	}
	@Override
	@Transactional
	public String getRate(String dealerId) {
		Session session = sessionFactory.getCurrentSession();
		String result ="FAIL";
		double rateo2=0.0,raten2=0.0,rateair=0.0;
		try {
			String hql = "select rateo2,raten2,rateair from DealerDetails where dealerId=:Id";
			Query query = session.createQuery(hql).setParameter("Id", dealerId);
			List li =  query.list();
			Iterator i = li.iterator();
			if(i.hasNext()) {
				Object[] object = (Object[]) i.next();
				rateo2 = (Double) object[0];
				raten2 = (Double) object[1];
				rateair = (Double) object[2];
			}
			result = rateo2+"-"+raten2+"-"+rateair;
		} catch (Exception e) {
			e.printStackTrace();
	}
		return result;
}
	@Override
	@Transactional
	public Double dealerCylinderRate(String cylinderType, String dealerId) {
		Session session = sessionFactory.getCurrentSession();
		Double result =0.0;
		double rate=0.0;
		try {
			String hql = "";
			if(cylinderType.contains("OXYGEN")) {
				hql = "select drateo2 from DealerDetails where dealerId=:Id";
				rate = (Double)session.createQuery(hql).setParameter("Id", dealerId).uniqueResult();
			}else if(cylinderType.contains("NITROGEN")) {
				hql = "select draten2 from DealerDetails where dealerId=:Id";
				rate = (Double)session.createQuery(hql).setParameter("Id", dealerId).uniqueResult();
			}
			else if(cylinderType.equals("Air")) {
				hql = "select drateair from DealerDetails where dealerId=:Id";
				rate = (Double)session.createQuery(hql).setParameter("Id", dealerId).uniqueResult();
			}
			if(rate!=0.0) {
			result = rate;
			}else {
				result = 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
	}
		return result;
}
	@Override
	@Transactional
	public Long getDealerCylinderSequence() {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("select PIO_DCTX_SEQ.NEXTVAL from dual");
		result = ((BigDecimal) query.uniqueResult()).longValue();
		return result;
	}
	
	@Override
	@Transactional
	public String updateDealerCylinderTable(DealerCylinderTxn dct) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(dct);
		return "SUCCESS";
	}
	@Override
	@Transactional
	public String updateTxIds(String[] txId) {
		Session session = sessionFactory.getCurrentSession();
		String result ="FAIL";
		
		try {
			String hql = "update DealerCylinderTxn set billGenerated='Y' where transactionId=:tx";
			String[] txp = txId[0].split(";");
			for(String t:txp) {
				Long l = Long.valueOf(t);
				System.out.println("Transactiona ID @ DealerDAOI"+l);
				if(l instanceof Long) {
					session.createQuery(hql).setParameter("tx", l).executeUpdate();
				}
			}
			result = "SUCCESS";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}