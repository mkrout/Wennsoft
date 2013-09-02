package com.wennsoft.portlet;

import com.wennsoft.repository.*;
import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.concurrent.*;
import java.util.logging.*;
import javax.portlet.*;
import javax.xml.parsers.*;

import com.wennsoft.repository.SignatureRepository;
import org.exoplatform.portal.webui.util.*;
import org.json.*;
import org.w3c.dom.*;

public class DataDisplay extends GenericPortlet
{
    private static final Map<String, Map<String, Object>> RENDER_CACHE = new ConcurrentHashMap<String, Map<String, Object>>();
    private static final Logger LOGGER = Logger.getLogger("InteractionManager");

    public static int getRenderCacheCount() throws Throwable
    {
        return RENDER_CACHE.size();
    }

    public static int getRenderCacheSize() throws Throwable
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(RENDER_CACHE);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.size();
    }

    public static void clearRenderCache()
    {
        RENDER_CACHE.clear();
    }

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
            element.setAttribute("src", request.getContextPath() + "/js/jquery.maskedinput-1.3.1-custom.js");
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
            element.setAttribute("src", request.getContextPath() + "/js/jquery.datatables.feature.filter-custom.js");
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

            element = response.createElement("script");
            element.setAttribute("type", "text/javascript");
            element.setAttribute("language", "javascript");
            element.setAttribute("src", request.getContextPath() + "/js/jquery.datatables.tabletools-2.1.5-min.js");
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

            element = response.createElement("script");
            element.setAttribute("type", "text/javascript");
            element.setAttribute("language", "javascript");
            element.setAttribute("src", request.getContextPath() + "/js/jquery.datatables.rowreordering-1.0.0-custom.js");
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

            element = response.createElement("script");
            element.setAttribute("type", "text/javascript");
            element.setAttribute("language", "javascript");
            element.setAttribute("src", request.getContextPath() + "/js/jquery.jqueryui.timepicker-1.3.1.js");
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

            element = response.createElement("script");
            element.setAttribute("type", "text/javascript");
            element.setAttribute("language", "javascript");
            element.setAttribute("src", request.getContextPath() + "/js/jquery.jqueryui.datetimepicker-custom.js");
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
                element.setAttribute("src", request.getContextPath() + "/js/data-display/edit.js");
                response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);
            }
            else if (request.getPortletMode().equals(PortletMode.VIEW))
            {
                switch (Display.fromName(request.getPreferences().getValue("display", null)))
                {
                    case DETAIL:
                    {
                        element = response.createElement("script");
                        element.setAttribute("type", "text/javascript");
                        element.setAttribute("language", "javascript");
                        element.setAttribute("src", request.getContextPath() + "/js/data-display/view-detail.js");
                        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

                        break;
                    }
                    case LIST:
                    {
                        element = response.createElement("script");
                        element.setAttribute("type", "text/javascript");
                        element.setAttribute("language", "javascript");
                        element.setAttribute("src", request.getContextPath() + "/js/data-display/view-list.js");
                        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);

                        break;
                    }
                }
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
            Map<String, Object> connectSettings = ConnectRepository.INSTANCE.loadConnectSettings("customer-connect");
            if (connectSettings == null)
            {
                throw new Exception("Connect settings not found");
            }

            List<Map<String, Object>> entitiesMetadata = (List<Map<String, Object>>)(SignatureRepository.INSTANCE.parseMetadata((String)connectSettings.get("WebServiceUri"), null)).get("Interface");
            if (entitiesMetadata == null || entitiesMetadata.isEmpty())
            {
                throw new Exception("Entities metadata not found");
            }

            request.setAttribute("windowId", request.getWindowID());
            request.setAttribute("exoLocale", Util.getPortalRequestContext().getLocale().toString().toLowerCase());
            request.setAttribute("requestContextPath", request.getContextPath());
            request.setAttribute("availableDisplays", new ArrayList<Display>(Arrays.asList(Display.values())));

            for (Entry<String, String[]> preference : request.getPreferences().getMap().entrySet())
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

            request.setAttribute("entitiesMetadata", entitiesMetadata);
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            request.setAttribute("error", throwable.getMessage());
        }

        if (request.getAttribute("error") != null)
        {
            getPortletContext().getRequestDispatcher(Display.NONE.getJspPage()).include(request, response);
        }
        else
        {
            getPortletContext().getRequestDispatcher(Display.EDIT.getJspPage()).include(request, response);
        }
    }
    
    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException
    {
        String connect = request.getPreferences().getValue("connect", null);
        String entity = request.getPreferences().getValue("entity", null);
        Display display = Display.fromName(request.getPreferences().getValue("display", null));
        String filter = request.getPreferences().getValue("filter", null);
        String fields = request.getPreferences().getValue("fields", null);

        if (entity != null && !entity.isEmpty() &&
            display != Display.NONE &&
            fields != null && !fields.isEmpty())
        {
            try
            {
                Map<String, Object> connectSettings = ConnectRepository.INSTANCE.loadConnectSettings("customer-connect");
                if (connectSettings == null)
                {
                    throw new Exception("Connect settings not found");
                }

                String renderCacheKey = connect + " | " + entity + " | " + display + " | " + fields;
                Map<String, Object> renderParameters = null;
                if (RENDER_CACHE.containsKey(renderCacheKey))
                {
                    renderParameters = RENDER_CACHE.get(renderCacheKey);
                }
                else
                {
                    renderParameters = new HashMap<String, Object>();

                    String entityId = Util.getPortalRequestContext().getRequestParameter(entity);

                    Document fieldsDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(fields.getBytes()));
                    NodeList fieldsNodeList = fieldsDocument.getElementsByTagName("field");

                    String controller = null;
                    Boolean historyEntity = false;
                    List<Map<String, Object>> controllersMetadata = (List<Map<String, Object>>)(SignatureRepository.INSTANCE.parseMetadata((String)connectSettings.get("WebServiceUri"), null)).get("Interface");
                    if (controllersMetadata == null || controllersMetadata.isEmpty())
                    {
                        throw new Exception("Controllers metadata not found");
                    }

                    Map<String, Object> entityMetadata = null;
                    for (Map<String, Object> controllerMetadata : controllersMetadata)
                    {
                        entityMetadata = (Map<String, Object>)controllerMetadata.get("Entity");
                        if (entityMetadata.get("Name").equals(entity))
                        {
                            controller = (String)controllerMetadata.get("Controller");
                            historyEntity = (Boolean)controllerMetadata.get("UsesHistory");
                            break;
                        }
                    }
                    if (controller == null || controller.isEmpty() || entityMetadata == null)
                    {
                        throw new Exception("Entity metadata not found");
                    }

                    List<Map<String, Object>> fieldsMetadata = (List<Map<String, Object>>)(SignatureRepository.INSTANCE.parseMetadata((String)connectSettings.get("WebServiceUri"), controller)).get("Fields");
                    if (fieldsMetadata == null || fieldsMetadata.isEmpty())
                    {
                        throw new Exception("Fields metadata not found");
                    }

                    List<Map<String, Object>> renderFieldsMetadata = new ArrayList<Map<String, Object>>();
                    for (int fieldsNodeIndex = 0; fieldsNodeIndex < fieldsNodeList.getLength(); fieldsNodeIndex++)
                    {
                        Map<String, Object> renderFieldMetadata = null;

                        for (Map<String, Object> fieldMetadata : fieldsMetadata)
                        {
                            NodeList fieldNodeList = fieldsNodeList.item(fieldsNodeIndex).getChildNodes();
                            for (int fieldNodeIndex = 0; fieldNodeIndex < fieldNodeList.getLength(); fieldNodeIndex++)
                            {
                                Node fieldNode = fieldNodeList.item(fieldNodeIndex);
                                if (fieldNode.getNodeName().equals("name"))
                                {
                                    if (fieldNode.getTextContent().equals(fieldMetadata.get("Name")) && !renderFieldsMetadata.contains(renderFieldMetadata))
                                    {
                                        renderFieldMetadata = fieldMetadata;
                                    }
                                }
                                else if (renderFieldMetadata != null && fieldNode.getNodeName().equals("sort"))
                                {
                                    renderFieldMetadata.put("Sort", fieldNode.getTextContent());
                                }
                            }
                        }

                        if (renderFieldMetadata != null)
                        {
                            if (renderFieldMetadata.containsKey("LookupFor") && renderFieldMetadata.get("LookupFor") != null)
                            {
                                for (Map<String, Object> pageAccess : (List<Map<String, Object>>)connectSettings.get("PageAccess"))
                                {
                                    if (pageAccess.get("Entity").equals(renderFieldMetadata.get("LookupFor")) && (Boolean)pageAccess.get("Enabled"))
                                    {
                                        renderFieldMetadata.put((String)pageAccess.get("Display"), pageAccess.get("Page"));
                                    }
                                }
                            }

                            renderFieldsMetadata.add(renderFieldMetadata);
                        }
                    }

                    String filterEntity = null;
                    if (display.equals(Display.LIST) && filter != null && !filter.isEmpty())
                    {
                        filterEntity = entity;
                        for (String filterField : filter.split("\\."))
                        {
                            List<Map<String, Object>> filterFieldsMetadata = (List<Map<String, Object>>)(SignatureRepository.INSTANCE.parseMetadata((String)connectSettings.get("WebServiceUri"), controller)).get("Fields");
                            for (Map<String, Object> filterFieldMetadata : filterFieldsMetadata)
                            {
                                if (filterFieldMetadata.get("Name").equals(filterField))
                                {
                                    if (filterFieldMetadata.get("LookupFor") != null)
                                    {
                                        filterEntity = (String)filterFieldMetadata.get("LookupFor");
                                    }
                                }
                            }
                        }
                    }

                    renderParameters.put("Controller", controller);
                    renderParameters.put("RenderFieldsMetadata", renderFieldsMetadata);
                    renderParameters.put("FilterEntity", filterEntity);
                    renderParameters.put("HistoryLimit", historyEntity ? connectSettings.get("HistoryLimit") : -1);
                    RENDER_CACHE.put(renderCacheKey, renderParameters);
                }

                request.setAttribute("windowId", request.getWindowID());
                request.setAttribute("exoLocale", Util.getPortalRequestContext().getLocale().toString());
                request.setAttribute("portalUri", String.format("http://%s:%s%s",
                                                                request.getServerName(),
                                                                request.getServerPort(),
                                                                Util.getPortalRequestContext().getPortalURI()));
                request.setAttribute("requestContextPath", request.getContextPath());
                request.setAttribute("availableDisplays", new ArrayList<Display>(Arrays.asList(Display.values())));

                for (Entry<String, String[]> preference : request.getPreferences().getMap().entrySet())
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

                request.setAttribute("connectSettings", connectSettings);
                request.setAttribute("controller", renderParameters.get("Controller"));
                request.setAttribute("fieldsMetadata", renderParameters.get("RenderFieldsMetadata"));
                request.setAttribute("filterEntity", renderParameters.get("FilterEntity"));
                request.setAttribute("historyLimit", renderParameters.get("HistoryLimit"));
            }
            catch (Throwable throwable)
            {
                LOGGER.log(Level.SEVERE, throwable.getMessage());
                request.setAttribute("error", throwable.getMessage());
            }
        }
        else
        {
        	LOGGER.log(Level.WARNING, "Preferences misconfigured");
            request.setAttribute("error", "Preferences misconfigured");
        }

        if (request.getAttribute("error") != null)
        {
            getPortletContext().getRequestDispatcher(Display.NONE.getJspPage()).include(request, response);
        }
        else
        {
            getPortletContext().getRequestDispatcher(display.getJspPage()).include(request, response);
        }
    }
    
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException
    {
        try
        {
            String save = request.getParameter("save");
            if (save.equals("Edit"))
            {
                request.getPreferences().setValue("connect", request.getParameter("connect"));
                request.getPreferences().setValue("entity", request.getParameter("entity"));
                request.getPreferences().setValue("display", request.getParameter("display"));
                request.getPreferences().setValue("filter", request.getParameter("filter") != null ? request.getParameter("filter").trim() : null);
                request.getPreferences().setValue("pageSize", request.getParameter("pageSize"));
                request.getPreferences().setValue("exportControl", new Boolean(request.getParameter("exportControl") != null).toString());
                request.getPreferences().setValue("columns", request.getParameter("columns"));
                request.getPreferences().setValue("fields", request.getParameter("fields"));
                request.getPreferences().store();

                RENDER_CACHE.remove(request.getParameter("connect") + " | " +
                                    request.getParameter("entity") + " | " +
                                    request.getParameter("display") + " | " +
                                    request.getParameter("filter") + " | " +
                                    request.getParameter("fields"));
            }
        }
        catch (Throwable throwable)
        {
        	LOGGER.log(Level.SEVERE, throwable.getMessage());
        }
    }
    
    public enum Display
    {
        LIST("/jsp/data-display/view-list.jsp", "List", true),
        DETAIL("/jsp/data-display/view-detail.jsp", "Detail", true),
        EDIT("/jsp/data-display/edit.jsp", "Edit", false),
        NONE("/jsp/error.jsp", "None", false);
        
        private String jspPage;
        private String name;
        private boolean visible;
        
        private Display(String jspPage, String name, boolean visible)
        {
            this.jspPage = jspPage;
            this.name = name;
            this.visible = visible;
        }

        public static Display fromName(String name)
        {
            Display display = Display.NONE;

            if (name != null)
            {
                for (Display displayValue : Display.values())
                {
                    if (name.equals(displayValue.getName()))
                    {
                        display = displayValue;
                    }
                }
            }

            return display;
        }
        
        public String getJspPage() { return jspPage; }
        public String getName() { return name; }
        public boolean getVisible() { return visible; }
    }
}
