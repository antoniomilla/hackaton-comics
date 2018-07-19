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
	name="comicCharacters" requestURI="${requestURI}" id="row">
	
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="comicCharacter/edit.do?comicCharacterId=${row.id}">
				<spring:message	code="comicCharacter.edit" />
			</a>
		</display:column>		
	</security:authorize>
	
	<spring:message code="comicCharacter.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />

	<spring:message code="comicCharacter.alias" var="aliasHeader" />
	<display:column property="alias" title="${aliasHeader}" sortable="true" />

	<spring:message code="comicCharacter.city" var="cityHeader" />
	<display:column property="city" title="${cityHeader}" sortable="false" />
	
	<spring:message code="comicCharacter.publisher" var="publisherHeader" />
	<display:column property="publisher.name" title="${publisherHeader}" sortable="true" />

	<display:column>
		<a href="comicCharacter/display.do?comicCharacterId=${row.id }">
		<spring:message code="comicCharacter.display"/>
		</a>
	</display:column>

</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="comicCharacter/create.do"> <spring:message
				code="comicCharacter.create" />
		</a>
	</div>
</security:authorize>