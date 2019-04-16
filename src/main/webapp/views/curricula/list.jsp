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

<security:authorize access="hasRole('HACKER')">
    <display:table name="curriculas" id="row" requestURI="${RequestURI}"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.id" var="subjectHeader" />
        <display:column property="id" title="${subjectHeader}" />


        <!-- Display -->
        <display:column>
            <a
                    href="curricula/hacker/display.do?curriculaId=${row.id}">
                <spring:message code="curricula.display" />
            </a>
        </display:column>

    </display:table>

    <acme:cancel url="curricula/hacker/create.do" code="curricula.create"/>
</security:authorize>
