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
	<img src="${volume.image }" width="200" height="200"/>
</div>

<div>
	<p><spring:message code="volume.name"/>: <jstl:out value="${volume.name }"></jstl:out></p>
	<p><spring:message code="volume.publisher"/>: <jstl:out value="${volume.publisher.name }"></jstl:out></p>
	<p><spring:message code="volume.author"/>: <jstl:out value="${volume.author.name }"></jstl:out></p>
	<p><spring:message code="volume.description"/>: <jstl:out value="${volume.description }"></jstl:out></p>
	<p><spring:message code="volume.chapterCount"/>: <jstl:out value="${volume.chapterCount }"></jstl:out></p>
</div>


<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comicCharacters" id="row">
	
	<display:column property="alias"/>
	
	<display:column>
		<a href="comicCharacter/display.do?comicCharacterId=${row.id }">
		<spring:message code="volume.display"/>
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
		<a href="comment/create.do?comicId=${comic.id }"><spring:message code="comment.create"/></a>
	</div>
</security:authorize>