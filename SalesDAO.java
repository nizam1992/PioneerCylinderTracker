package com.pio.dao;

import java.sql.Date;

public interface SalesDAO {

	String fetchDealerSales(Date frDate, Date tDate, String dealerId);

}
