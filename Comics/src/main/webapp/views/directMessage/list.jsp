<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="directMessages" requestURI="${requestURI}" id="row">
	
	
	<jstl:if test="${name=='Inbox' }">
		<spring:message code="directMessage.sender" var="senderHeader" />
		<display:column property="sender.nickname" title="${senderHeader}" sortable="true" />
	</jstl:if>
	
	<jstl:if test="${name=='Sent' }">
		<spring:message code="directMessage.recipient" var="recipientHeader" />
		<display:column property="recipient.nickname" title="${recipientHeader}" sortable="true" />
	</jstl:if>
	
	<jstl:if test="${name=='Trash' }">
		<spring:message code="directMessage.sender" var="senderHeader" />
		<display:column property="sender.nickname" title="${senderHeader}" sortable="true" />
		<spring:message code="directMessage.recipient" var="recipientHeader" />
		<display:column property="recipient.nickname" title="${recipientHeader}" sortable="true" />
	</jstl:if>
	
	<spring:message code="directMessage.subject" var="subjectHeader"/>
	<display:column property="subject" title="${subjectHeader }" sortable="true"/>
	
	<display:column>
		<a href="directMessage/display.do?directMessageId=${row.id }">
		<spring:message code="directMessage.display"/>
		</a>
	</display:column>

</display:table>


<security:authorize access="hasRole('USER')">
	<div>
		<a href="directMessage/create.do"> <spring:message
				code="directMessage.send" />
		</a>
	</div>
</security:authorize>