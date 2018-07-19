<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<form:form action="${requestURI}" modelAttribute="Client">	

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:label path="name">
	<spring:message code="cliente.nombre" />:
	</form:label> 
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	
	<form:label path="birthDate">
	<spring:message code="cliente.fechaNacimiento" />:
	</form:label> 
	<form:input path="birthDate"/>
	<form:errors cssClass="error" path="birthDate"/>
	
	<form:label path="dni">
	<spring:message code="cliente.dni" />:
	</form:label>
	<form:input path="dni"/>
	<form:errors cssClass="error" path="dni"/>
	
	<form:label path="email">
	<spring:message code="cliente.email" />:
	</form:label>
	<form:input path="email"/>
	<form:errors cssClass="error" path="email"/>
	
	<form:hidden path="userAccount"/>
	<form:hidden path="registerDate"/>
	<form:hidden path="level"/>
	<form:hidden path="comicsRead"/>
	
	
	<input type="submit" name="save"
		value="<spring:message code="cliente" />" />&nbsp; 
	<input type="button" name="cancel"
		value="<spring:message code="cliente.cancel" />"
		onclick="javascript: relativeRedir('cliente/list.do');" />
	<br />
	</form:form>