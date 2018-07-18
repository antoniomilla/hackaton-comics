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
	<img src="${autor.imagen }" width="200" height="200"/>
</div>

<div style="position:absolute;top:270px;left:230px;width:150;">
	<p><spring:message code="autor.nombre"/>: <jstl:out value="${autor.nombre }"></jstl:out></p>
	<p><spring:message code="autor.fechaNacimiento"/>: <jstl:out value="${autor.fechaNacimiento }"></jstl:out></p>
	<p><spring:message code="autor.lugarNacimiento"/>: <jstl:out value="${autor.lugarNacimiento }"></jstl:out></p>
</div>


<div style="position:absolute;top:220px;left:600px;">
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comics" id="row">
	
	<display:column property="nombre"/>
	
	<display:column>
		<a href="comic/display.do?comicId=${row.id }">
		<spring:message code="autor.ver"/>
		</a>
	</display:column>
	
	</display:table>
</div>
