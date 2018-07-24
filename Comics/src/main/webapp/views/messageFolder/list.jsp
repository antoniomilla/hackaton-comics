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
	name="messageFolders" requestURI="${requestURI}" id="row">
	
	<security:authorize access="isAuthenticated()">
		<display:column>
			<a href="messageFolder/edit.do?messageFolderId=${row.id}">
				<spring:message	code="messageFolder.edit" />
			</a>
		</display:column>
	</security:authorize>
	
	<spring:message code="messageFolder.name" var="nameForDisplayHeader" />
	<display:column property="nameForDisplay" title="${nameForDisplayHeader}" sortable="true" />
	
	<display:column>
		<a href="directMessage/list.do?messageFolderId=${row.id }">
		<spring:message code="messageFolder.display"/>
		</a>
	</display:column>

</display:table>


<security:authorize access="hasRole('USER')">
	<div>
		<a href="messageFolder/create.do"> <spring:message
				code="messageFolder.create" />
		</a>
	</div>
</security:authorize>