package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

@ComponentConfig
(
    lifecycle = UIApplicationLifecycle.class, template = "app:/groovy/webui/component/keyEntitiesManagementPortlet/UIKeyEntitiesManagementPortlet.gtmpl"
)

@Serialized
public class UIKeyEntitiesManagementPortlet extends UIPortletApplication 
{
    final static public String KEY_ENTITIES_ADD_POPUP = "KeysEntitiesAddPopup";

    public UIKeyEntitiesManagementPortlet() throws Exception 
    {
        addChild(UIListUsers.class, null, null).setRendered(true);
        UIListKeyEntities uiListKeyEntities = getChild(UIListKeyEntities.class);
        if (uiListKeyEntities != null)
        {
            removeChild(UIListKeyEntities.class);
        }
        addChild(UIListKeyEntities.class, null, null).setRendered(false);
    }

    public void setAddPopup(String userName_, List<KeyEntity> listKeyEntities) throws Exception
    {
        removeChildById(KEY_ENTITIES_ADD_POPUP) ;
        UIPopupWindow uiPopupWindow = addChild(UIPopupWindow.class, null, KEY_ENTITIES_ADD_POPUP);
        uiPopupWindow.setWindowSize(800, 600);
        UIKeyEntitiesAdd uiKeyEntitiesAdd = uiPopupWindow.createUIComponent(UIKeyEntitiesAdd.class, null, null);
        uiKeyEntitiesAdd.init(listKeyEntities) ;
        uiPopupWindow.setUIComponent(uiKeyEntitiesAdd) ;
        uiPopupWindow.setShow(true) ;
        uiPopupWindow.setResizable(true) ;
    }
}
