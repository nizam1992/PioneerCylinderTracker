package com.pio.util;

import java.sql.Date;

public interface SalesUtil {

	String fetchDealerSales(Date frDate, Date tDate, String dealerId);

}
