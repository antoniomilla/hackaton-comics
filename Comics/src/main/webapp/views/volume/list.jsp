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
	name="volumes" requestURI="${requestURI}" id="row">

	
	<spring:message code="volume.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />

	<spring:message code="volume.releaseDate" var="levelHeader" />
	<display:column property="releaseDate" title="${levelHeader}" sortable="true" />
	
	<spring:message code="volume.chapterCount" var="levelHeader" />
	<display:column property="chapterCount" title="${levelHeader}" sortable="true" />
	
	<spring:message code="volume.description" var="levelHeader" />
	<display:column property="description" title="${levelHeader}" sortable="true" />
	
	<display:column>
		<a href="volume/display.do?volumeId=${row.id }">
		<spring:message code="volume.display"/>
		</a>
	</display:column>
</display:table>
