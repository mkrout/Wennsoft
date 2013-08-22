package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.List;
import java.util.ArrayList;
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
	protected ListAccess<KeyEntity> create(String state) throws Exception {
        List<KeyEntity> keyEntities = new ArrayList<KeyEntity>();
        if (state.equals("root")) 
        {   
            KeyEntity keyEntity = new KeyEntity("Customer", "Handy Gloves Inc.", "12-1031");
            keyEntities.add(keyEntity);
            keyEntity = new KeyEntity("Customer", "eXoplatform", "12-1032");
            keyEntities.add(keyEntity);
            keyEntity = new KeyEntity("Customer", "Capgemini", "12-1033");
            keyEntities.add(keyEntity);
        }
		return new ListAccessImpl<KeyEntity>(KeyEntity.class, keyEntities);
	}
}
