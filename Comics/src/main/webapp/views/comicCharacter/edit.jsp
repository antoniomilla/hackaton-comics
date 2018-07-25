<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="comicCharacter/edit.do" modelAttribute="comicCharacter">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="comicComicCharacter"/>
	<form:hidden path="otherAliases"/>
	<form:hidden path="comments"/>
	

	<form:label path="name">
		<spring:message code="comicCharacter.name" />:
	</form:label>
	<form:input path="name" />
	<form:errors cssClass="error" path="name" />
	<br />

	<form:label path="alias">
		<spring:message code="comicCharacter.alias" />:
	</form:label>
	<form:input path="alias" />
	<form:errors cssClass="error" path="alias" />
	<br />
	
	<form:label path="description">
		<spring:message code="comicCharacter.description" />:
	</form:label>
	<form:textarea path="description" />
	<form:errors cssClass="error" path="description" />
	<br />
	
	<form:label path="city">
		<spring:message code="comicCharacter.city" />:
	</form:label>
	<form:input path="city" />
	<form:errors cssClass="error" path="city" />
	<br />
	
	<form:label path="firstAppareance">
		<spring:message code="comicCharacter.firstAppareance" />:
	</form:label>
	<form:input path="firstAppareance" />
	<form:errors cssClass="error" path="firstAppareance" />
	<br />

	<form:label path="publisher">
		<spring:message code="comicCharacter.publisher" />:
	</form:label>
	<form:select id="publishers" path="publisher">
		<form:option value="0" label="----" />		
		<form:options items="${publishers}" itemValue="id" itemLabel="name" />
	</form:select>
	<form:errors cssClass="error" path="publisher" />
	<br />

	<form:label path="image">
		<spring:message code="comicCharacter.image" />:
	</form:label>
	<form:input path="image" />
	<form:errors cssClass="error" path="image" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="comicCharacter.save" />" />&nbsp; 
	<jstl:if test="${comicCharacter.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="comicCharacter.delete" />"
			onclick="return confirm('<spring:message code="comicCharacter.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="comicCharacter.cancel" />"
		onclick="javascript: relativeRedir('comicCharacter/list.do');" />
	<br />


</form:form>