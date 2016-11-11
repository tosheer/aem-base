<%@page session="false"%>
<%@include file="/libs/foundation/global.jsp"%>
<%@ page import="com.day.cq.i18n.I18n,
                   com.day.cq.wcm.api.WCMMode,
                   com.day.cq.wcm.foundation.Placeholder,
                   com.day.cq.wcm.foundation.forms.FormsHelper,
				   org.apache.commons.lang.StringUtils,
                   com.day.cq.wcm.foundation.forms.LayoutHelper, org.apache.jackrabbit.util.Text,java.util.Map" %>

<div class="form_row">
   <div class="form_leftcol"></div>
   <div class="form_rightcol">
<%
    final WCMMode wcmMode = WCMMode.fromRequest(request);
    final boolean isEditMode = (wcmMode == WCMMode.EDIT) || (wcmMode == WCMMode.DESIGN);
    final boolean hasSubmit = properties.get("submit", Boolean.FALSE);


    if ( hasSubmit ) {
        final boolean isSubmittable = FormsHelper.checkRule(resource, slingRequest,
                pageContext, "submittableRule");
        if (isSubmittable || isEditMode) {
            final String name = properties.get("name", "Submit");
            final String title = FormsHelper.getTitle(resource, I18n.get(slingRequest, "Submit", "Default form end submit button text"));
            boolean clientValidation = FormsHelper.doClientValidation(slingRequest);
            out.write("<input class=\"form_button_submit\" type=\"" + (clientValidation ? "button" : "submit") + "\"");
            if (FormsHelper.isReadOnly(slingRequest)) {
                out.write(" disabled=\"disabled\"");
            }
            if ( name.length() > 0 ) {
                out.write(" name=\"");
                out.write(Text.encodeIllegalXMLCharacters(name));
                out.write("\"");
            }
            if ( title.length() > 0 ) {
                out.write(" value=\"");
                out.write(Text.encodeIllegalXMLCharacters(title));
                out.write("\"");
            }
            if (clientValidation) {
                out.write(" onclick=\"if (");
                out.write(FormsHelper.getFormsPreCheckMethodName(slingRequest));
                out.write("('");
                if ( name.length() > 0 ) {
                    out.write(name);
                }
                out.write("')) { document.forms['");
                out.write(FormsHelper.getFormId(slingRequest));
                out.write("'].submit();} else return false;\"");
            }
            if (!isSubmittable) {
                out.write(" disabled=\"\"");
            }
            out.write(">");
        }
    }
    final boolean hasReset = properties.get("reset", Boolean.FALSE);
    if ( hasReset ) {
        %>&nbsp;&nbsp;&nbsp;<%
        String resetTitle = properties.get("resetTitle", "");
        out.write("<input class=\"form_button_reset\" type=\"reset\"");
        if (FormsHelper.isReadOnly(slingRequest)) {
            out.write(" disabled=\"disabled\"");
        }
        if ( resetTitle.length() > 0 ) {
            out.write(" value=\"");
            out.write(Text.encodeIllegalXMLCharacters(resetTitle));
            out.write("\"");
        }
        out.write(">");
    }
    %></div>
  </div><%
    // draw the placeholder for UI mode touch
    if (!hasSubmit && !hasReset) {
        %><%= Placeholder.getDefaultPlaceholder(slingRequest, "Form End", "") %><%
    }

    LayoutHelper.printDescription(FormsHelper.getDescription(resource, ""), out);
    FormsHelper.endForm(slingRequest);

    // turn of decoration and close the decorating DIV
    componentContext.setDecorate(false);
    %></div><%

    // draw the edit bar
    if (editContext != null) {
        editContext.includeEpilog(slingRequest, slingResponse, wcmMode);
    }

%></form>
