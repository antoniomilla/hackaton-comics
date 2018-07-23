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
	<img src="${author.image }" width="200" height="200"/>
</div>

<div>
	<p><spring:message code="author.name"/>: <jstl:out value="${author.name }"></jstl:out></p>
	<p><spring:message code="author.birthDate"/>: <jstl:out value="${author.birthDate }"></jstl:out></p>
	<p><spring:message code="author.birthPlace"/>: <jstl:out value="${author.birthPlace }"></jstl:out></p>
	<p><spring:message code="author.description"/>: <jstl:out value="${author.description }"></jstl:out></p>

</div>


<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comics" id="row">
	
	<display:column property="name"/>
	
	<display:column>
		<a href="comic/display.do?comicId=${row.id }">
		<spring:message code="author.display"/>
		</a>
	</display:column>
	
	</display:table>
</div>

<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="volumes" id="row">
	
	<display:column property="name"/>
	
	<display:column>
		<a href="volume/display.do?volumeId=${row.id }">
		<spring:message code="author.display"/>
		</a>
	</display:column>
	
	</display:table>
	
</div>

<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comments" id="row">
	
	<spring:message code="comment.text" var="textHeader" />
	<display:column property="text" title="${textHeader}" sortable="true"/>
	
	<spring:message code="comment.creationTime" var="creationTimeHeader" />
	<display:column property="creationTime" title="${creationTimeHeader}" sortable="true"/>
	
	<spring:message code="comment.user" var="userHeader" />
	<display:column property="user.nickname" title="${userHeader}" sortable="true"/>
	
	</display:table>
</div>

<security:authorize access="hasRole('USER')">
	<div>
		<a href="comment/author/create.do?authorId=${author.id }"><spring:message code="comment.create"/></a>
	</div>
</security:authorize>

