<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:security="http://www.springframework.org/security/tags"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="content">
		<div class="page-header">
		  <h2 id="page-title">${group.name}</h2>
        </div>
		<div class="row">
		  <div>	
			<h2><spring:message code="group.members"/></h2>
			<ul>
			  <c:forEach var="member" items="${group.members}">
			    <li>
			      <jsp:element name="a">
		          <jsp:attribute name="href">
		            <c:url value="/user/${member.identifier}"/>
		          </jsp:attribute>
		          ${member.identifier}
		        </jsp:element>
		      </li>
			  </c:forEach>
			</ul>
	      </div>
	      <div>	
			<h2><spring:message code="group.permissions"/></h2>
			<ul>
			  <c:forEach var="permission" items="${group.permissions}">
			    <li><spring:message code="${permission}"/></li>
			  </c:forEach>
			</ul>
	      </div>
	      <div>	
			<h2><spring:message code="group.aces"/></h2>
			<ul>
			  <c:forEach var="row" items="${aces}">
			    <c:set var="ace" value="${row[1]}"/>
			    <c:set var="object" value="${row[0]}"/>
			    <li>${object.identifier} <spring:message code="${em:convert(ace.permission)}"/></li>
			  </c:forEach>
			</ul>
	      </div>
	      <security:authorize url="hasRole('PERMISSION_WRITE_GROUP')">
		    <div>
		      <jsp:element name="a">
		        <jsp:attribute name="href">
		          <c:url value="/group/${group.identifier}">
		            <c:param name="form">true</c:param>
		          </c:url>
		        </jsp:attribute>
		        <spring:message code="edit.group"/>
		      </jsp:element>
		    </div>
		  </security:authorize>
		</div>		
	</div>
</jsp:root>