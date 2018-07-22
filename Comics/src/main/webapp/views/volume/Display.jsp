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

<div style="position:absolute;top:270px;left:230px;">
	<p><spring:message code="comic.name"/>: <jstl:out value="${comic.name }"></jstl:out></p>
	<p><spring:message code="comic.publisher"/>: <jstl:out value="${comic.publisher.name }"></jstl:out></p>
	<p><spring:message code="comic.author"/>: <jstl:out value="${comic.author.name }"></jstl:out></p>
</div>


<div style="position:absolute;top:200px;left:400px;">
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comicCharacters" id="row">
	
	<display:column property="alias"/>
	
	<display:column>
		<a href="comicCharacter/display.do?comicCharacterId=${row.id }">
		<spring:message code="comic.display"/>
		</a>
	</display:column>
	
	</display:table>
</div>