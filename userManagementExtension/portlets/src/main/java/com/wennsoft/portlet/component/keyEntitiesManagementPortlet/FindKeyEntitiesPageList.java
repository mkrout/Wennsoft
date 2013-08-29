package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.commons.utils.ListAccessImpl;
import org.exoplatform.commons.utils.PageListAccess;

public class FindKeyEntitiesPageList extends PageListAccess<KeyEntity, String> 
{
	private static final long serialVersionUID = 1L;

    public FindKeyEntitiesPageList(String state, int pageSize) 
    {
	    super(state, pageSize);
    }

	@Override
	protected ListAccess<KeyEntity> create(String state) throws Exception 
	{	
        List<KeyEntity> listKeyEntities = new ArrayList<KeyEntity>();
        String keyEntities = Utils.getAttributeUserProfile(state, "keyEntities");
        KeyEntity keyEntity;
        if (keyEntities != null)
        {
        	String[] splittedKeyEntities = keyEntities.split("@");
        	int count = 0;
        	for (String splittedKeyEntity : splittedKeyEntities)
        	{
        	    if (!splittedKeyEntity.equals(""))
        		{
        		    keyEntity = new KeyEntity(String.valueOf(count), splittedKeyEntity.split("/")[0], splittedKeyEntity.split("/")[1]);
        		    listKeyEntities.add(keyEntity);
        		    count++;
        		}
        	}
        }
		return new ListAccessImpl<KeyEntity>(KeyEntity.class, listKeyEntities);
	}
}