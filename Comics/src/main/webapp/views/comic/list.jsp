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
	name="comics" requestURI="${requestURI}" id="row">
	
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="comic/edit.do?comicId=${row.id}">
				<spring:message	code="comic.edit" />
			</a>
		</display:column>		
	</security:authorize>
	
	<security:authorize access="hasRole('CLIENTE')">
		<display:column>
			<jstl:choose>
				<jstl:when test="${leidos.contains(row)}">
					<a href="cliente/comic/noLeido.do?comicId=${row.id}" 
					   onclick="javascript: return confirm('<spring:message code="comic.confirm.marcar.no.leido" />')">
						<spring:message code="comic.no.leido" />
					</a>					
				</jstl:when>
				<jstl:otherwise>
					<a href="cliente/comic/leido.do?comicId=${row.id}">
						<spring:message code="comic.leido" />
					</a>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</security:authorize>
	
	<spring:message code="comic.nombre" var="nombreHeader" />
	<display:column property="nombre" title="${nombreHeader}" sortable="true"/>

	<spring:message code="comic.editorial" var="editorialHeader" />
	<display:column property="editorial.nombre" title="${editorialHeader}" sortable="true" />

	<spring:message code="comic.autor" var="autorHeader" />
	<display:column property="autor.nombre" title="${autorHeader}" sortable="true" />
	
	<spring:message code="comic.numPaginas" var="numPaginasHeader" />
	<display:column property="numPaginas" title="${numPaginasHeader}" sortable="true" />
	
	
	<display:column>
		<a href="comic/display.do?comicId=${row.id }">
		<spring:message code="comic.ver"/>
		</a>
	</display:column>

	
</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="comic/create.do"> <spring:message
				code="comic.create" />
		</a>
	</div>
</security:authorize>