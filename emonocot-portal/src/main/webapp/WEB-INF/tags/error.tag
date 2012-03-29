<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<c:if test="${not empty error}">
		<div class="alert alert-error error" data-dismiss="alert">
			<a class="close" href="#">×</a>
			<p>
				<strong><spring:message code="${error.code}"	arguments="${error.arguments}" /></strong>
			</p>
    		<jsp:scriptlet>session.removeAttribute("error");</jsp:scriptlet>
		</div>
	</c:if>
</jsp:root>