<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="messageFolder/edit.do" modelAttribute="messageFolder">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="name"/>
	<form:hidden path="owner"/>
	<form:hidden path="messages"/>
	
	

	<form:label path="nameForDisplay">
		<spring:message code="messageFolder.name" />:
	</form:label>
	<form:input path="nameForDisplay" />
	<form:errors cssClass="error" path="nameForDisplay" />
	<br />


	<input type="submit" name="save"
		value="<spring:message code="messageFolder.save" />" />&nbsp; 
	<jstl:if test="${messageFolder.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="messageFolder.delete" />"
			onclick="return confirm('<spring:message code="messageFolder.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="messageFolder.cancel" />"
		onclick="javascript: relativeRedir('messageFolder/list.do');" />


</form:form>