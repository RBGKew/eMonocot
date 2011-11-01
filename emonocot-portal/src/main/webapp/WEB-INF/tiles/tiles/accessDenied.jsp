<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<div class="row">
		<div class="twelvecol">
			<div class="content-wrapper">

				<div class="row">
					<div class="tencol">
						<h2>
							<spring:message code="accessDenied" />
						</h2>
						<p>${SPRING_SECURITY_403_EXCEPTION.message}</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</jsp:root>