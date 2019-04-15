<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:showtext fieldset="true" code="parade.title" value="${p.title }"/>
<acme:showtext fieldset="true" code="parade.description" value="${p.description}"/>
<fieldset><legend><b><spring:message code="parade.floats"/></b></legend>
<jstl:forEach items="${p.floats}" var="f">
	<jstl:out value="${f.title}"/>
	<br>
</jstl:forEach>
</fieldset>
<acme:showtext fieldset="true" code="parade.maxRow" value="${p.maxRow}"/>
<acme:showtext fieldset="true" code="parade.maxColumn" value="${p.maxColumn}"/>
<img src="${sponsorshipBanner}"/>
<security:authorize access="hasRole('BROTHERHOOD')">

<a href="segment/brotherhood/list.do?paradeId=${p.id}">
			<spring:message code="parade.path"/>
		</a>
</security:authorize>
