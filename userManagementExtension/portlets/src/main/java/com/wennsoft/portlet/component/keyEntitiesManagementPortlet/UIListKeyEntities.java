package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIGrid;
import org.exoplatform.webui.core.UIPageIterator;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;

@ComponentConfig
(
    lifecycle = UIContainerLifecycle.class,
    events = 
        {
    	    @EventConfig(listeners = UIListKeyEntities.DeleteActionListener.class, phase=Phase.DECODE)
    	}
)

@Serialized
public class UIListKeyEntities extends UIContainer 
{
    public static final String CONNECT = "connectId";
    public static final String KEY = "key";
    private static final String[] KEY_ENTITY_ACTION = { "Delete"};
    
    private static final String[] KEY_ENTITY_BEAN_FIELD = { CONNECT, KEY };
    private static UIGrid grid_;
    private static String state_;
    
    public void load(String state) throws Exception
    {
    	state_ = state;
    	grid_ = getChild(UIGrid.class);
        if (grid_ != null) 
        {
        	removeChild(UIGrid.class);
        }
    	grid_= addChild(UIGrid.class, null, "UIListKeyEntitiesGird");
        grid_.configure(KEY, KEY_ENTITY_BEAN_FIELD, KEY_ENTITY_ACTION);
        grid_.getUIPageIterator().setId("UIListKeyEntitiesGirdIterator");
        grid_.getUIPageIterator().setParent(this);
        grid_.getUIPageIterator().setPageList(new FindKeyEntitiesPageList(state, 10));
        UIPageIterator pageIterator = grid_.getUIPageIterator();
	    if (pageIterator.getAvailable() == 0) 
	    {
	        UIApplication uiApp = Util.getPortalRequestContext().getUIApplication();
	        uiApp.addMessage(new ApplicationMessage("UIListKeyEntities.msg.noKeyEntity", null));
	    }
    }

    @Override
    public void processRender(WebuiRequestContext context) throws Exception 
    {
        int curPage = grid_.getUIPageIterator().getCurrentPage();
        grid_.getUIPageIterator().setCurrentPage(curPage);
        grid_.getUIPageIterator().getCurrentPageData();
        super.processRender(context);
    }
    
    public static class DeleteActionListener extends EventListener<UIListKeyEntities> 
    {
        public void execute(Event<UIListKeyEntities> event) throws Exception 
        {
        	UIListKeyEntities uiListKeyEntities = event.getSource();
        	String key = event.getRequestContext().getRequestParameter(OBJECTID);
        	String keyEntities = Utils.getAttributeUserProfile(state_, "keyEntities");
            String keyEntity = new String();
            if (keyEntities != null)
            {
            	List<String> splittedKeyEntities = new ArrayList<String>();
            	for (String splittedKeyEntity : keyEntities.split("@"))
            	{
            		if (!splittedKeyEntity.split("/")[1].equals(key))
            		{
            			splittedKeyEntities.add(splittedKeyEntity);
            		}
            	}
            	for (String splittedKeyEntity : splittedKeyEntities)
            	{
            		keyEntity += "@" + splittedKeyEntity ;
            	}
            	Utils.setAttributeUserProfile(state_, "keyEntities", !keyEntity.equals("")?keyEntity.substring(1):keyEntity);
            	uiListKeyEntities.load(state_);
            	event.getRequestContext().addUIComponentToUpdateByAjax(uiListKeyEntities);
            }
        }
    }
}