package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIGrid;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIPageIterator;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;

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
    public static final String KEY_ENTITY_NUMBER = "keyEntityNumber";
    public static final String KEY_ENTITY_NAME = "keyEntityName";
    private static final String[] KEY_ENTITY_ACTION = { "Delete"};
    
    private static final String[] KEY_ENTITY_BEAN_FIELD = { CONNECT, KEY_ENTITY_NUMBER, KEY_ENTITY_NAME };
    private UIGrid grid_;
    
    public void load(String state) throws Exception
    {
    	grid_= addChild(UIGrid.class, null, "UIListKeyEntitiesGird");
        grid_.configure(CONNECT, KEY_ENTITY_BEAN_FIELD, KEY_ENTITY_ACTION);
        grid_.getUIPageIterator().setId("UIListKeyEntitiesGirdIterator");
        grid_.getUIPageIterator().setParent(this);
        grid_.getUIPageIterator().setPageList(new FindKeyEntitiesPageList(state, 10));
        UIPageIterator pageIterator = grid_.getUIPageIterator();
	    if (pageIterator.getAvailable() == 0) 
	    {
	        UIApplication uiApp = Util.getPortalRequestContext().getUIApplication();
	        uiApp.addMessage(new ApplicationMessage("UIListKeyEntities.label.noKeyEntity", null));
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
        	System.out.println("UserKeyEntities deleted successfully");
        }
    }
}