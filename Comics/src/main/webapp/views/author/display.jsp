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
