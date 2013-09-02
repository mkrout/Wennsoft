package com.wennsoft.service;

import com.wennsoft.repository.*;
import java.util.*;
import java.util.logging.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.json.*;

@Path("/resource/{connect}")
public class ConnectResource
{
    private static final Logger LOGGER = Logger.getLogger("InteractionManager");

    @GET
    @Path("/themes/{theme}/images/{image}")
    @Produces("image/*")
    public Response getImage(@Context UriInfo uriInfo)
    {
        return getFile(uriInfo.getPath().replaceFirst("resource/", ""));
    }

    @GET
    @Path("/localization/{localization}")
    @Produces("text/javascript")
    public Response getLocalization(@Context UriInfo uriInfo)
    {
        return getFile(uriInfo.getPath().replaceFirst("resource/", ""));
    }

    @GET
    @Path("/themes/{theme}/stylesheet.css")
    @Produces("text/css")
    public Response getTheme(@Context UriInfo uriInfo)
    {
        return getFile(uriInfo.getPath().replaceFirst("resource/", ""));
    }

    @GET
    @Path("/themes")
    @Produces("application/json")
    public Response getThemes(@Context UriInfo uriInfo)
    {
        return getDirectory(uriInfo.getPath().replaceFirst("resource/", ""));
    }

    @GET
    @Path("/themes/stylesheet.css")
    @Produces("text/css")
    public Response getStylesheet(@Context UriInfo uriInfo)
    {
        return getFile(uriInfo.getPath().replaceFirst("resource/", ""));
    }

    private Response getDirectory(String directoryPath)
    {
        Response response;

        try
        {
            List<Map<String, String>> resourceDirectoryContent = ConnectRepository.INSTANCE.loadResourceDirectory(directoryPath);
            JSONArray resourceDirectoryContentJsonArray = new JSONArray();
            for (Map<String, String> resourceDirectoryContentItem : resourceDirectoryContent)
            {
                JSONObject resourceDirectoryContentItemJsonObject = new JSONObject();
                for (String key : resourceDirectoryContentItem.keySet())
                {
                    resourceDirectoryContentItemJsonObject.put(key, resourceDirectoryContentItem.get(key));
                }
                resourceDirectoryContentJsonArray.put(resourceDirectoryContentItemJsonObject);
            }

            response = Response.status(200).entity(resourceDirectoryContentJsonArray.toString()).build();
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            response = Response.status(500).build();
        }

        return response;
    }

    private Response getFile(String filePath)
    {
        Response response;

        try
        {
            byte[] file = ConnectRepository.INSTANCE.loadResourceFile(filePath);
            if (file != null)
            {
                response = Response.status(200).entity(file).build();
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
}
