package com.wennsoft.portlet;

import com.wennsoft.repository.*;
import javax.portlet.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.exoplatform.container.*;
import org.exoplatform.portal.mop.*;
import org.exoplatform.portal.mop.navigation.*;
import org.exoplatform.portal.mop.user.*;
import org.exoplatform.portal.webui.util.*;
import org.exoplatform.services.organization.*;
import org.w3c.dom.*;

public class Navigation extends GenericPortlet
{
    private static final Logger LOGGER = Logger.getLogger("InteractionManager");

    @Override
    public void doHeaders(RenderRequest request, RenderResponse response)
    {
        try
        {
            Map<String, Object> connectSettings = ConnectRepository.INSTANCE.loadConnectSettings("customer-connect");
            if (connectSettings == null)
            {
                throw new Exception("Connect settings not found");
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

            if (request.getPortletMode().equals(PortletMode.EDIT))
            {
                element = response.createElement("script");
                element.setAttribute("type", "text/javascript");
                element.setAttribute("language", "javascript");
                element.setAttribute("src", request.getContextPath() + "/js/navigation/edit.js");
                response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);
            }
            else if (request.getPortletMode().equals(PortletMode.VIEW))
            {
                element = response.createElement("script");
                element.setAttribute("type", "text/javascript");
                element.setAttribute("language", "javascript");
                element.setAttribute("src", request.getContextPath() + "/js/navigation/view.js");
                response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);
            }
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            request.setAttribute("error", throwable.getMessage());
        }
    }

    @Override
    public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException
    {
        try
        {
            request.setAttribute("windowId", request.getWindowID());

            for (Map.Entry<String, String[]> preference : request.getPreferences().getMap().entrySet())
            {
                if (preference.getValue().length == 1)
                {
                    request.setAttribute(preference.getKey(), preference.getValue()[0]);
                }
                else
                {
                    request.setAttribute(preference.getKey(), preference.getValue());
                }
            }
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            request.setAttribute("error", throwable.getMessage());
        }

        if (request.getAttribute("error") != null)
        {
            getPortletContext().getRequestDispatcher("/jsp/error.jsp").include(request, response);
        }
        else
        {
            System.out.println(request.getPreferences().getValue("userPlatformToolbar", "FFF"));
            getPortletContext().getRequestDispatcher("/jsp/navigation/edit.jsp").include(request, response);
        }
    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException
    {
        try
        {
            OrganizationService service = (OrganizationService)PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
            boolean administrator = service.getMembershipHandler().findMembershipsByUserAndGroup(request.getRemoteUser(), "/platform/administrators").size() > 0;

            UserNode rootUserNode = Util.getPortalRequestContext()
                                        .getUserPortalConfig()
                                        .getUserPortal()
                                        .getNode(Util.getPortalRequestContext()
                                                     .getUserPortalConfig()
                                                     .getUserPortal()
                                                     .getNavigation(Util.getPortalRequestContext().getSiteKey()),
                                                 Scope.ALL,
                                                 UserNodeFilterConfig.builder().withVisibility(Visibility.DISPLAYED).build(),
                                                 null);

            request.setAttribute("administrator", administrator);
            request.setAttribute("pages", processUserNode(rootUserNode));
            request.setAttribute("portalUri", String.format("http://%s:%s%s",
                                                            request.getServerName(),
                                                            request.getServerPort(),
                                                            Util.getPortalRequestContext().getPortalURI()));
            request.setAttribute("remoteUser", request.getRemoteUser());
            request.setAttribute("windowId", request.getWindowID());

            for (Map.Entry<String, String[]> preference : request.getPreferences().getMap().entrySet())
            {
                if (preference.getValue().length == 1)
                {
                    request.setAttribute(preference.getKey(), preference.getValue()[0]);
                }
                else
                {
                    request.setAttribute(preference.getKey(), preference.getValue());
                }
            }
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            request.setAttribute("error", throwable.getMessage());
        }

        if (request.getAttribute("error") != null)
        {
            getPortletContext().getRequestDispatcher("/jsp/error.jsp").include(request, response);
        }
        else
        {
            getPortletContext().getRequestDispatcher("/jsp/navigation/view.jsp").include(request, response);
        }
    }

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException
    {
        try
        {
            request.getPreferences().setValue("connect", request.getParameter("connect"));
            request.getPreferences().setValue("formatContainers", new Boolean(request.getParameter("formatContainers") != null).toString());
            request.getPreferences().setValue("formatNavigation", new Boolean(request.getParameter("formatNavigation") != null).toString());
            request.getPreferences().setValue("userPlatformToolbar", new Boolean(request.getParameter("userPlatformToolbar") != null).toString());
            request.getPreferences().store();
            System.out.println(request.getPreferences().getValue("userPlatformToolbar", "FFF"));
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
        }
    }

    private Map<String, Object> processUserNode(UserNode userNode)
    {
        Map<String, Object> userNodeMap = new HashMap<String, Object>();

        List<Map<String, Object>> childrenUserNodeMap = new ArrayList<Map<String, Object>>();
        if (userNode.hasChildrenRelationship())
        {
            for (UserNode childUserNode : userNode.getChildren())
            {
                childrenUserNodeMap.add(processUserNode(childUserNode));
            }
        }
        userNodeMap.put("Children", childrenUserNodeMap);
        userNodeMap.put("Label", userNode.getLabel());
        userNodeMap.put("Name", userNode.getName());
        userNodeMap.put("Uri", userNode.getPageRef() != null ? userNode.getURI() : null);

        return userNodeMap;
    }
}
