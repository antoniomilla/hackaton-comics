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
	<img src="${comic.image }" width="200" height="200"/>
</div>

<div>
	<p><spring:message code="comic.name"/>: <jstl:out value="${comic.name }"></jstl:out></p>
	<p><spring:message code="comic.publisher"/>: <a href="publisher/display.do?publisherId=${comic.publisher.id }"><jstl:out value="${comic.publisher.name }"></jstl:out></a></p>
	<p><spring:message code="comic.author"/>: <a href="author/display.do?authorId=${comic.author.id }"><jstl:out value="${comic.author.name }"></jstl:out></a></p>
</div>


<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comicCharacters" id="row">
	
	<display:column property="alias"/>
	
	<display:column>
		<a href="comicCharacter/display.do?comicCharacterId=${row.id }">
		<spring:message code="comic.display"/>
		</a>
	</display:column>
	
	</display:table>
</div>