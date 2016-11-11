<%@include file="/libs/foundation/global.jsp"%>
<c:set var="completeFormPath">
    <cq:text property="completeFormPath" escapeXml="true"
        placeholder="" default="" />
</c:set>
<%@page import="com.day.cq.wcm.api.components.ComponentContext"%>
<%
    slingRequest.setAttribute(ComponentContext.BYPASS_COMPONENT_HANDLING_ON_INCLUDE_ATTRIBUTE, true);
%>
<sling:include path="${completeFormPath}"/>
<%
    slingRequest.removeAttribute(ComponentContext.BYPASS_COMPONENT_HANDLING_ON_INCLUDE_ATTRIBUTE);
%>