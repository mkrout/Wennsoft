package com.wennsoft.portlet;

import com.wennsoft.repository.*;
import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.concurrent.*;
import java.util.logging.*;
import javax.portlet.*;
import javax.xml.parsers.*;
import org.exoplatform.portal.webui.util.*;
import org.json.*;
import org.w3c.dom.*;

public class Request extends GenericPortlet
{
    private static final Map<String, Map<String, Object>> METADATA_RENDER_CACHE = new ConcurrentHashMap<String, Map<String, Object>>();
    private static final Logger LOGGER = Logger.getLogger("InteractionManager");

    public static void clearRenderCache()
    {
        METADATA_RENDER_CACHE.clear();
    }

    @Override
    public void doHeaders(RenderRequest request, RenderResponse response)
    {
        //response.getCacheControl().setPublicScope(true);
        //response.getCacheControl().setUseCachedContent(true);
    	
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

        if (request.getPortletMode().toString().equalsIgnoreCase("edit"))
        {
            element = response.createElement("script");
            element.setAttribute("type", "text/javascript");
            element.setAttribute("language", "javascript");
            element.setAttribute("src", request.getContextPath() + "/js/request/edit.js");
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);
        }
        else if (request.getPortletMode().toString().equalsIgnoreCase("view"))
        {

            element = response.createElement("script");
            element.setAttribute("type", "text/javascript");
            element.setAttribute("language", "javascript");
            element.setAttribute("src", request.getContextPath() + "/js/request/view-detail.js");
            response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, element);



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

            List<Map<String, Object>> entitiesMetadata = (List<Map<String, Object>>)(SignatureRepository.INSTANCE.parseMetadata(
                    (String) connectSettings.get("WebServiceUri"),
                    null)).get("Interface");
            if (entitiesMetadata == null || entitiesMetadata.isEmpty())
            {
                throw new Exception("Entities metadata not found");
            }

            request.setAttribute("windowId", request.getWindowID());
            request.setAttribute("exoLocale", Util.getPortalRequestContext().getLocale().toString().toLowerCase());
            request.setAttribute("availableDisplays", new ArrayList<Display>(Arrays.asList(Display.values())));
            request.setAttribute("requestContextPath", request.getContextPath());
            
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
        String entity = request.getPreferences().getValue("entity", null);
        Display display = Display.fromName("Detail");
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

                Map<String, Object> metadataRenderParameters = null;
                String metadataRenderKey = entity + " | " + display + " | " + filter + " | " + fields;
                if (METADATA_RENDER_CACHE.containsKey(metadataRenderKey))
                {
                    metadataRenderParameters = METADATA_RENDER_CACHE.get(metadataRenderKey);
                }
                else
                {
                    metadataRenderParameters = new HashMap<String, Object>();

                    String entityId = Util.getPortalRequestContext().getRequestParameter(entity);

                    Document fieldsDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(fields.getBytes()));
                    NodeList fieldsNodeList = fieldsDocument.getElementsByTagName("field");

                    String controller = null;
                    Boolean historyEntity = false;
                    
                    List<Map<String, Object>> controllersMetadata = (List<Map<String, Object>>)(SignatureRepository.INSTANCE.parseMetadata(
                            (String) connectSettings.get("WebServiceUri"),
                            null)).get("Interface");
                    
                    List<Map<String, Object>> controllersMetadataUtility = (List<Map<String, Object>>)(SignatureRepository.INSTANCE.parseMetadata(
                            (String) connectSettings.get("WebServiceUri"),
                            null)).get("Utility");

                    if (controllersMetadata == null || controllersMetadata.isEmpty())
                    {
                        throw new Exception("Controllers metadata not found");
                    }

                    Map<String, Object> entityMetadata = null;
                    Map<String, String> entityToController = new HashMap<String,String>();
                    for (Map<String, Object> controllerMetadata : controllersMetadata)
                    {
                    	System.out.println((String)controllerMetadata.get("Controller"));
                        entityMetadata = (Map<String, Object>)controllerMetadata.get("Entity");
                        entityToController.put((String)entityMetadata.get("Name"),(String)controllerMetadata.get("Controller"));
                        if (entityMetadata.get("Name").equals(entity))
                        {
                            controller = (String)controllerMetadata.get("Controller");
                            historyEntity = (Boolean)controllerMetadata.get("UsesHistory");
                            break;
                        }
                    }
                    for (Map<String, Object> controllerMetadata : controllersMetadataUtility)
                    {
                    	System.out.println((String)controllerMetadata.get("Controller"));
                        entityMetadata = (Map<String, Object>)controllerMetadata.get("Entity");
                        entityToController.put((String)entityMetadata.get("Name"),(String)controllerMetadata.get("Controller"));
                    }
                    if (controller == null || controller.isEmpty() || entityMetadata == null)
                    {
                        throw new Exception("Entity metadata not found");
                    }

