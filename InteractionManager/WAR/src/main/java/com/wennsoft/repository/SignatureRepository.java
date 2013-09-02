package com.wennsoft.repository;

import org.apache.commons.lang.*;
import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.ws.rs.core.*;

public enum SignatureRepository
{
    INSTANCE;

    private final Map<String, String> METADATA_CACHE = new ConcurrentHashMap<String, String>();

    public void clearMetadataCache() throws Throwable
    {
        METADATA_CACHE.clear();
    }

    public String createData(String webServiceUri, String entityName, byte[] data) throws Throwable
    {
        String createDataResult = null;

        HttpURLConnection connection = null;
        try
        {
            String url = String.format("%s/Connect/%s", webServiceUri, entityName);

            connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            connection.connect();
            if (connection.getResponseCode() == 200)
            {
                createDataResult = readInputStream(connection.getInputStream());
            }
            else
            {
                JSONObject errorMessageJsonObject = new JSONObject();
                errorMessageJsonObject.put("Error", connection.getResponseCode());
                errorMessageJsonObject.put("Message", new JSONObject(readInputStream(connection.getErrorStream())));
                throw new Exception(errorMessageJsonObject.toString());
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }

        return createDataResult;
    }

    public int getMetadataCacheCount() throws Throwable
    {
        return METADATA_CACHE.size();
    }

    public int getMetadataCacheSize() throws Throwable
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(METADATA_CACHE);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.size();
    }

    public Map<String, Object> parseMetadata(String webServiceUri, String controller) throws Throwable
    {
        return convertJsonString(readMetadata(webServiceUri, controller));
    }

    public String readData(String webServiceUri, String controller, String entityId, MultivaluedMap<String, String> parameters) throws Throwable
    {
        String data = null;

        HttpURLConnection connection = null;
        try
        {
            StringBuffer webServiceUriPathBuffer = new StringBuffer();
            webServiceUriPathBuffer.append("/Connect");
            if (controller != null && !controller.isEmpty())
            {
                webServiceUriPathBuffer.append(String.format("/%s", controller));

                if (entityId != null && !entityId.isEmpty())
                {
                    webServiceUriPathBuffer.append(String.format("/%s", entityId));
                }
            }
            UriBuilder builder = UriBuilder.fromUri(new URI(webServiceUri + webServiceUriPathBuffer.toString()));
            if (parameters != null)
            {
                for (String parameterName : parameters.keySet())
                {
                    builder.queryParam(parameterName, StringUtils.join(parameters.get(parameterName), ","));
                }
            }

            connection = (HttpURLConnection)builder.build().toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "json");
            connection.setDoInput(true);

            connection.connect();
            if (connection.getResponseCode() == 200)
            {
                data = readInputStream(connection.getInputStream());
            }
            else
            {
                JSONObject errorMessageJsonObject = new JSONObject();
                errorMessageJsonObject.put("Error", connection.getResponseCode());
                errorMessageJsonObject.put("Message", new JSONObject(readInputStream(connection.getErrorStream())));
                throw new Exception(errorMessageJsonObject.toString());
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }

        return data;
    }

    public String readMetadata(String webServiceUri, String controller) throws Throwable
    {
        String metadata = null;

        String metadataCacheKey = controller != null ? controller : "";
        if (METADATA_CACHE.containsKey(metadataCacheKey))
        {
            metadata = METADATA_CACHE.get(metadataCacheKey);
        }
        else
        {
            HttpURLConnection connection = null;
            try
            {
                StringBuffer webServiceUriPathBuffer = new StringBuffer();
                webServiceUriPathBuffer.append("/Connect");
                if (controller != null && !controller.isEmpty())
                {
                    webServiceUriPathBuffer.append(String.format("/%s", controller));
                }
                webServiceUriPathBuffer.append(String.format("/%s", "Metadata"));

                UriBuilder builder = UriBuilder.fromUri(new URI(webServiceUri + webServiceUriPathBuffer.toString()));

                connection = (HttpURLConnection)builder.build().toURL().openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "json");
                connection.setDoInput(true);

                connection.connect();
                if (connection.getResponseCode() == 200)
                {
                    metadata = readInputStream(connection.getInputStream());
                    METADATA_CACHE.put(metadataCacheKey, metadata);
                }
                else
                {
                    JSONObject errorMessageJsonObject = new JSONObject();
                    errorMessageJsonObject.put("Error", connection.getResponseCode());
                    errorMessageJsonObject.put("Message", new JSONObject(readInputStream(connection.getErrorStream())));
                    throw new Exception(errorMessageJsonObject.toString());
                }
            }
            finally
            {
                if (connection != null)
                {
                    connection.disconnect();
                }
            }
        }

