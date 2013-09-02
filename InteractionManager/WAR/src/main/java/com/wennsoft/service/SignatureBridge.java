package com.wennsoft.service;

import com.wennsoft.repository.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.apache.commons.lang.*;
import org.json.JSONObject;

@Path("/bridge/{connect}")
public class SignatureBridge
{
    private static final Logger LOGGER = Logger.getLogger("InteractionManager");

    @GET
    @Path("/{controller}{entityId:(/.*)?}")
    @Produces("application/json")
    public Response getData(@Context HttpServletRequest request,
                            @Context UriInfo uriInfo,
                            @PathParam("connect") String connect,
                            @PathParam("controller") String controller,
                            @PathParam("entityId") String entityId)
    {
        Response response;

        try
        {
            Map<String, Object> connectSettings = ConnectRepository.INSTANCE.loadConnectSettings(connect);
            if (connectSettings == null)
            {
                throw new Exception("Connect settings not found");
            }

            List<String> formattedEntitySegments = new ArrayList<String>();
            for (String entityIdSegment : entityId.split("/"))
            {
                if (!entityIdSegment.isEmpty())
                {
                    formattedEntitySegments.add(URLEncoder.encode(entityIdSegment, "UTF-8").replace("+", "%20"));
                }
            }

            if (request.getRemoteUser()!=null && request.getRemoteUser().equalsIgnoreCase("mtoboggan") && controller.equals("Customers"))
            {
                uriInfo.getQueryParameters().add("Filters", "{\"op\": \"&&\", \"conds\": [[ \"CustomerNumber\", \"==\", \"101\"]]}");
            }
            if (request.getRemoteUser()!=null && request.getRemoteUser().equalsIgnoreCase("mtoboggan") && controller.equals("Locations"))
            {
                uriInfo.getQueryParameters().add("Filters", "{\"op\": \"&&\", \"conds\": [[ \"CustomerNumber\", \"==\", \"101\"]]}");
            }
           
            formatRequestParameterNames(uriInfo.getQueryParameters());

            String data = SignatureRepository.INSTANCE.readData((String)connectSettings.get("WebServiceUri"),
                                                                controller,
                                                                StringUtils.join(formattedEntitySegments, "/"),
                                                                uriInfo.getQueryParameters());
            
            if (data != null)
            {
                JSONObject dataJsonObject = new JSONObject(data);
                formatResponseParameterNames(dataJsonObject);
                response = Response.status(200).entity(dataJsonObject.toString()).build();
            }
            else
            {
                response = Response.status(404).build();
            }
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            response = Response.status(500).build();
        }

        return response;
    }

    @GET
    @Path("/{controller:(controller)?}/metadata")
    @Produces("application/json")
    public Response getMetadata(@PathParam("connect") String connect,
                                @PathParam("controller") String controller)
    {
        Response response;

        try
        {
            Map<String, Object> connectSettings = ConnectRepository.INSTANCE.loadConnectSettings(connect);
            if (connectSettings == null)
            {
                throw new Exception("Connect settings not found");
            }

            String data = SignatureRepository.INSTANCE.readMetadata((String)connectSettings.get("WebServiceUri"),
                                                                    controller);
            if (data != null)
            {
                JSONObject dataJsonObject = new JSONObject(data);
                formatResponseParameterNames(dataJsonObject);
                response = Response.status(200).entity(dataJsonObject.toString()).build();
            }
            else
            {
                response = Response.status(404).build();
            }
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            response = Response.status(500).build();
        }

        return response;
    }

    /**
     * Scrub library-specific parameter names to absolve the remote service of dependencies.
     */
    private void formatRequestParameterNames(MultivaluedMap<String, String> parameters) throws Throwable
    {
        if (parameters.containsKey("_"))
        {
            parameters.remove("_");
        }
        if (parameters.containsKey("bRegex"))
        {
            parameters.remove("bRegex");
        }
        if (parameters.containsKey("sEcho"))
        {
            parameters.remove("sEcho");
        }
        if (parameters.containsKey("iColumns"))
        {
            int columnCount = Integer.parseInt(parameters.remove("iColumns").get(0));
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
            {
                if (parameters.containsKey("bRegex_" + columnIndex))
                {
                    parameters.remove("bRegex_" + columnIndex);
                }
                if (parameters.containsKey("bSearchable_" + columnIndex))
                {
                    parameters.remove("bSearchable_" + columnIndex);
                }
                if (parameters.containsKey("bSortable_" + columnIndex))
                {
                    parameters.remove("bSortable_" + columnIndex);
                }
                if (parameters.containsKey("mDataProp_" + columnIndex))
                {
                    parameters.remove("mDataProp_" + columnIndex);
                }
                if (parameters.containsKey("sSearch_" + columnIndex))
                {
                    parameters.remove("sSearch_" + columnIndex);
                }
            }
        }
        if (parameters.containsKey("iDisplayLength"))
        {
            parameters.put("DisplayLength", parameters.remove("iDisplayLength"));
        }
        if (parameters.containsKey("iDisplayStart"))
        {
            parameters.put("DisplayStart", parameters.remove("iDisplayStart"));
        }
        if (parameters.containsKey("iSortingCols"))
        {
            List<Integer> sortColumns = new ArrayList<Integer>();
            List<String> sortDirections = new ArrayList<String>();
            int sortColumnCount = Integer.parseInt(parameters.remove("iSortingCols").get(0));
            for (int sortColumnIndex = 0; sortColumnIndex < sortColumnCount; sortColumnIndex++)
            {
                if (parameters.containsKey("iSortCol_" + sortColumnIndex))
                {
                    sortColumns.add(Integer.parseInt(parameters.remove("iSortCol_" + sortColumnIndex).get(0)));
                }
                if (parameters.containsKey("sSortDir_" + sortColumnIndex))
                {
                    sortDirections.add(parameters.remove("sSortDir_" + sortColumnIndex).get(0));
                }
            }

            parameters.add("SortColumns", StringUtils.join(sortColumns, ","));
            parameters.add("SortDirections", StringUtils.join(sortDirections, ","));
        }
        if (parameters.containsKey("sColumns"))
        {
            parameters.remove("sColumns");
        }
        if (parameters.containsKey("sSearch"))
        {
            parameters.remove("sSearch");
        }
    }

    /**
     * Scrub library-specific parameter names to absolve the remote service of dependencies.
     */
    private void formatResponseParameterNames(JSONObject dataJsonObject) throws Throwable
    {
        if (dataJsonObject.has("EntityData"))
        {
            dataJsonObject.put("aaData", dataJsonObject.remove("EntityData"));
        }
        if (dataJsonObject.has("TotalDisplayRecords"))
        {
            dataJsonObject.put("iTotalDisplayRecords", dataJsonObject.remove("TotalDisplayRecords"));
        }
        if (dataJsonObject.has("TotalRecords"))
        {
            dataJsonObject.put("iTotalRecords", dataJsonObject.remove("TotalRecords"));
        }
    }
}
