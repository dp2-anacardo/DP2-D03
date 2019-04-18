<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="positions" id="row" requestURI="${requestURI}"
               pagesize="5" class="displaytag">

    <spring:message code="position.title" var="title"/>
    <display:column property="title" title="${title}"/>

    <spring:message code="position.description" var="description"/>
    <display:column property="description" title="${description}"/>

    <display:column> <a href="position/show.do?positionId=${row.id}">
        <spring:message code="position.show" /></a> </display:column>




    <security:authorize access="hasRole('HACKER')">
        <display:column> <a href="position/show.do?positionId=${row.id}">
            <spring:message code="position.application" /></a> </display:column>

    </security:authorize>


</display:table>

<security:authorize access="hasRole('COMPANY')">
    <input type="button" value="<spring:message code="position.create" />"
           onclick="javascript: relativeRedir('position/company/create.do');" />
</security:authorize>