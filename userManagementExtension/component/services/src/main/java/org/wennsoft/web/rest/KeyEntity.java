package org.wennsoft.web.rest;

public class KeyEntity 
{
	private String connectId;
	private String key;
	
    public KeyEntity() 
    {
	}
	
    public KeyEntity(String connectId, String key) 
    {
    	this.connectId = connectId;
	    this.key = key;
    }
    
	public String getConnectId() 
    {
	    return connectId; 
    }
    
	public String getKey() 
    {
	    return key;
    }
	
	public void setConnectId(String connectId) 
    {
        this.connectId = connectId;
    }
    
	public void setKey(String key) 
	{
	    this.key = key;
	}
}
