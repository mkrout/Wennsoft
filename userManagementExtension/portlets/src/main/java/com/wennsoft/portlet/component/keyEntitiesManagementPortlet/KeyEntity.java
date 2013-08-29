package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

public class KeyEntity 
{
	private String num;
	private String connectId;
	private String key;
	
    public KeyEntity(String num, String connectId, String key) 
    {
    	this.num = num;
    	this.connectId = connectId;
	    this.key = key;
    }
    
    public String getNum() 
    {
	    return num; 
    }
	
    public String getConnectId() 
    {
	    return connectId; 
    }
    
	public String getKey() 
    {
	    return key;
    }
	
	public void setNum(String num) 
    {
        this.num = num;
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
