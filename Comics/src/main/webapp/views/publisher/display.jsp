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
	<img src="${publisher.image }" width="200" height="200"/>
</div>

<div>
	<p><spring:message code="publisher.name"/>: <jstl:out value="${publisher.name }"></jstl:out></p>
	<p><spring:message code="publisher.foundationDate"/>: <jstl:out value="${publisher.foundationDate }"></jstl:out>
	<p><spring:message code="publisher.description"/>: <jstl:out value="${publisher.description }"></jstl:out>
</div>


<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comicCharacters" id="row">
	
	<display:column property="alias"/>
	
	<display:column>
		<a href="comicCharacter/display.do?comicCharacterId=${row.id }">
		<spring:message code="publisher.display"/>
		</a>
	</display:column>
	
	</display:table>
</div>

<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comics" id="row">
	
	<display:column property="name"/>
	
	<display:column>
		<a href="comic/display.do?comicId=${row.id }">
		<spring:message code="publisher.display"/>
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
		<a href="comment/publisher/create.do?publisherId=${publisher.id }"><spring:message code="comment.create"/></a>
	</div>
</security:authorize>