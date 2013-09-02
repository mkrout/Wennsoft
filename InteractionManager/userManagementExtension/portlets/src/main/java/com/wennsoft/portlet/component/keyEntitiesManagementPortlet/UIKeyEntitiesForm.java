package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;

@ComponentConfig
(
    lifecycle = UIFormLifecycle.class, template = "app:/groovy/webui/component/keyEntitiesManagementPortlet/UIKeyEntitiesForm.gtmpl", 
    events = 
    { 
        @EventConfig(listeners = UIKeyEntitiesForm.AddKeyEntitiesActionListener.class, phase = Phase.DECODE)
    }
)
@Serialized
public class UIKeyEntitiesForm extends UIForm
{
	private static String userName;
	
	public void load(String userName_) throws Exception
    {
	    userName = userName_;
		UIListKeyEntities uiListKeyEntities = getChild(UIListKeyEntities.class);
        if (uiListKeyEntities != null) 
        {
        	removeChild(UIListKeyEntities.class);
        }
		addChild(UIListKeyEntities.class, null, null);
        setActions(new String[] {"AddKeyEntities"});
    }

    public static class AddKeyEntitiesActionListener extends EventListener<UIKeyEntitiesForm> 
    {
        public void execute(Event<UIKeyEntitiesForm> event) throws Exception 
        {
            UIKeyEntitiesForm uiKeyEntitiesForm = event.getSource();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiKeyEntitiesForm.getParent();
            uiKeyEntitiesManagementPortlet.setAddPopup(userName);
        }
    }
}