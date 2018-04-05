package com.pio.util;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pio.dao.SalesDAO;
@Service
public class SalesUtility implements SalesUtil {

	@Autowired
	private SalesDAO sDAO;
	
	@Override
	public String fetchDealerSales(Date frDate, Date tDate, String dealerId) {
		return sDAO.fetchDealerSales(frDate,tDate,dealerId);
	}

}
