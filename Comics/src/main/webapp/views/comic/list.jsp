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
	
	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:choose>
				<jstl:when test="${read.contains(row)}">
					<a href="user/comic/unread.do?comicId=${row.id}" 
					   onclick="javascript: return confirm('<spring:message code="comic.confirm.set.unread" />')">
						<spring:message code="comic.unread" />
					</a>					
				</jstl:when>
				<jstl:otherwise>
					<a href="user/comic/read.do?comicId=${row.id}">
						<spring:message code="comic.read" />
					</a>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</security:authorize>
	
	<spring:message code="comic.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true"/>

	<spring:message code="comic.publisher" var="publisherHeader" />
	<display:column property="publisher.name" title="${publisherHeader}" sortable="true" />

	<spring:message code="comic.author" var="authorHeader" />
	<display:column property="author.name" title="${authorHeader}" sortable="true" />
	
	
	
	<display:column>
		<a href="comic/display.do?comicId=${row.id }">
		<spring:message code="comic.display"/>
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