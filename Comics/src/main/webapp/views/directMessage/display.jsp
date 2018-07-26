<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<div>
	<p><spring:message code="directMessage.sender"/>: <jstl:out value="${directMessage.sender.nickname }"></jstl:out></p>
	<p><spring:message code="directMessage.recipient"/>: <jstl:out value="${directMessage.recipient.nickname }"></jstl:out></p>
	<p><spring:message code="directMessage.subject"/>: <jstl:out value="${directMessage.subject }"></jstl:out></p>
	<p><spring:message code="directMessage.body"/>: <jstl:out value="${directMessage.body }"></jstl:out></p>
	
	
	<form:form action="directMessage/edit.do" modelAttribute="directMessage">
	<jstl:if test="${directMessage.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="directMessage.delete" />"
			onclick="return confirm('<spring:message code="directMessage.confirm.delete" />')" />&nbsp;
	</jstl:if>
	</form:form>
	
</div>