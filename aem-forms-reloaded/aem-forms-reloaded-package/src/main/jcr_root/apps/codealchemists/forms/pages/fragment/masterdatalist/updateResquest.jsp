<%@include file="/libs/foundation/global.jsp"%>
<%@page import="com.day.cq.wcm.api.AuthoringUIMode"%>
<%--
    In Request we are setting the Authoring mode as Touch UI, As by default Authoring mode is set to classic UI
    when content is created in /etc hierarchy.
--%>
<%
    slingRequest.setAttribute(AuthoringUIMode.REQUEST_ATTRIBUTE_NAME, AuthoringUIMode.TOUCH);
%>