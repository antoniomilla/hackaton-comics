<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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

<div>
	<img src="${comic.image}" width="200" height="200"/>
</div>

<div>
	<p>
	    <spring:message code="comics.name"/>:
	    <c:if test="${userComic != null}">
	        <c:if test="${userComic.starred}">
                <app:post-link cssClass="linkButton" action="comics/unstar.do?id=${comic.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
                    <img class="iconColumnIcon iconButton" src="images/star_yes.png" />
                </app:post-link>
            </c:if>
            <c:if test="${not userComic.starred}">
                <app:post-link cssClass="linkButton" action="comics/star.do?id=${comic.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
                    <img class="iconColumnIcon iconButton" src="images/star_no.png" />
                </app:post-link>
            </c:if>
	    </c:if>
	    <c:out value="${comic.name}"/>
	</p>
	<p><spring:message code="comics.publisher"/>: <a href="publishers/show.do?id=${comic.publisher.id}"><c:out value="${comic.publisher.name}"/></a></p>
	<p><spring:message code="comics.author"/>: <a href="authors/show.do?id=${comic.author.id}"><c:out value="${comic.author.name}"/></a></p>
	<c:if test="${comic.description != null}">
	    <p><spring:message code="comics.description"/>: <c:out value="${comic.description}"/></p>
	</c:if>
	<c:if test="${not empty comic.tags}">
        <p>
            <spring:message code="comics.tags"/>:
            <c:forEach var="tag" items="${comic.tags}">
                <span class="searchTag"><c:out value="${tag}"/></span>
            </c:forEach>
        </p>
	</c:if>

	<c:if test="${userComic != null}">
        <p>
            <spring:message code="comics.user.score"/>:
            <c:forEach var="score" begin="0" end="10">
                <c:choose>
                    <c:when test="${userComic.score != null and userComic.score == score}">
                        <a href="#" onclick="return false" class="scoreButton currentScoreButton">
                            <c:out value="${score}" />
                        </a>
                    </c:when>
                    <c:otherwise>
                        <app:post-link action="comics/rate.do?id=${comic.id}&score=${score}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" cssClass="scoreButton">
                            <c:out value="${score}" />
                        </app:post-link>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${userComic.score != null}">
                <app:post-link action="comics/unrate.do?id=${comic.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" cssClass="unrateLink">
                    (<spring:message code="comics.user.unrate"/>)
                </app:post-link>
            </c:if>
        </p>
        <p>
            <spring:message code="comics.user.status"/>:
            <c:forEach var="status" items="${statuses}">
                <c:if test="${userComic.status == status}">
                    <a href="#" onclick="return false" class="statusButton currentStatusButton">
                        <spring:message code="comics.user.status.${status}"/>
                    </a>
                </c:if>
                <c:if test="${userComic.status != status}">
                    <app:post-link action="comics/set_status.do?id=${comic.id}&status=${status}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" cssClass="statusButton">
                        <spring:message code="comics.user.status.${status}"/>
                    </app:post-link>
                </c:if>
            </c:forEach>
        </p>
	</c:if>
</div>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <app:redir-button code="misc.actions.edit" action="comics/edit.do?id=${comic.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    <app:delete-button action="comics/delete.do?id=${comic.id}&returnAction=comics/list.do" />
</c:if>

<h3><spring:message code="comics.characters" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="comic.comicComicCharacters" id="comicComicCharacter" requestURI="${currentRequestUri}" sort="list">
        <display:column property="comicCharacter.alias" titleKey="comic_comic_characters.comicCharacter" sortable="true"  href="comic_characters/show.do" paramProperty="comicCharacter.id" paramId="id"/>
        <display:column property="role" titleKey="comic_comic_characters.role" />

        <c:if test="${principal != null and principal.administrator or principal.trusted}">
            <display:column titleKey="misc.actions">
                <app:redir-button code="misc.actions.edit" action="comic_comic_characters/edit.do?id=${comicComicCharacter.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                <app:delete-button code="misc.actions.remove" action="comic_comic_characters/delete.do?id=${comicComicCharacter.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'comicComicCharacter'))}" />
            </display:column>
        </c:if>
	</display:table>
</div>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <app:redir-button code="misc.actions.add" action="comic_comic_characters/new.do?comic=${comic.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
</c:if>

<h3><spring:message code="comics.volumes" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="volumes" id="volume" requestURI="${currentRequestUri}" sort="list">
	    <display:column title="#" sortable="true">
	        <c:out value="${volume_rowNum}" />
	    </display:column>
	    <display:column sortProperty="name" titleKey="volume.name" sortable="true">
	        <c:if test="${userComic != null and userComic.readVolumes.contains(volume)}">
	            <img class="iconColumnIcon" src="images/read.png" />
	        </c:if>
	        <a href="volumes/show.do?id=${volume.id}">
	            <c:out value="${volume.name}" />
	        </a>
	    </display:column>
	    <display:column property="author.name" titleKey="volume.author" href="authors/show.do" paramId="id" paramProperty="author.id" sortable="true" />
	    <display:column property="releaseDate" titleKey="volume.releaseDate" format="{0,date,dd/MM/yyyy}" sortable="true" />
	    <c:if test="${principal != null and principal.administrator or principal.trusted}">
	        <display:column property="orderNumber" titleKey="volume.orderNumber" sortable="true" />
	    </c:if>

        <security:authorize access="isAuthenticated()">
            <display:column titleKey="misc.actions">
                <c:if test="${userComic != null}">
                    <c:choose>
                        <c:when test="${userComic.readVolumes.contains(volume)}">
                            <app:post-button code="volume.unread" action="volumes/unread.do?id=${volume.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                        </c:when>
                        <c:otherwise>
                            <app:post-button code="volume.read" action="volumes/read.do?id=${volume.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${principal != null and principal.administrator or principal.trusted}">
                    <app:redir-button code="misc.actions.edit" action="volumes/edit.do?id=${volume.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                    <app:delete-button action="volumes/delete.do?id=${volume.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'volume'))}" />
                </c:if>
            </display:column>
        </security:authorize>
	</display:table>
</div>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <div>
        <app:redir-button code="misc.actions.new" action="volumes/new.do?comic=${comic.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </div>
</c:if>

<h3><spring:message code="comics.comments" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="comments" id="comment" requestURI="${currentRequestUri}" sort="list">
        <display:column property="text" titleKey="comments.text" sortable="true"/>
        <display:column property="user.nickname" titleKey="comments.user" sortable="true"/>
        <display:column property="creationTime" titleKey="comments.creationTime" sortable="true" format="{0,date,dd/MM/yyyy HH:mm:ss}"  />

        <security:authorize access="hasRole('ADMINISTRATOR')">
            <display:column titleKey="misc.actions">
                <app:delete-button action="comments/delete.do?id=${comment.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'comment'))}" />
            </display:column>
        </security:authorize>
	</display:table>
</div>

<security:authorize access="hasRole('USER')">
    <h3><spring:message code="comments.writeAComment" /></h3>
	<div>
		<form:form modelAttribute="comment" action="comments/create.do?returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'comment'))}">
		    <form:hidden path="creationTime" />
		    <form:hidden path="user" />
		    <form:hidden path="comic" />
		    <app:textarea code="comments.text" path="text" />
		    <app:submit />
		</form:form>
	</div>
</security:authorize>
