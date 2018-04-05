package com.pio.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SalesDAOI implements SalesDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public String fetchDealerSales(Date frDate, Date tDate, String dealerId) {
		String result = "";
		
		Session session = sessionFactory.getCurrentSession();
		
		try{
			String hql = "select billNo,cylinderType,"
					+ "totalCylinders,quantity,saleValue,CGST,SGST,billValue,billDate from "
					+ "GenerateInvoiceBean where dealerId=:dealerId and billDate "
					+ "between :fromDate and :toDate";
			Query query = session.createQuery(hql).setParameter("dealerId", dealerId).
					setParameter("fromDate", frDate).setParameter("toDate", tDate);
			List list = query.list();
			
			String billNo="",cylinderType="",totalCylinders="",quantity="",saleValue="",
					CGST="",SGST="",billValue="",billDate="";
			
			for(Iterator it = list.iterator();it.hasNext();){
				Object[] ob = (Object[])it.next();
				billNo +=(Long)ob[0]+";";
				cylinderType +=(String)ob[1]+";";
				totalCylinders +=(Integer)ob[2]+";";
				quantity +=(Double)ob[3]+";";
				saleValue +=(Double)ob[4]+";";
				CGST +=(Double)ob[5]+";";
				SGST +=(Double)ob[6]+";";
				billValue +=(Double)ob[7]+";";
				billDate +=(Timestamp)ob[8]+";";
			}
			
			if(!billNo.isEmpty()){
				result = billNo+"#"+cylinderType+"#"+totalCylinders+"#"+quantity+"#"
			+saleValue+"#"+CGST+"#"+SGST+"#"+billValue+"#"+billDate;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

}
