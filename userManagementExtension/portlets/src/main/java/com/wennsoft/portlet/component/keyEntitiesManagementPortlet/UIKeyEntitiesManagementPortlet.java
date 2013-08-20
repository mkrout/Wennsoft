package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

@ComponentConfig
(
    lifecycle = UIApplicationLifecycle.class, template = "app:/groovy/webui/component/keyEntitiesManagementPortlet/UIKeyEntitiesManagementPortlet.gtmpl"
)

@Serialized
public class UIKeyEntitiesManagementPortlet extends UIPortletApplication 
{
    public UIKeyEntitiesManagementPortlet() throws Exception 
    {
        addChild(UIListUsers.class, null, null).setRendered(true);
    }
}
