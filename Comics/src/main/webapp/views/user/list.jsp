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
	name="users" requestURI="${requestURI}" id="row">

	
	<display:column>
		<a href="user/displayUser.do?userId=${row.id }">
			<spring:message code="user.display"/>
		</a>
	</display:column>
	
	<spring:message code="user.nickname" var="nameHeader" />
	<display:column property="nickname" title="${nameHeader}" sortable="true" />

	<spring:message code="user.level" var="levelHeader" />
	<display:column property="level" title="${levelHeader}" sortable="true" />
	
	<spring:message code="user.blocked" var="blockedHeader" />
	<display:column property="blocked" title="${blockedHeader}" sortable="true" />
	
	<spring:message code="user.blockReason" var="blockReasonHeader" />
	<display:column property="blockReason" title="${blockReasonHeader}" sortable="true" />

	<spring:message code="user.trusted" var="trustedHeader" />
	<display:column property="trusted" title="${trustedHeader}" sortable="true" />
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:choose>
				<jstl:when test="${row.trusted==false}">
					<a href="administrator/trust.do?userId=${row.id}">
						<spring:message code="user.untrusted" />
					</a>					
				</jstl:when>
				<jstl:otherwise>
					<a href="administrator/untrust.do?userId=${row.id}">
						<spring:message code="user.trusted" />
					</a>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:choose>
				<jstl:when test="${row.blocked==false}">
					<a href="administrator/block.do?userId=${row.id}">
						<spring:message code="user.unblocked" />
					</a>					
				</jstl:when>
				<jstl:otherwise>
					<a href="administrator/unblock.do?userId=${row.id}">
						<spring:message code="user.blocked" />
					</a>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</security:authorize>

</display:table>
