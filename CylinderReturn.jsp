<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js" type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js" type="text/javascript"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-theme.min.css">

<title>Return cylinders</title>
<style>
table, th, td {
    border: 1px solid black;
    border-collapse: collapse;
}
th, td {
    padding: 5px;
    text-align: left;
}
</style>
</head>
<body>
<form action="/PioneerCylinderTracker/CylinderReturn" method="post">
<center>
<div style="color:green"><h1>${ret}</h1></div>
<div style="color:red"><h1>${bad}${comp}</h1></div>
    <table>   
	<tr>
				<td><b>Lorry Number </b></td><td><input type="text" name="lorryNo" maxlength="10"/></td>
				</tr>
       <tr>        
       <tr>
				<td><b>Dealer ID </b></td><td><input type="text" name="dealerId" maxlength="6"/></td>
				</tr>
       <tr>
       <tr>
				<td><b>Cylinder Numbers </b></td><td><input type="text" name="cid"/></td> <td> Enter cylinder numbers as 123 345 5677</td>
				</tr>
        <tr align="center">
				<td></td><td><input type="submit" name="ReturnCylinder" value="Return Cylinder"
					class="btn btn-success" /></td>
			</tr>
        
</table>
</center>
</form>
</body>

</html>