package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

public class KeyEntity 
{
    private String connectId;
	private String keyEntityName;
    private String keyEntityNumber;
	
    public KeyEntity(String connectId, String keyEntityName, String keyEntityNumber) 
    {
	    this.connectId = connectId;
	    this.keyEntityName = keyEntityName;
	    this.keyEntityNumber = keyEntityNumber;
    }
	
    public String getConnectId() 
    {
	    return connectId; 
    }
    
	public String getKeyEntityName() 
    {
	    return keyEntityName;
    }
	
	public String getKeyEntityNumber() 
    {
	    return keyEntityNumber;
    }
    
	public void setConnectId(String connectId) 
    {
        this.connectId = connectId;
    }
    
	public void setKeyEntityName(String keyEntityName) 
	{
	    this.keyEntityName = keyEntityName;
	}
	
	public void setKeyEntityNumber(String keyEntityNumber) 
    {
	    this.keyEntityNumber = keyEntityNumber;
    }
}
