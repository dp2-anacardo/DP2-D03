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

<acme:showtext fieldset="true" code="application.submitMoment" value="${application.submitMoment}"/>
<acme:showtext fieldset="true" code="parade.description" value="${p.description}"/>
<acme:showtext fieldset="true" code="parade.maxRow" value="${p.maxRow}"/>
<acme:showtext fieldset="true" code="parade.maxColumn" value="${p.maxColumn}"/>
<img src="${sponsorshipBanner}"/>
<security:authorize access="hasRole('BROTHERHOOD')">

<a href="segment/brotherhood/list.do?paradeId=${p.id}">
			<spring:message code="parade.path"/>
		</a>
</security:authorize>
