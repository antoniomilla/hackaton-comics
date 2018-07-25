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

<form:form action="publisher/edit.do" modelAttribute="publisher">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:label path="name">
		<spring:message code="publisher.name" />:
	</form:label>
	<form:input path="name" />
	<form:errors cssClass="error" path="name" />
	<br />

	<form:label path="foundationDate">
		<spring:message code="publisher.foundationDate" />:
	</form:label>
	<form:input path="foundationDate" />
	<form:errors cssClass="error" path="foundationDate" />
	<br />
	
	<form:label path="description">
		<spring:message code="publisher.description" />:
	</form:label>
	<form:textarea path="description" />
	<form:errors cssClass="error" path="description" />
	<br />

	<form:label path="image">
		<spring:message code="publisher.image" />:
	</form:label>
	<form:input path="image" />
	<form:errors cssClass="error" path="image" />
	<br />
	
	<input type="submit" name="save"
		value="<spring:message code="publisher.save" />" />&nbsp; 
	<jstl:if test="${publisher.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="publisher.delete" />"
			onclick="return confirm('<spring:message code="publisher.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="publisher.cancel" />"
		onclick="javascript: relativeRedir('publisher/list.do');" />
	<br />


</form:form>