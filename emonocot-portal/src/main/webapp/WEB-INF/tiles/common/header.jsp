<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:security="http://www.springframework.org/security/tags"
	version="2.0">		
	<div class="topbar">
		<div class="fill">
			<div class="container">
			    <jsp:element name="a">
			      <jsp:attribute name="class">brand</jsp:attribute>
			      <jsp:attribute name="href">
			        <c:url value="/"/>
			      </jsp:attribute>
				  eMonocot			    
			    </jsp:element>
			    <div>
			    	<ul>
			    		<li>
			    			<jsp:element  name="a">
				    			<jsp:attribute name="href"><c:url value="/"/></jsp:attribute>
				    			<spring:message code="identify.title"/>
				    		</jsp:element>
				    	</li>
			    		<li>
			    			<jsp:element  name="a">
				    			<jsp:attribute name="href"><c:url value="/"/></jsp:attribute>
				    			<spring:message code="classify.title"/>
				    		</jsp:element>
			    		</li>
			    		<li>
			    			<jsp:element  name="a">
				    			<jsp:attribute name="href"><c:url value="/search?query="/></jsp:attribute>
				    			<spring:message code="explore.title"/>
				    		</jsp:element>
				    	</li>
			    	</ul>
			    </div>
			    <div class="offset12 pull-right">
			    <ul class="nav">
				<security:authorize access="!isAuthenticated()">
				  <li><jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/register"/> 
			        </jsp:attribute>
			        <spring:message code="register"/>
			      </jsp:element>
			      </li>
			      <li><jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/login"/> 
			        </jsp:attribute>
			        <spring:message code="login"/>
			      </jsp:element>
			      </li>
			    </security:authorize>
			    <security:authorize access="isAuthenticated()">
			      <li><jsp:element  name="a">
				    <jsp:attribute name="href">
					  <c:url value="/logout"/> 
			        </jsp:attribute>
			        <spring:message code="logout"/>
			      </jsp:element>
			      </li>
			    </security:authorize>
			    </ul>
			    
			</div>
		</div>
	</div>
</div>
</jsp:root>