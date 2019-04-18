<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('HACKER')">

    <h2><spring:message code="curricula.personal"/></h2>
    <display:table name="${curricula.personalData}" id="row"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.personal.name" var="personalNameHeader" />
        <display:column property="fullName" title="${personalNameHeader}" />

        <spring:message code="curricula.personal.statement" var="statementHeader" />
        <display:column property="statement" title="${statementHeader}" />

        <spring:message code="curricula.personal.phoneNumber" var="phoneNumberHeader" />
        <display:column property="phoneNumber" title="${phoneNumberHeader}" />

        <spring:message code="curricula.personal.githubProfile" var="githubHeader" />
        <display:column property="githubProfile" title="${githubHeader}" />

        <spring:message code="curricula.personal.linkedInProfile" var="linkedInProfileHeader" />
        <display:column property="linkedInProfile" title="${linkedInProfileHeader}" />

        <display:column>
            <a
                    href="personalData/hacker/edit.do?personalDataId=${row.id}">
                <spring:message code="curricula.personal.edit" />
            </a>
        </display:column>

    </display:table>


    <h2><spring:message code="curricula.educational"/></h2>
    <display:table name="${curricula.educationalData}" id="row"
                   requestURI="curricula/hacker/display.do?curriculaId=${curricula.id}"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.educational.degree" var="personalNameHeader" />
        <display:column property="degree" title="${personalNameHeader}" />

        <spring:message code="curricula.educational.institution" var="statementHeader" />
        <display:column property="institution" title="${statementHeader}" />

        <spring:message code="curricula.educational.mark" var="phoneNumberHeader" />
        <display:column property="mark" title="${phoneNumberHeader}" />

        <spring:message code="curricula.educational.startD" var="githubHeader" />
        <display:column property="startDate" title="${githubHeader}" />

        <spring:message code="curricula.educational.endD" var="linkedInProfileHeader" />
        <display:column property="endDate" title="${linkedInProfileHeader}" />

        <display:column>
            <a
                    href="educationalData/hacker/edit.do?educationalDataId=${row.id}&curriculaId=${curricula.id}">
                <spring:message code="curricula.edit" />
            </a>
        </display:column>

        <display:column>
            <a
                    href="educationalData/hacker/delete.do?educationalDataId=${row.id}">
                <spring:message code="curricula.delete" />
            </a>
        </display:column>

    </display:table>

    <acme:cancel url="educationalData/hacker/create.do?curriculaId=${curricula.id}" code="curricula.educational.create"/>



    <h2><spring:message code="curricula.position"/></h2>
    <display:table name="${curricula.positionData}" id="row"
                   requestURI="curricula/hacker/display.do?curriculaId=${curricula.id}"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.position.title" var="personalNameHeader" />
        <display:column property="title" title="${personalNameHeader}" />

        <spring:message code="curricula.position.description" var="statementHeader" />
        <display:column property="description" title="${statementHeader}" />

        <spring:message code="curricula.educational.startD" var="githubHeader" />
        <display:column property="startDate" title="${githubHeader}" />

        <spring:message code="curricula.educational.endD" var="linkedInProfileHeader" />
        <display:column property="endDate" title="${linkedInProfileHeader}" />

        <display:column>
            <a
                    href="positionData/hacker/edit.do?positionDataId=${row.id}&curriculaId=${curricula.id}">
                <spring:message code="curricula.edit" />
            </a>
        </display:column>

        <display:column>
            <a
                    href="positionData/hacker/delete.do?positionDataId=${row.id}">
                <spring:message code="curricula.delete" />
            </a>
        </display:column>

    </display:table>

    <acme:cancel url="positionData/hacker/create.do?curriculaId=${curricula.id}" code="curricula.position.create"/>

    <h2><spring:message code="curricula.misc"/></h2>
    <display:table name="${curricula.miscData}" id="row"
                   requestURI="curricula/hacker/display.do?curriculaId=${curricula.id}"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.misc.freeT" var="personalNameHeader" />
        <display:column property="freeText" title="${personalNameHeader}" />

        <spring:message code="curricula.misc.attachments" var="statementHeader" />
        <display:column title="${statementHeader}">
            <jstl:forEach items="${curricula.miscData.attachment}" var="a">
                <jstl:out value="${a.link}"></jstl:out>
            </jstl:forEach>
        </display:column>

        <display:column>
            <a
                    href="miscData/hacker/edit.do?miscDataId=${row.id}&curriculaId=${curricula.id}">
                <spring:message code="curricula.edit" />
            </a>
        </display:column>

        <display:column>
            <a
                    href="miscData/hacker/delete.do?miscDataId=${row.id}">
                <spring:message code="curricula.delete" />
            </a>
        </display:column>

    </display:table>

    <jstl:if test="${curricula.miscData == null}">
        <acme:cancel url="miscData/hacker/create.do?curriculaId=${curricula.id}" code="curricula.misc.create"/>
    </jstl:if>