        return metadata;
    }

    public String updateData(String webServiceUri, String entityName, String entityId, byte[] data) throws Throwable
    {
        String updateDataResult = null;

        HttpURLConnection connection = null;
        try
        {
            String url = String.format("%s/Connect/%s/%s", webServiceUri, entityName, entityId);

            connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setDoInput(true);

            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.close();

            connection.connect();
            if (connection.getResponseCode() == 200)
            {
                updateDataResult = readInputStream(connection.getInputStream());
            }
            else
            {
                JSONObject errorMessageJsonObject = new JSONObject();
                errorMessageJsonObject.put("Error", connection.getResponseCode());
                errorMessageJsonObject.put("Message", new JSONObject(readInputStream(connection.getErrorStream())));
                throw new Exception(errorMessageJsonObject.toString());
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }

        return updateDataResult;
    }

    private Map<String, Object> convertJsonString(String json) throws Throwable
    {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        if (json != null && !json.isEmpty())
        {
            JSONArray jsonArray = null;
            JSONObject jsonObject = null;
            try
            {
                jsonArray = new JSONArray(json);
            }
            catch (JSONException jsonArrayException)
            {
                try
                {
                    jsonObject = new JSONObject(json);
                }
                catch (JSONException jsonObjectException)
                {
                    throw jsonObjectException;
                }
            }

            if (jsonArray != null)
            {
                jsonMap.put("Data", convertJsonArray(jsonArray));
            }
            else if (jsonObject != null)
            {
                jsonMap = convertJsonObject(jsonObject);
            }
        }

        return jsonMap;
    }

    private List<Object> convertJsonArray(JSONArray jsonArray) throws Throwable
    {
        List<Object> jsonArrayList = new ArrayList<Object>();

        for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++)
        {
            Object jsonArrayObject = jsonArray.get(jsonArrayIndex);
            if (jsonArrayObject.getClass().equals(JSONArray.class))
            {
                jsonArrayList.add(convertJsonArray((JSONArray) jsonArrayObject));
            }
            else if (jsonArrayObject.getClass().equals(JSONObject.class))
            {
                jsonArrayList.add(convertJsonObject((JSONObject) jsonArrayObject));
            }
            else
            {
                jsonArrayList.add(jsonArrayObject);
            }
        }

        return jsonArrayList;
    }

    private Map<String, Object> convertJsonObject(JSONObject jsonObject) throws Throwable
    {
        Map<String, Object> jsonObjectMap = new HashMap<String, Object>();

        if (jsonObject.equals(JSONObject.NULL))
        {
            jsonObjectMap = null;
        }
        else
        {
            Iterator<?> jsonIterator = jsonObject.keys();
            while (jsonIterator.hasNext())
            {
                String key = (String)jsonIterator.next();
                Object value = jsonObject.get(key);

                if (value.equals(JSONObject.NULL))
                {
                    jsonObjectMap.put(key, null);
                }
                else if (value.getClass().equals(JSONArray.class))
                {
                    jsonObjectMap.put(key, convertJsonArray((JSONArray)value));
                }
                else if (value.getClass().equals(JSONObject.class))
                {
                    jsonObjectMap.put(key, convertJsonObject((JSONObject)value));
                }
                else
                {
                    jsonObjectMap.put(key, value);
                }
            }
        }

        return jsonObjectMap;
    }

    private String readInputStream(InputStream inputStream) throws Throwable
    {
        StringBuffer output = new StringBuffer();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
        char[] charBuffer = new char[4096];
        int bufferIndex = 0;
        do
        {
            if ((bufferIndex = bufferedReader.read(charBuffer, 0, 4096)) >= 0)
            {
                output.append(charBuffer, 0, bufferIndex);
            }
        }
        while (bufferIndex > 0);

        return output.toString();
    }
}
