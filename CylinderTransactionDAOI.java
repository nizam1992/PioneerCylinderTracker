package com.pio.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pio.model.CylinderTransactions;

@Repository
public class CylinderTransactionDAOI implements CylinderTransactionDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public String transact(CylinderTransactions buy) {
		String result="";
		String dealerId = buy.getDealerId();
		Long cId = buy.getCylinderId();
		String cylType = buy.getCylinderType();
		Session session = sessionFactory.getCurrentSession();
		try {
			//Alter Cylinder Table after transaction
			String hql = "update Cylinder set usageStatus =:dealerId, billGenerated ='N' "
					+ "where cylinderId =:cId";
			session.createQuery(hql).setParameter("cId", cId)
			.setParameter("dealerId", dealerId).executeUpdate();
			session.saveOrUpdate(buy);
			
			hql = "select count(usageStatus) from Cylinder where usageStatus=:dealerId";
			Long count = (Long)session.createQuery(hql).setParameter("dealerId", dealerId).uniqueResult();
			hql ="update DealerDetails set totalCylindersTaken=:count1 where dealerId=:dealerId";
			session.createQuery(hql).setParameter("count1", count).setParameter("dealerId", dealerId).executeUpdate();
			if(cylType.contains("OXYGEN")) {
				hql = "select count(cylinderType) from CylinderTransactions where dealerId=:dealerId "
						+ "and cylinderType=:cylType and liveTx='Y'";
				count = (Long)session.createQuery(hql).setParameter("dealerId", dealerId)
						.setParameter("cylType", cylType).uniqueResult();
				hql ="update DealerDetails set o2No=:count1 where dealerId=:dealerId";
				session.createQuery(hql).setParameter("count1", count).setParameter("dealerId", dealerId).executeUpdate();
				}
			else if(cylType.contains("NITROGEN")) {
				hql = "select count(cylinderType) from CylinderTransactions where dealerId=:dealerId "
						+ "and cylinderType=:cylType and liveTx='Y'";
				count = (Long)session.createQuery(hql).setParameter("dealerId", dealerId)
						.setParameter("cylType", cylType).uniqueResult();
				hql ="update DealerDetails set n2No=:count1 where dealerId=:dealerId";
				session.createQuery(hql).setParameter("count1", count).setParameter("dealerId", dealerId).executeUpdate();
				
			}else {
				
			}
			result = "SUCCESS";
			}
			catch (Exception e) {
				e.printStackTrace();
				result = "FAIL";
			}
		return result;
	}
	@Transactional
	public Long findSequenceID () {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("select PIO_TX_SEQ.NEXTVAL from dual");
		result = ((BigDecimal) query.uniqueResult()).longValue();
		return result;
	}
	
	
	@Transactional
	public String listCylinder(String dealerId){
		String result = "";
		Session session = sessionFactory.getCurrentSession();
		try {
			String hql = "select TransactionId from CylinderTransactions where dealerId=:dealerId and DOR is null";
			Query query = session.createQuery(hql).setParameter("dealerId", dealerId);	
			List txList =  query.list();
			String list = "";
			for(Iterator<String> i = txList.iterator();i.hasNext();){
				String val =  i.next();
				list+= val+";";				
				}
			return list;
		}
			catch (Exception e) {
				e.printStackTrace();
				result = "FAIL";
			}
		return result;
	}
	
	@Transactional
	public String returnCylinder(String txId){
		String result = "";
		CylinderTransactions cylTx = new CylinderTransactions();
		Session session = sessionFactory.getCurrentSession();
		try {
			//Alter Cylinder Table after transaction
			String[] txIds = txId.split(";");
			Timestamp time = Timestamp.valueOf(LocalDateTime.now());
			String hql = "update CylinderTransactions set DOR =:time where transactionId =:txId";
			for(String tx:txIds){
				session.createQuery(hql).setParameter("txId", tx).setParameter("time", time).executeUpdate();
			}
		}
			catch (Exception e) {
				e.printStackTrace();
				result = "FAIL";
			}
		return result;
	}
	@Override
	@Transactional
	public TreeMap<Integer,String> listCylinders(String dealerId) {
		TreeMap<Integer,String> result = new TreeMap<Integer,String>();
		Session session = sessionFactory.getCurrentSession();
		try {
			String hql = "select cylinderId,cylinderType from CylinderTransactions where dealerId =:dealerId and "
					+ "liveTx='Y'";
			Query query = session.createQuery(hql).setParameter("dealerId", dealerId);
			String cylinderType = "";
			Integer cylinderId = 0;
				List lists =  query.list();
				for(Iterator i = lists.iterator();i.hasNext();){
					Object[] object = (Object[]) i.next();
					cylinderId = (Integer) object[0];
					cylinderType = (String) object[1];
					result.put(cylinderId,cylinderType);
				}
		}
			catch (Exception e) {
				e.printStackTrace();
				result = null;
			}
		return result;
	}
	@Override
	@Transactional
	public String returnCylinders(CylinderTransactions cylinders) {
		String result ="";
		Session session = sessionFactory.getCurrentSession();
		String dealerId = cylinders.getDealerId();
		Long cylId =cylinders.getCylinderId();
		try {
			Timestamp time = Timestamp.valueOf(LocalDateTime.now());
			String hql = "select usageStatus from Cylinder where cylinderId=:cno";
			String d1 = (String)session.createQuery(hql).setParameter("cno", cylId).uniqueResult();
			String d2 ="";
			if(!d1.equals("0")){
			hql = "select dealerId from CylinderTransactions where liveTx='Y' and cylinderId=:cno and DOR is null";
			d2 = (String)session.createQuery(hql).setParameter("cno", cylId).uniqueResult();
				if(!d2.isEmpty()){			
								if(d1.equals(d2) && d1.equals(dealerId)){
								hql = "update Cylinder set usageStatus ='0',lastModifiedDate=:lmd,billGenerated='N' where "
										+ "usageStatus =:dealerId and cylinderId=:cylId";
								session.createQuery(hql).setParameter("lmd", time).setParameter("dealerId", dealerId).
								setParameter("cylId", cylId).executeUpdate();
							
								hql = "update CylinderTransactions set DOR =:lmd,liveTx='N' where "
										+ "dealerId =:dealerId and cylinderId=:cylId and DOR is null";
								session.createQuery(hql).setParameter("lmd", time).setParameter("dealerId", dealerId).
								setParameter("cylId", cylId).executeUpdate();
								result ="SUCCESS";
								}else if(d1.equals(d2)){
									if(!d1.equals("dealerId"))
											result = "DIFF"+d1+"-"+cylId;
								}
			}else{
				result = "FAIL";
			}
			}
			else if(d1.equals("0")){
				result ="NOBILL"+cylId;
			}
			else {
				result ="NOREGISTER"+cylId;
			}
		}
			catch (Exception e) {
				e.printStackTrace();
				result = "FAIL";
			}
		return result;
	}
	@Override
	@Transactional
	public String usageStatus(Long cylinderId) {
		String result = "";
		Session session = sessionFactory.getCurrentSession();
		try {
			
			String hql = "select usageStatus from Cylinder where cylinderId=:cId and damage='N'";
			result =(String) session.createQuery(hql).setParameter("cId", cylinderId).uniqueResult();
			if(result instanceof String) {
				return result;
			}
			else result ="FAIL";
			
	}catch (Exception e) {
			e.printStackTrace();
			result = "FAIL";
		}
	return result;
	}
	@Override
	@Transactional
	public Long findBillID() {
		Long result = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("select PIO_BILL_SEQ.NEXTVAL from dual");
		result = ((BigDecimal) query.uniqueResult()).longValue();
		return result;
	}
}
