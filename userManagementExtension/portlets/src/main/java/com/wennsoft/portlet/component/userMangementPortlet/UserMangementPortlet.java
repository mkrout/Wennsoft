package com.wennsoft.portlet.component.userMangementPortlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.exoplatform.portal.webui.util.Util;
import org.w3c.dom.Element;

public class UserMangementPortlet extends GenericPortlet
{
    @Override
    public void doHeaders(RenderRequest request, RenderResponse response)
    {
        Element element = response.createElement("link");
        element.setAttribute("type", "text/css");
        element.setAttribute("rel", "stylesheet");
        element.setAttribute("href", request.getContextPath() + "/css/style.css");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery-2.0.3-min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);


        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.datatables-1.9.4-custom.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.datatables.customizations-custom.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.datatables.feature.filter-custom.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.jqueryui.datatables.rowreordering-1.0.0-custom.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.datatables.tabletools-2.1.5-min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.bpopup-0.9.4.min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/globalize-4.0.1.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.globalize.customizations-custom.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", String.format("/interaction-manager/service/resource/customer-connect/localization/globalize.culture.%s.js",
                Util.getPortalRequestContext().getLocale().toString().toLowerCase()));
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/view.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException
    {
        getPortletContext().getRequestDispatcher("/jsp/view.jsp").include(request, response);
    }
}
