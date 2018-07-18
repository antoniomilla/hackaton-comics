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
	name="editoriales" requestURI="${requestURI}" id="row">
	
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="editorial/edit.do?editorialId=${row.id}">
				<spring:message	code="editorial.edit" />
			</a>
		</display:column>		
	</security:authorize>
	
	<spring:message code="editorial.nombre" var="editorialHeader" />
	<display:column property="nombre" title="${editorialHeader}" sortable="true" />

	<spring:message code="editorial.fundacion" var="fundacionHeader" />
	<display:column property="fundacion" title="${fundacionHeader}" sortable="true" />

	<spring:message code="editorial.descripcion" var="descripcionHeader" />
	<display:column property="descripcion" title="${descripcionHeader}" sortable="false" />
	
	<display:column>
		<a href="editorial/display.do?editorialId=${row.id }">
		<spring:message code="editorial.ver"/>
		</a>
	</display:column>

</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="editorial/create.do"> <spring:message
				code="editorial.create" />
		</a>
	</div>
</security:authorize>