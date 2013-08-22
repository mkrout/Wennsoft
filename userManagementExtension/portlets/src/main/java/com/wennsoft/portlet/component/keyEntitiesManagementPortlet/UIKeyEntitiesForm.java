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
        @EventConfig(listeners = UIKeyEntitiesForm.SaveActionListener.class, phase = Phase.DECODE), 
        @EventConfig(listeners = UIKeyEntitiesForm.CancelActionListener.class, phase = Phase.DECODE),
        @EventConfig(listeners = UIKeyEntitiesForm.AddKeyEntitiesActionListener.class, phase = Phase.DECODE)
    }
)
@Serialized
public class UIKeyEntitiesForm extends UIForm
{
	public void load(String userName) throws Exception
    {
		addChild(UIListKeyEntities.class, null, null);
        setActions(new String[] {"Save", "Cancel", "AddKeyEntities"});
    }

    public static class SaveActionListener extends EventListener<UIKeyEntitiesForm> 
    {
        public void execute(Event<UIKeyEntitiesForm> event) throws Exception 
        {
        	System.out.println("UserKeyEntities saved successfully");
        }
    }
    
    public static class CancelActionListener extends EventListener<UIKeyEntitiesForm> 
    {
        public void execute(Event<UIKeyEntitiesForm> event) throws Exception 
        {
        	System.out.println("UserKeyEntities cancelled successfully");
        }
    }
    
    public static class AddKeyEntitiesActionListener extends EventListener<UIKeyEntitiesForm> 
    {
        public void execute(Event<UIKeyEntitiesForm> event) throws Exception 
        {
            UIKeyEntitiesForm uiKeyEntitiesForm = event.getSource();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiKeyEntitiesForm.getParent();
            uiKeyEntitiesManagementPortlet.setAddPopup();
        }
    }
}