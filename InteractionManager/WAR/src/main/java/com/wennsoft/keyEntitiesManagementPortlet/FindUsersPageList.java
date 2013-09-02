package com.wennsoft.keyEntitiesManagementPortlet;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.commons.utils.PageListAccess;
import org.exoplatform.commons.utils.Safe;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;

public class FindUsersPageList extends PageListAccess<User, Query> 
{
	private static final long serialVersionUID = 1L;

	public FindUsersPageList(Query state, int pageSize) 
    {
        super(state, pageSize);
    }

    protected ListAccess<User> create(Query state) throws Exception 
    {
        ExoContainer container = PortalContainer.getInstance();
        OrganizationService service = (OrganizationService) container.getComponentInstance(OrganizationService.class);
        PageList<User> pageList = service.getUserHandler().findUsers(state);
        return Safe.unwrap(pageList);
    }
}
