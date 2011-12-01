<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags" version="2.0">
	<style type="text/css">
      body {
        background-color: #ffffff;
      }
    </style>
    <br/>
    <div class="row">
        <div class="span-one-third">
          	<h2><spring:message code="identify.title"/></h2>
          	<p><spring:message code="identify.message"/></p>
        </div>
        <div class="span-one-third">
          	<h2><spring:message code="classify.title"/></h2>
           	<p><spring:message code="classify.message"/></p>
       	</div>
        <div class="span-one-third">
          	<h2><spring:message code="explore.title"/></h2>
          	<p><spring:message code="explore.message"/></p>
        </div>
      </div>
</jsp:root>