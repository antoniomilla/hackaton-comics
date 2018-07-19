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
	name="publishers" requestURI="${requestURI}" id="row">
	
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="publisher/edit.do?publisherId=${row.id}">
				<spring:message	code="publisher.edit" />
			</a>
		</display:column>		
	</security:authorize>
	
	<spring:message code="publisher.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />

	<spring:message code="publisher.fundacion" var="foundationDateHeader" />
	<display:column property="foundationDate" title="${foundationDateHeader}" sortable="true" />

	<spring:message code="publisher.descripcion" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" sortable="true" />
	
	<display:column>
		<a href="publisher/display.do?publisherId=${row.id }">
		<spring:message code="publisher.display"/>
		</a>
	</display:column>

</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="publisher/create.do"> <spring:message
				code="publisher.create" />
		</a>
	</div>
</security:authorize>