package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.organization.Query;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIGrid;
import org.exoplatform.webui.core.UISearch;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;

@ComponentConfig
(
    lifecycle = UIContainerLifecycle.class, events = { @EventConfig(listeners = UIListUsers.DisplayUserKeyEntitiesActionListener.class) }
)

@Serialized
public class UIListUsers extends UISearch 
{
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String USER_NAME = "userName";
    
    private static final String[] USER_BEAN_FIELD = { USER_NAME, LAST_NAME, FIRST_NAME, EMAIL };
    private static final String[] USER_ACTION = { "DisplayUserKeyEntities"};
    @SuppressWarnings("unchecked")
	private static final List<SelectItemOption<String>> OPTIONS_ = Collections.unmodifiableList(Arrays.asList
    		(
                new SelectItemOption<String>(USER_NAME, USER_NAME), new SelectItemOption<String>(LAST_NAME, LAST_NAME),
                new SelectItemOption<String>(FIRST_NAME, FIRST_NAME), new SelectItemOption<String>(EMAIL, EMAIL))
            );
    
    private Query lastQuery_;
    private String userSelected_;
    private UIGrid grid_;

    public UIListUsers() throws Exception 
    {
        super(OPTIONS_);
        grid_ = addChild(UIGrid.class, null, "UIListUsersGird");
        grid_.configure(USER_NAME, USER_BEAN_FIELD, USER_ACTION);
        grid_.getUIPageIterator().setId("UIListUsersIterator");
        grid_.getUIPageIterator().setParent(this);
    }
    
    public String getUserSelected() {
        return userSelected_;
    }
    
    public void advancedSearch(UIFormInputSet advancedSearchInput) 
    {
    }

    @Override
    public void processRender(WebuiRequestContext context) throws Exception 
    {
        int curPage = grid_.getUIPageIterator().getCurrentPage();
        if (lastQuery_ == null)
        {
            lastQuery_ = new Query();
        }    
        search(lastQuery_);
        grid_.getUIPageIterator().setCurrentPage(curPage);
        grid_.getUIPageIterator().getCurrentPageData();
        super.processRender(context);
    }
    
    public void quickSearch(UIFormInputSet quickSearchInput) throws Exception 
    {
        Query query = new Query();
        UIFormStringInput input = (UIFormStringInput) quickSearchInput.getChild(0);
        UIFormSelectBox select = (UIFormSelectBox) quickSearchInput.getChild(1);
        String name = input.getValue();
        if (name != null && !(name = name.trim()).equals("")) 
        {
            if (name.indexOf("*") < 0) 
            {
                if (name.charAt(0) != '*')
                {
                    name = "*" + name;
                }    
                if (name.charAt(name.length() - 1) != '*')
                {
                    name += "*";
                }    
            }
            name = name.replace('?', '_');
            String selectBoxValue = select.getValue();
            if (selectBoxValue.equals(USER_NAME))
            {	
                query.setUserName(name);
            }    
            if (selectBoxValue.equals(LAST_NAME))
            {	
                query.setLastName(name);
            }    
            if (selectBoxValue.equals(FIRST_NAME))
            {	
                query.setFirstName(name);
            }    
            if (selectBoxValue.equals(EMAIL))
            {	
                query.setEmail(name);
            }    
        }
        search(query);
        if (getChild(UIGrid.class).getUIPageIterator().getAvailable() == 0) 
        {
            UIApplication uiApp = Util.getPortalRequestContext().getUIApplication();
            uiApp.addMessage(new ApplicationMessage("UISearchForm.msg.empty", null));
        }
    }

    public void setUserSelected(String userName) 
    {
        userSelected_ = userName;
    }
    
    public void search(Query query) 
    {
        lastQuery_ = query;
        grid_.getUIPageIterator().setPageList(new FindUsersPageList(query, 10));
    }
   
    public static class DisplayUserKeyEntitiesActionListener extends EventListener<UIListUsers> 
    {
        public void execute(Event<UIListUsers> event) throws Exception 
        {
            UIListUsers uiListUsers = event.getSource();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiListUsers.getParent();
            UIKeyEntitiesForm uiKeyEntitiesForm = uiKeyEntitiesManagementPortlet.getChild(UIKeyEntitiesForm.class);
            uiKeyEntitiesForm.setRendered(true);
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesManagementPortlet);
        }
    }
}
