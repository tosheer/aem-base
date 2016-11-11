<%@include file="/libs/foundation/global.jsp"%>
<%@page import="com.day.cq.wcm.foundation.forms.FormsHelper,
    net.codealchemists.aem.forms.constants.ActionHandlerConstants"%>
<%
    String path = resource.getPath()
            + ActionHandlerConstants.FORM_ACTION_HANDLER_SELECTOR + ActionHandlerConstants.HTML_EXTENSION;
    slingRequest.setAttribute(ActionHandlerConstants.SUBMIT_ACTION_NAME, slingRequest.getResource().getValueMap().get(
            ActionHandlerConstants.SUBMIT_ACTION_NAME, ""));
    FormsHelper.setForwardPath(slingRequest, path);
%>