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
	<img src="${comic.imagen }" width="200" height="200"/>
</div>

<div style="position:absolute;top:270px;left:230px;">
	<p><spring:message code="comic.nombre"/>: <jstl:out value="${comic.nombre }"></jstl:out></p>
	<p><spring:message code="comic.numPaginas"/>: <jstl:out value="${comic.numPaginas }"></jstl:out></p>
	<p><spring:message code="comic.editorial"/>: <jstl:out value="${comic.editorial.nombre }"></jstl:out></p>
	<p><spring:message code="comic.autor"/>: <jstl:out value="${comic.autor.nombre }"></jstl:out></p>
</div>


<div style="position:absolute;top:200px;left:400px;">
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="personajes" id="row">
	
	<display:column property="alias"/>
	
	<display:column>
		<a href="personaje/display.do?personajeId=${row.id }">
		<spring:message code="comic.ver"/>
		</a>
	</display:column>
	
	</display:table>
</div>