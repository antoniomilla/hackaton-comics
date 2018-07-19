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
	name="authors" requestURI="${requestURI}" id="row">
	
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="author/edit.do?authorId=${row.id}">
				<spring:message	code="author.edit" />
			</a>
		</display:column>		
	</security:authorize>
	
	<spring:message code="author.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />

	<spring:message code="author.birthDate" var="birthDateHeader" />
	<display:column property="birthDate" title="${birthDateHeader}" sortable="true" />

	<spring:message code="author.birthPlace" var="birthPlaceHeader" />
	<display:column property="birthPlace" title="${birthPlaceHeader}" sortable="false" />
	
	<display:column>
		<a href="author/display.do?authorId=${row.id }">
		<spring:message code="author.display"/>
		</a>
	</display:column>

</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="author/create.do"> <spring:message
				code="author.create" />
		</a>
	</div>
</security:authorize>