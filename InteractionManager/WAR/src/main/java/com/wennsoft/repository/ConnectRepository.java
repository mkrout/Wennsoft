package com.wennsoft.repository;

import com.thoughtworks.xstream.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public enum ConnectRepository
{
    INSTANCE;

    private final XStream XSTREAM = new XStream();
    private final Map<String, Map<String, Object>> CONNECT_SETTINGS_CACHE = new ConcurrentHashMap<String, Map<String, Object>>();
    private final Map<String, Object> RESOURCE_CACHE = new ConcurrentHashMap<String, Object>();

    public void clearConnectSettingsCache() throws Throwable
    {
        CONNECT_SETTINGS_CACHE.clear();
    }

    public void clearResourceCache() throws Throwable
    {
        RESOURCE_CACHE.clear();
    }

    public int getConnectSettingsCacheCount() throws Throwable
    {
        return CONNECT_SETTINGS_CACHE.size();
    }

    public int getResourceCacheCount() throws Throwable
    {
        return RESOURCE_CACHE.size();
    }

    public int getConnectSettingsCacheSize() throws Throwable
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(CONNECT_SETTINGS_CACHE);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.size();
    }

    public int getResourceCacheSize() throws Throwable
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(RESOURCE_CACHE);
        byteArrayOutputStream.close();
        return byteArrayOutputStream.size();
    }

    public Map<String, Object> loadConnectSettings(String connect) throws Throwable
    {
        Map<String, Object> connectSettings = null;

        if (CONNECT_SETTINGS_CACHE.containsKey(connect))
        {
            connectSettings = CONNECT_SETTINGS_CACHE.get(connect);
        }
        else
        {
            connectSettings = new HashMap<String, Object>();

            File connectSettingsFile = new File(connect + File.separator + "settings.xml");
            if (!connectSettingsFile.exists())
            {
                if (!connectSettingsFile.createNewFile())
                {
                    throw new Exception("Connect settings file could not be created");
                }
            }

            if (connectSettingsFile.length() > 0)
            {
                InputStream inputStream = null;
                try
                {
                    inputStream = new FileInputStream(connectSettingsFile);
                    connectSettings = (Map<String, Object>)XSTREAM.fromXML(inputStream);
                    CONNECT_SETTINGS_CACHE.put(connect, connectSettings);
                }
                finally
                {
                    if (inputStream != null)
                    {
                        inputStream.close();
                    }
                }
            }
        }

        if (!connectSettings.containsKey("HistoryLimit") || (Integer)connectSettings.get("HistoryLimit") < 5)
        {
            connectSettings.put("HistoryLimit", 5);
        }
        if (!connectSettings.containsKey("Theme") || connectSettings.get("Theme") == null || ((String)connectSettings.get("Theme")).isEmpty())
        {
            connectSettings.put("Theme", "default");
        }
        
        return connectSettings;
    }

    public List<Map<String, String>> loadResourceDirectory(String directoryPath) throws Throwable
    {
        List<Map<String, String>> directory = null;

        if (RESOURCE_CACHE.containsKey(directoryPath))
        {
            directory = (List<Map<String, String>>)RESOURCE_CACHE.get(directoryPath);
        }
        else
        {
            File resourceDirectoryFile = new File(directoryPath);
            if (resourceDirectoryFile.exists() && resourceDirectoryFile.isDirectory())
            {
                directory = new ArrayList<Map<String, String>>();
                for (File resourceDirectorySubFile : resourceDirectoryFile.listFiles())
                {
                    Map<String, String> resourceDirectoryFileInformation = new HashMap<String, String>();
                    resourceDirectoryFileInformation.put("Name", resourceDirectorySubFile.getName());
                    resourceDirectoryFileInformation.put("Type", resourceDirectorySubFile.isDirectory() ? "Directory" : "File");
                    directory.add(resourceDirectoryFileInformation);
                }

                RESOURCE_CACHE.put(directoryPath, directory);
            }
        }

        return directory;
    }

    public byte[] loadResourceFile(String filePath) throws Throwable
    {
        byte[] file = null;

        if (RESOURCE_CACHE.containsKey(filePath))
        {
            file = (byte[])RESOURCE_CACHE.get(filePath);
        }
        else
        {
            FileInputStream resourceFileInputStream = null;
            try
            {
                File resourceFile = new File(filePath);
                if (resourceFile.exists() && resourceFile.isFile())
                {
                    resourceFileInputStream = new FileInputStream(resourceFile);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int readResult;
                    byte[] buffer = new byte[1024];
                    while ((readResult = resourceFileInputStream.read(buffer, 0, buffer.length)) != -1)
                    {
                        byteArrayOutputStream.write(buffer, 0, readResult);
                    }
                    file = byteArrayOutputStream.toByteArray();

                    RESOURCE_CACHE.put(filePath, file);
                }
            }
            finally
            {
                if (resourceFileInputStream != null)
                {
                    resourceFileInputStream.close();
                }
            }
        }

        return file;
    }

    public void saveConnectSettings(String connect, Map<String, Object> connectSettings) throws Throwable
    {
        File connectSettingsFile = new File(connect + File.separator + "settings.xml");
        if (!connectSettingsFile.exists())
        {
            if (!connectSettingsFile.createNewFile())
            {
                throw new Exception("Connect settings file could not be created");
            }
        }

        OutputStream outputStream = null;
        try
        {
            outputStream = new FileOutputStream(connectSettingsFile);
            XSTREAM.toXML(connectSettings, outputStream);
            CONNECT_SETTINGS_CACHE.put(connect, connectSettings);
            clearResourceCache();
        }
        finally
        {
            if (outputStream != null)
            {
                outputStream.close();
            }
        }
    }
}
