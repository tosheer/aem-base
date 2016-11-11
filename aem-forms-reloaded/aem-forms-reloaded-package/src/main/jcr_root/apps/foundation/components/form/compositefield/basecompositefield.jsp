<%@include file="/libs/foundation/global.jsp"%>
<c:set var="compositePath">
    <cq:text property="compositePath" escapeXml="true"
        placeholder="" default="" />
</c:set>
<%@page import="com.day.cq.wcm.api.components.ComponentContext"%>
<%
    slingRequest.setAttribute(ComponentContext.BYPASS_COMPONENT_HANDLING_ON_INCLUDE_ATTRIBUTE, true);
%>
<sling:include path="${compositePath}"/>
<%
    slingRequest.removeAttribute(ComponentContext.BYPASS_COMPONENT_HANDLING_ON_INCLUDE_ATTRIBUTE);
%>