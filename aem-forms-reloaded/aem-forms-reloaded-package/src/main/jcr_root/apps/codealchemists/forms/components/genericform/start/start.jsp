<%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>
<%@ page import="com.day.cq.wcm.foundation.forms.ValidationInfo,
                com.day.cq.wcm.foundation.forms.FormsConstants,
                com.day.cq.wcm.foundation.forms.FormsHelper,
                org.apache.sling.api.resource.Resource,
                org.apache.sling.api.resource.ResourceUtil,
                org.apache.sling.api.resource.ValueMap,
                org.apache.commons.lang3.StringUtils,
                org.apache.sling.scripting.jsp.util.JspSlingHttpServletResponseWrapper, com.day.cq.wcm.foundation.Placeholder,
                net.codealchemists.aem.forms.util.FormsResponseHelper"%>
<cq:setContentBundle/>
<cq:include script="abacus.jsp"/><%
   slingRequest.setAttribute("loadActionName", properties.get("loadActionName", ""));
    if(StringUtils.isNotEmpty((String)slingRequest.getAttribute("loadActionName")))
    {%>
        <cq:include script="load.html"/>
    <%}
   com.day.cq.wcm.foundation.forms.FormsHelper.startForm(slingRequest, new JspSlingHttpServletResponseWrapper(pageContext));
   pageContext.setAttribute("DISABLE_CSV", StringUtils.isNotBlank(request.getParameter("dcsv")));
   // we create the form div our selfs, and turn decoration on again.
%>
<c:set var="ajaxCall" value="" />
<c:if test="${properties.ajaxCall}">
   <c:set var="ajaxCall" value="data-submit-type=\"ajaxCall\""/>
</c:if>
<%
if(StringUtils.endsWithIgnoreCase(com.day.cq.wcm.foundation.forms.FormsHelper.getFormId(slingRequest), request.getParameter(":formid")))
{%>
<script type="text/json" class="base-forms-response">
    <%
    FormsResponseHelper.buildFormSubmitJsonResponse(slingRequest, new JspSlingHttpServletResponseWrapper(pageContext));%>
</script>
<%}%>
<div class="form base-forms-submit-type" ${ajaxCall}><%
%><%= Placeholder.getDefaultPlaceholder(slingRequest, "Form Start", "") %><%
   componentContext.setDecorate(true);
%>