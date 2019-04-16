<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<display:table name="problem" id="row" requestURI="problem/show.do"
               class="displaytag">
    <spring:message code="problem.title" var="titleHeader"/>
    <display:column property="title" title="${titleHeader}"
                    sortable="false"/>
</display:table>

<display:table name="problem" id="row" requestURI="problem/show.do"
               class="displaytag">
    <spring:message code="problem.statement" var="statementHeader"/>
    <display:column property="statement" title="${statementHeader}"
                    sortable="false"/>
</display:table>

<display:table name="problem" id="row" requestURI="problem/show.do"
               class="displaytag">
    <spring:message code="problem.hint" var="hintHeader"/>
    <display:column property="hint" title="${hintHeader}"
                    sortable="false"/>
</display:table>

<display:table name="problem" id="row" requestURI="problem/show.do"
               class="displaytag">
    <spring:message code="problem.isFinal" var="isFinalHeader"/>
    <display:column title="${isFinalHeader}" sortable="false">
        <jstl:if test="${row.isFinal eq true}">
            <spring:message code="problem.isFinal.final"/>
        </jstl:if>
        <jstl:if test="${row.isFinal eq false}">
            <spring:message code="problem.isFinal.draft"/>
        </jstl:if>
    </display:column>
</display:table>

<security:authorize access="hasRole('COMPANY')">
    <acme:cancel url="problem/delete.do?problemID=${row.id}" code="problem.delete"/>
    <acme:cancel url="problem/company/list.do" code="problem.goBack"/>

</security:authorize>

<security:authorize access="hasRole('HACKER')">
    <acme:cancel url="problem/hacker/list.do" code="problem.goBack"/>

</security:authorize>