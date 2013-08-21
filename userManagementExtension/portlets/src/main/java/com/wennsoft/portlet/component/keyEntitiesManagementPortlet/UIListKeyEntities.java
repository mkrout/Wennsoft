package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIPageIterator;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;

@ComponentConfig
(
    lifecycle = UIContainerLifecycle.class
)

@Serialized
public class UIListKeyEntities extends UIContainer 
{
    public static final String CONNECT = "connectId";
    public static final String KEY_ENTITY_NUMBER = "keyEntityNumber";
    public static final String KEY_ENTITY_NAME = "keyEntityName";
    
    private static final String[] KEY_ENTITY_BEAN_FIELD = { CONNECT, KEY_ENTITY_NUMBER, KEY_ENTITY_NAME };
    private UIGrid grid_;

    public UIListKeyEntities() throws Exception 
    {
        grid_= addChild(UIGrid.class, null, "UIListKeyEntitiesGird");
        grid_.configure(CONNECT, KEY_ENTITY_BEAN_FIELD, null);
        grid_.getUIPageIterator().setId("UIListKeyEntitiesGirdIterator");
        grid_.getUIPageIterator().setParent(this);
        String state = new String();
        grid_.getUIPageIterator().setPageList(new FindKeyEntitiesPageList(state, 2));
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
}
