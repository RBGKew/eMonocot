<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:em="http://e-monocot.org/portal/functions"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">

	<div class="content">
		<div class="page-header">
			<h2 id="page-title">${source.identifier}</h2>
			
			<jsp:element name="a">
					<jsp:attribute name="href">
						<c:url value="/admin/source/${source.identifier}" />
					</jsp:attribute>
					<spring:message code="administrator.page"/>
				</jsp:element>
		</div>
		<div class="row">
			<div class="content-wrapper">
			<br/>
			<a href="${source.uri}">
				<spring:message code="availableAt" />
			</a>
			</div>
			<br/>
			
		</div>
	</div>
	
</jsp:root>