                    List<Map<String, Object>> fieldsMetadata = (List<Map<String, Object>>)(SignatureRepository.INSTANCE.parseMetadata(
                            (String) connectSettings.get("WebServiceUri"),
                            controller)).get("Fields");
                    if (fieldsMetadata == null || fieldsMetadata.isEmpty())
                    {
                        throw new Exception("Fields metadata not found");
                    }

                    List<Map<String, Object>> renderFieldsMetadata = new ArrayList<Map<String, Object>>();

                    //for (Map<String, Object> fieldMetadata : fieldsMetadata)
                    //{
                    //    if ((Boolean)fieldMetadata.get("IsRequired"))
                    //    {
                    //        fieldMetadata.put("Edit", "true");
                    //        renderFieldsMetadata.add(fieldMetadata);
                    //    }
                    // }

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
                                else if  (renderFieldMetadata != null && fieldNode.getNodeName().equals("default"))
                                {
                                	renderFieldMetadata.put("default",fieldNode.getTextContent());
                                }
                                else if (renderFieldMetadata != null && fieldNode.getNodeName().equals("input-type"))
                                {
                                    renderFieldMetadata.put("input-type", fieldNode.getTextContent());
                                }

                            }
                        }

                        if (renderFieldMetadata != null)
                        {
                            renderFieldsMetadata.add(renderFieldMetadata);
                        }
                    }

                    // TODO Remove with new metadata and filtering
                    List<Map<String, Object>> hiddenFieldsMetadata = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> renderFieldMetadata : renderFieldsMetadata)
                    {
                    	
                        String targetEntity = null;
                        if (renderFieldMetadata.get("LookupFor") != null)
                        {
                        	targetEntity = (String)renderFieldMetadata.get("LookupFor");
                        	renderFieldMetadata.put("LookupFor", targetEntity);
                        	renderFieldMetadata.put("Controller",entityToController.get(targetEntity));
                        }
                        
                    }
                        	
                    // TODO Remove with new metadata and filtering.
                    List<String> primaryKeys = (List<String>)entityMetadata.get("PrimaryKeys");
                    for (int primaryKeyIndex = 0; primaryKeyIndex < primaryKeys.size(); primaryKeyIndex++)
                    {
                        for (Map<String, Object> renderFieldMetadata : renderFieldsMetadata)
                        {
                            if (renderFieldMetadata.get("Name").equals(primaryKeys.get(primaryKeyIndex)))
                            {
                                Map<String, Object> primaryKeyTo = new HashMap<String, Object>();
                                primaryKeyTo.put("Entity", entityMetadata.get("Name"));
                                primaryKeyTo.put("FkIndex", primaryKeyIndex);

                                List<Map<String, Object>> foreignKeyTo = new ArrayList<Map<String, Object>>();
                                if (renderFieldMetadata.containsKey("ForeignKeyTo") && renderFieldMetadata.get("ForeignKeyTo") != null)
                                {
                                    foreignKeyTo = (List<Map<String, Object>>)renderFieldMetadata.get("ForeignKeyTo");
                                }
                                foreignKeyTo.add(primaryKeyTo);
                                renderFieldMetadata.put("ForeignKeyTo", foreignKeyTo);
                            }
                        }
                        for (Map<String, Object> hiddenFieldMetadata : hiddenFieldsMetadata)
                        {
                            if (hiddenFieldMetadata.get("Name").equals(primaryKeys.get(primaryKeyIndex)))
                            {
                                Map<String, Object> primaryKeyTo = new HashMap<String, Object>();
                                primaryKeyTo.put("Entity", entityMetadata.get("Name"));
                                primaryKeyTo.put("FkIndex", primaryKeyIndex);

                                List<Map<String, Object>> foreignKeyTo = null;
                                if (hiddenFieldMetadata.containsKey("ForeignKeyTo") && hiddenFieldMetadata.get("ForeignKeyTo") != null)
                                {
                                    foreignKeyTo = (List<Map<String, Object>>)hiddenFieldMetadata.get("ForeignKeyTo");
                                }
                                else
                                {
                                    foreignKeyTo = new ArrayList<Map<String, Object>>();
                                }
                                foreignKeyTo.add(primaryKeyTo);
                                hiddenFieldMetadata.put("ForeignKeyTo", foreignKeyTo);
                            }
                        }
                    }

                /*    String filterEntity = null;
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
                                    else
                                    {
                                        filterEntity = (String)((List<Map<String, Object>>)filterFieldMetadata.get("ForeignKeyTo")).get(0).get("Entity");
                                    }
                                }
                            }
                        }
                    }
                    */

                    /*
                    // TODO Remove with new metadata and filtering
                    if (display.equals(Display.LIST) && filter != null && !filter.isEmpty())
                    {
                        for (Map<String, Object> fieldMetadata : fieldsMetadata)
                        {
                            if (fieldMetadata.containsKey("ForeignKeyTo") && fieldMetadata.get("ForeignKeyTo") != null)
                            {
                                for (Map<String, Object> foreignKeyTo : (List<Map<String, Object>>)fieldMetadata.get("ForeignKeyTo"))
                                {
                                    if (foreignKeyTo.get("Entity").equals(filterEntity) &&
                                        !renderFieldsMetadata.contains(fieldMetadata) &&
                                        !hiddenFieldsMetadata.contains(fieldMetadata))
                                    {
                                        hiddenFieldsMetadata.add(fieldMetadata);
                                    }
                                }
                            }
                        }
                    }

                    */

                    metadataRenderParameters.put("Controller", controller);
                    metadataRenderParameters.put("RenderFieldsMetadata", renderFieldsMetadata);
                    // TODO Remove with new metadata and filtering
                    metadataRenderParameters.put("HiddenFieldsMetadata", hiddenFieldsMetadata);
                    //metadataRenderParameters.put("FilterEntity", filterEntity);
                    metadataRenderParameters.put("HistoryLimit",  historyEntity ? connectSettings.get("HistoryLimit") : -1);
                    METADATA_RENDER_CACHE.put(metadataRenderKey, metadataRenderParameters);
                    
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
                request.setAttribute("controller", metadataRenderParameters.get("Controller"));
                request.setAttribute("fieldsMetadata", metadataRenderParameters.get("RenderFieldsMetadata"));
                request.setAttribute("hiddenFieldsMetadata", metadataRenderParameters.get("HiddenFieldsMetadata"));
                request.setAttribute("filterEntity", metadataRenderParameters.get("FilterEntity"));
                request.setAttribute("historyLimit", metadataRenderParameters.get("HistoryLimit"));

                request.setAttribute("errorDataJson", request.getPortletSession().getAttribute("errorDataJson"));
                request.setAttribute("errorMessageJson", request.getPortletSession().getAttribute("errorMessageJson"));
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

        request.getPortletSession().removeAttribute("errorDataJson");
        request.getPortletSession().removeAttribute("errorMessageJson");

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
            Map<String, Object> connectSettings = ConnectRepository.INSTANCE.loadConnectSettings("customer-connect");
            if (connectSettings == null)
            {
                throw new Exception("Connect settings not found");
            }

            String save = request.getParameter("save");
            if (save.equals("Detail"))
            {
                JSONObject dataJsonObject = new JSONObject();
                for (String parameterName : Collections.list(request.getParameterNames()))
                {
                    if (!parameterName.equals("save") && !parameterName.equals("action"))
                    {
                        dataJsonObject.put(parameterName, request.getParameter(parameterName));
                    }
                }


                try
                {
                    String createDataResponse = SignatureRepository.INSTANCE.createData((String)connectSettings.get("WebServiceUri"), request.getParameter("controller"), dataJsonObject.toString().getBytes());
                    response.sendRedirect(String.format("%s?%s=%s",
                                          Util.getPortalRequestContext().getInitialURI(),
                                          request.getPreferences().getValue("entity", null),
                                          createDataResponse));
                }
                catch (Throwable throwable)
                {
                	LOGGER.log(Level.SEVERE, throwable.getMessage());

                    // TODO FIX EXCEPTION CLASSIFICATION
                    request.getPortletSession().setAttribute("errorDataJson", dataJsonObject.toString());
                    request.getPortletSession().setAttribute("errorMessageJson", throwable.getMessage());
                     // TODO Is this redirect necessary?
                    //response.sendRedirect(String.format("%s?%s=%s",
                    //                      Util.getPortalRequestContext().getInitialURI(),
                    //                      request.getPreferences().getValue("entity", null),
                    //                      "Create"));
                }
                

            }
            else if (save.equals("Edit"))
            {
                request.getPreferences().setValue("entity", request.getParameter("entity"));
                request.getPreferences().setValue("display", request.getParameter("display"));
                request.getPreferences().setValue("filter", request.getParameter("filter") != null ? request.getParameter("filter").trim() : null);
                request.getPreferences().setValue("pageSize", request.getParameter("pageSize"));
                request.getPreferences().setValue("exportControl", new Boolean(request.getParameter("exportControl") != null).toString());
                request.getPreferences().setValue("create", new Boolean(request.getParameter("create") != null).toString());
                request.getPreferences().setValue("columns", request.getParameter("columns"));
                request.getPreferences().setValue("fields", request.getParameter("fields"));
                request.getPreferences().store();

                METADATA_RENDER_CACHE.remove(request.getParameter("entity") + " | " + request.getParameter("fields"));
            }
        }
        catch (Throwable throwable)
        {
        	LOGGER.log(Level.SEVERE, throwable.getMessage());
            request.getPortletSession().setAttribute("error", throwable.getMessage());
        }
    }
    
    public enum Display
    {
        DETAIL("/jsp/request/view-detail.jsp", "Detail", true),
        EDIT("/jsp/request/edit.jsp", "Edit", false),
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
