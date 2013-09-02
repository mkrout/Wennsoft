package com.wennsoft.portlet;

import com.wennsoft.repository.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.portlet.*;
import org.exoplatform.portal.webui.util.*;
import org.json.*;
import org.w3c.dom.*;

public class ConnectSettings extends GenericPortlet
{
	private static final Logger LOGGER = Logger.getLogger("InteractionManager");
	
    @Override
    public void doHeaders(RenderRequest request, RenderResponse response)
    {
    	Map<String, Object> connectSettings = null;
        try
        {
        	connectSettings = ConnectRepository.INSTANCE.loadConnectSettings("customer-connect");
        	if (connectSettings == null)
        	{
        		request.setAttribute("error", "Connect settings not found");
        	}
        }
        catch (Throwable throwable)
        {
        	// log? 
        }
    	
        Element element = response.createElement("link");
        element.setAttribute("type", "text/css");
        element.setAttribute("rel", "stylesheet");
        element.setAttribute("href", request.getContextPath() + "/service/resource/customer-connect/themes/stylesheet.css");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        String theme = (String)connectSettings.get("Theme");
        if (theme == null)
        {
            theme = "default";
        }
        element = response.createElement("link");
        element.setAttribute("type", "text/css");
        element.setAttribute("rel", "stylesheet");
        element.setAttribute("href", request.getContextPath() + String.format("/service/resource/customer-connect/themes/%s/stylesheet.css", theme));
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/spin-1.3-custom.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery-2.0.3-min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.jqueryui-1.10.3-min.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.jqueryui.combobox-custom.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.jqueryui.spinner-custom.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/jquery.jqueryui.customizations-custom.js");
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
        element.setAttribute("src", request.getContextPath() + String.format("/service/resource/customer-connect/localization/globalize.culture.%s.js",
                                                                             Util.getPortalRequestContext().getLocale().toString().toLowerCase()));
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

        element = response.createElement("script");
        element.setAttribute("type", "text/javascript");
        element.setAttribute("language", "javascript");
        element.setAttribute("src", request.getContextPath() + "/js/connect-settings/view.js");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);
    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException
    {
        try
        {
            Map<String, Object> connectSettings = ConnectRepository.INSTANCE.loadConnectSettings("customer-connect");
            if (connectSettings == null)
            {
                request.setAttribute("error", "Connect settings not found");
            }

            JSONArray availableDisplaysJsonArray = new JSONArray();
            for (DataDisplay.Display display : DataDisplay.Display.values())
            {
                availableDisplaysJsonArray.put(new JSONObject(display));
            }

            JSONArray pageAccessJsonArray = new JSONArray();
            List<Map<String, Object>> pageAccess = new ArrayList<Map<String, Object>>();
            if (connectSettings.containsKey("PageAccess") && connectSettings.get("PageAccess") != null)
            {
                pageAccess = (List<Map<String, Object>>)connectSettings.get("PageAccess");
            }
            for (Map<String, Object> pageAccessItem : pageAccess)
            {
                JSONObject pageAccessJsonObject = new JSONObject();
                pageAccessJsonObject.put("Entity", pageAccessItem.get("Entity"));
                pageAccessJsonObject.put("Display", pageAccessItem.get("Display"));
                pageAccessJsonObject.put("Page", pageAccessItem.get("Page"));
                pageAccessJsonObject.put("Enabled", pageAccessItem.get("Enabled"));
                pageAccessJsonArray.put(pageAccessJsonObject);
            }

            List<Map<String, String>> availableThemes = ConnectRepository.INSTANCE.loadResourceDirectory("customer-connect/themes");

            request.setAttribute("windowId", request.getWindowID());
            request.setAttribute("exoLocale", Util.getPortalRequestContext().getLocale().toString().toLowerCase());
            request.setAttribute("requestContextPath", request.getContextPath());
            request.setAttribute("connectSettings", connectSettings);
            request.setAttribute("availableDisplaysJson", availableDisplaysJsonArray);
            request.setAttribute("pageAccessJson", pageAccessJsonArray.toString());
            request.setAttribute("availableThemes", availableThemes);
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            request.setAttribute("error", throwable.getMessage());
        }

        if (request.getAttribute("error") != null)
        {
        	LOGGER.log(Level.SEVERE, request.getAttribute("error").toString());
            getPortletContext().getRequestDispatcher("/jsp/error.jsp").include(request, response);
        }
        else
        {
            getPortletContext().getRequestDispatcher("/jsp/connect-settings/view.jsp").include(request, response);
        }
    }

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException
    {
        try
        {
            Map<String, Object> connectSettings = new HashMap<String, Object>();

            connectSettings.put("WebServiceUri", request.getParameter("webServiceUri"));
            connectSettings.put("TermsAndConditions", Boolean.parseBoolean(request.getParameter("termsAndConditions")));
            if (request.getParameter("historyLimit") != null && !request.getParameter("historyLimit").isEmpty())
            {
                connectSettings.put("HistoryLimit", Integer.parseInt(request.getParameter("historyLimit")));
            }

            List<Map<String, Object>> pageAccess = new ArrayList<Map<String, Object>>();
            JSONArray pageAccessJsonArray = new JSONArray(request.getParameter("pageAccess"));
            for (int pageAccessIndex = 0; pageAccessIndex < pageAccessJsonArray.length(); pageAccessIndex++)
            {
                Map<String, Object> pageAccessItem = new HashMap<String, Object>();
                JSONObject pageAccessJsonObject = pageAccessJsonArray.getJSONObject(pageAccessIndex);
                pageAccessItem.put("Entity", pageAccessJsonObject.get("Entity"));
                pageAccessItem.put("Display", pageAccessJsonObject.get("Display"));
                pageAccessItem.put("Page", pageAccessJsonObject.get("Page"));
                pageAccessItem.put("Enabled", pageAccessJsonObject.getBoolean("Enabled"));
                pageAccess.add(pageAccessItem);
            }
            connectSettings.put("PageAccess", pageAccess);

            connectSettings.put("Theme", request.getParameter("theme"));

            ConnectRepository.INSTANCE.saveConnectSettings("customer-connect", connectSettings);
            DataDisplay.clearRenderCache();

            SignatureRepository.INSTANCE.updateData(request.getParameter("webServiceUri"), "LoggingLevel", request.getParameter("webServiceLoggingLevel"), new byte[0]);
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
        }
    }
}
