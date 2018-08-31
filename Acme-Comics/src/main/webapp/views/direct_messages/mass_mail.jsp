<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="appfn" uri="/WEB-INF/appfn.tld" %>

<form:form action="${formAction}" modelAttribute="form">
    <app:preserve-return-action />

    <app:select path="type" items="${types}" code="direct_messages.massMailType" itemCodePrefix="direct_messages.massMailType" />

    <app:textbox path="subject" code="direct_messages.subject" />
	<app:textarea path="body" code="direct_messages.body" />

	<app:checkbox path="administrationNotice" code="direct_messages.administrationNotice" />

	<div>
        <app:submit />
        <app:cancel-button />
    </div>
</form:form>
