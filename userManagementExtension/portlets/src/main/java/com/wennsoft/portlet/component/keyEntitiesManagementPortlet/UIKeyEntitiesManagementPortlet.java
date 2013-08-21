package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

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
    final static public String ADD_POPUP = "KeysAntitiesAddPopup";

    public UIKeyEntitiesManagementPortlet() throws Exception 
    {
        addChild(UIListUsers.class, null, null).setRendered(true);
        addChild(UIKeyEntitiesForm.class, null, null).setRendered(false);
        setAddPopup() ;
    }

    public void setAddPopup() throws Exception {
        removeChildById(ADD_POPUP) ;
        UIPopupWindow uiPopup = addChild(UIPopupWindow.class, null, ADD_POPUP);
        uiPopup.setWindowSize(500, 400);
        UIKeyEntitiesAdd uiAdd = uiPopup.createUIComponent(UIKeyEntitiesAdd.class, null, null);
        uiAdd.update() ;
        uiPopup.setUIComponent(uiAdd) ;
        uiPopup.setShow(true) ;
        uiPopup.setResizable(true) ;
    }
}
