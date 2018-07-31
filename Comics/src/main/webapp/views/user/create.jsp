<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<form:form action="${requestURI}" modelAttribute="user">	

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="creationTime"/>
	
	<form:label path="nickname">
	<spring:message code="user.nickname" />:
	</form:label> 
	<form:input path="nickname"/>
	<form:errors cssClass="error" path="nickname"/>
	
	<br><br>
	<form:label path="description">
	<spring:message code="user.description" />:
	</form:label> 
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	
	<form:label path="userAccount.username">
	<spring:message code="user.account" />:
	</form:label> 
	<form:input path="userAccount.username"/>
	<form:errors cssClass="error" path="userAccount.username"/>
	
	<br><br>
	
	<form:label path="userAccount.password">
	
	</form:label> 
	<form:password path="userAccount.password"/>
	<form:errors cssClass="error" path="userAccount.password"/>
	<spring:message code="user.password" />:<br>
	<spring:message code="user.onlyFriends" />:
	<form:radiobutton path="onlyFriendsCanSendDms" value="True"/><spring:message code="user.True"/>
	<form:radiobutton path="onlyFriendsCanSendDms" value="False"/><spring:message code="user.False"/>

	<form:hidden path="userAccount.authorities"/>
	
	
	<form:hidden path="level"/>
	<form:hidden path="userComics"/>
	<form:hidden path="blocked"/>
	<form:hidden path="blockReason"/>
	<form:hidden path="trusted"/>
	<form:hidden path="lastLevelUpdateDate"/>
	
	<form:hidden path="friends"/>
	<form:hidden path="messageFolders"/>
	<form:hidden path="sent"/>
	<form:hidden path="receipt"/>
	<form:hidden path="userComments"/>
	<form:hidden path="userVolumes"/>
	
	<br><br>
	<input type="submit" name="save"
		value="<spring:message code="user.create" />" />&nbsp; 
	<input type="button" name="cancel"
		value="<spring:message code="user.cancel" />"
		onclick="javascript: relativeRedir('cliente/list.do');" />
	<br />
	</form:form>