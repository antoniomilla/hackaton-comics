<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<p><spring:message code="comment.user"/>: <jstl:out value="${comment.user.nickname }"/></p>
<p><spring:message code="comment.text"/>: <jstl:out value="${comment.text }"/></p>
<p><spring:message code="comment.creationTime"/>: <jstl:out value="${comment.creationTime }"/></p>
<jstl:if test="${comment.comic!=null }">
	<p><spring:message code="comment.comic"/>: <a href="comic/display.do?comicId=${comment.comic.id }"><jstl:out value="${comment.comic.name }"/></a></p>
</jstl:if>
<jstl:if test="${comment.publisher!=null }">
	<p><spring:message code="comment.publisher"/>: <a href="publisher/display.do?publisherId=${comment.publisher.id }"><jstl:out value="${comment.publisher.name }"/></a></p>
</jstl:if>
<jstl:if test="${comment.author!=null }">
	<p><spring:message code="comment.author"/>: <a href="author/display.do?authorId=${comment.author.id }"><jstl:out value="${comment.author.name }"/></a></p>
</jstl:if>
<jstl:if test="${comment.comicCharacter!=null }">
	<p><spring:message code="comment.comicCharacter"/>: <a href="comicCharacter/display.do?comicCharacterId=${comment.comicCharacter.id }"><jstl:out value="${comment.comicCharacter.name }"/></a></p>
</jstl:if>
<jstl:if test="${comment.volume!=null }">
	<p><spring:message code="comment.volume"/>: <a href="volume/display.do?volumeId=${comment.volume.id }"><jstl:out value="${comment.volume.name }"/></a></p>
</jstl:if>