</security:authorize>

<security:authorize access="hasRole('COMPANY')">

    <h2><spring:message code="curricula.personal"/></h2>
    <display:table name="${curricula.personalData}" id="row"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.personal.name" var="personalNameHeader" />
        <display:column property="fullName" title="${personalNameHeader}" />

        <spring:message code="curricula.personal.statement" var="statementHeader" />
        <display:column property="statement" title="${statementHeader}" />

        <spring:message code="curricula.personal.phoneNumber" var="phoneNumberHeader" />
        <display:column property="phoneNumber" title="${phoneNumberHeader}" />

        <spring:message code="curricula.personal.githubProfile" var="githubHeader" />
        <display:column property="githubProfile" title="${githubHeader}" />

        <spring:message code="curricula.personal.linkedInProfile" var="linkedInProfileHeader" />
        <display:column property="linkedInProfile" title="${linkedInProfileHeader}" />

    </display:table>


    <h2><spring:message code="curricula.educational"/></h2>
    <display:table name="${curricula.educationalData}" id="row"
                   requestURI="curricula/hacker/display.do?curriculaId=${curricula.id}"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.educational.degree" var="personalNameHeader" />
        <display:column property="degree" title="${personalNameHeader}" />

        <spring:message code="curricula.educational.institution" var="statementHeader" />
        <display:column property="institution" title="${statementHeader}" />

        <spring:message code="curricula.educational.mark" var="phoneNumberHeader" />
        <display:column property="mark" title="${phoneNumberHeader}" />

        <spring:message code="curricula.educational.startD" var="githubHeader" />
        <display:column property="startDate" title="${githubHeader}" />

        <spring:message code="curricula.educational.endD" var="linkedInProfileHeader" />
        <display:column property="endDate" title="${linkedInProfileHeader}" />

    </display:table>

    <h2><spring:message code="curricula.position"/></h2>
    <display:table name="${curricula.positionData}" id="row"
                   requestURI="curricula/hacker/display.do?curriculaId=${curricula.id}"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.position.title" var="personalNameHeader" />
        <display:column property="title" title="${personalNameHeader}" />

        <spring:message code="curricula.position.description" var="statementHeader" />
        <display:column property="description" title="${statementHeader}" />

        <spring:message code="curricula.educational.startD" var="githubHeader" />
        <display:column property="startDate" title="${githubHeader}" />

        <spring:message code="curricula.educational.endD" var="linkedInProfileHeader" />
        <display:column property="endDate" title="${linkedInProfileHeader}" />

    </display:table>

    <h2><spring:message code="curricula.misc"/></h2>
    <display:table name="${curricula.miscData}" id="row"
                   requestURI="curricula/hacker/display.do?curriculaId=${curricula.id}"
                   pagesize="5" class="displaytag">


        <spring:message code="curricula.misc.freeT" var="personalNameHeader" />
        <display:column property="freeText" title="${personalNameHeader}" />

        <spring:message code="curricula.misc.attachments" var="statementHeader" />
        <display:column title="${statementHeader}">
            <jstl:forEach items="${curricula.miscData.attachment}" var="a">
                <jstl:out value="${a.link}"></jstl:out>
            </jstl:forEach>
        </display:column>

    </display:table>

</security:authorize>
