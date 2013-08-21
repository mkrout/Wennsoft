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
        KeyEntity keyEntity = new KeyEntity("Customer", "12-1031", "Handy Gloves Inc.");
        keyEntities.add(keyEntity);
        keyEntity = new KeyEntity("Customer", "12-1032", "eXoplatform");
        keyEntities.add(keyEntity);
        keyEntity = new KeyEntity("Customer", "12-1033", "Capgemini");
        keyEntities.add(keyEntity);
		return new ListAccessImpl<KeyEntity>(KeyEntity.class, keyEntities);
	}
}
