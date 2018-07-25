<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="directMessage/edit.do" modelAttribute="directMessage">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="administrationNotice"/>
	<form:hidden path="messageFolder"/>
	<form:hidden path="sender"/>
	

	<form:label path="subject">
		<spring:message code="directMessage.subject" />:
	</form:label>
	<form:input path="subject" />
	<form:errors cssClass="error" path="subject" />
	<br />
	
	<form:label path="body">
		<spring:message code="directMessage.body" />:
	</form:label>
	<form:textarea path="body" />
	<form:errors cssClass="error" path="body" />
	<br />

	<form:label path="recipient">
		<spring:message code="directMessage.recipient" />:
	</form:label>
	<form:select id="recipients" path="recipient" >
		<form:option value="0" label="----" />		
		<form:options items="${recipients}" itemValue="id"
			itemLabel="nickname" />
	</form:select>
	<form:errors cssClass="error" path="recipient" />

	<input type="submit" name="save"
		value="<spring:message code="directMessage.save" />" />&nbsp; 
	<jstl:if test="${directMessage.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="directMessage.delete" />"
			onclick="return confirm('<spring:message code="directMessage.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="directMessage.cancel" />"
		onclick="javascript: relativeRedir('directMessage/list.do');" />
	<br />


</form:form>