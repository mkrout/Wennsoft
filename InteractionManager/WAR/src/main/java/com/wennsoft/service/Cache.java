package com.wennsoft.service;

import com.wennsoft.portlet.*;
import com.wennsoft.repository.*;
import java.util.logging.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.json.*;

@Path("/cache")
public class Cache
{
    private static final Logger LOGGER = Logger.getLogger("InteractionManager");

    @GET
    @Produces("application/json")
    public Response getCaches()
    {
        Response response;

        try
        {
            JSONArray dataJsonArray = new JSONArray();
            dataJsonArray.put(new String[]
            {
                "Settings",
                Integer.toString(ConnectRepository.INSTANCE.getConnectSettingsCacheCount()),
                Integer.toString(ConnectRepository.INSTANCE.getConnectSettingsCacheSize()),
            });
            dataJsonArray.put(new String[]
            {
                "Resource",
                Integer.toString(ConnectRepository.INSTANCE.getResourceCacheCount()),
                Integer.toString(ConnectRepository.INSTANCE.getResourceCacheSize()),
            });
            dataJsonArray.put(new String[]
            {
                "Repository",
                Integer.toString(SignatureRepository.INSTANCE.getMetadataCacheCount()),
                Integer.toString(SignatureRepository.INSTANCE.getMetadataCacheSize()),
            });
            dataJsonArray.put(new String[]
            {
                "Render",
                Integer.toString(DataDisplay.getRenderCacheCount()),
                Integer.toString(DataDisplay.getRenderCacheSize()),
            });

            JSONObject responseJsonObject = new JSONObject();
            responseJsonObject.put("iTotalRecords", dataJsonArray.length());
            responseJsonObject.put("iTotalDisplayRecords", dataJsonArray.length());
            responseJsonObject.put("aaData", dataJsonArray);
            response = Response.status(200).entity(responseJsonObject.toString()).build();
        }
        catch (Throwable throwable)
        {
            LOGGER.log(Level.SEVERE, throwable.getMessage());
            response = Response.status(500).build();
        }

        return response;
    }

    @DELETE
    @Path("/{cache}")
    public Response clearCache(@PathParam("cache") String cache)
    {
        Response response;

        try
        {
            if (cache.equals("Settings"))
            {
                ConnectRepository.INSTANCE.clearConnectSettingsCache();
                response = Response.status(200).build();
            }
            else if (cache.equals("Resource"))
            {
                ConnectRepository.INSTANCE.clearResourceCache();
                response = Response.status(200).build();
            }
            else if (cache.equals("Repository"))
            {
                SignatureRepository.INSTANCE.clearMetadataCache();
                response = Response.status(200).build();
            }
            else if (cache.equals("Render"))
            {
                DataDisplay.clearRenderCache();
                response = Response.status(200).build();
            }
            else
            {
                response = Response.status(400).build();
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
