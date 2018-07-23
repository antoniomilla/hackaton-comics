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
	<img src="${comicCharacter.image }" width="200" height="200"/>
</div>

<div>
	<p><spring:message code="comicCharacter.name"/>: <jstl:out value="${comicCharacter.name }"></jstl:out></p>
	<p><spring:message code="comicCharacter.alias"/>: <jstl:out value="${comicCharacter.alias }"></jstl:out>
	<p><spring:message code="comicCharacter.city"/>: <jstl:out value="${comicCharacter.city }"></jstl:out>
	<p><spring:message code="comicCharacter.publisher"/>: <a href="publisher/display.do?publisherId=${comicCharacter.publisher.id }"><jstl:out value="${comicCharacter.publisher.name }"></jstl:out></a>
	<p><spring:message code="comicCharacter.description"/>: <jstl:out value="${comicCharacter.description }"></jstl:out>
</div>


<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comics" id="row">
	
	<display:column property="name"/>
	
	<display:column>
		<a href="comic/display.do?comicId=${row.id }">
		<spring:message code="comicCharacter.display"/>
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
		<a href="comment/comicCharacter/create.do?comicCharacterId=${comicCharacter.id }"><spring:message code="comment.create"/></a>
	</div>
</security:authorize>