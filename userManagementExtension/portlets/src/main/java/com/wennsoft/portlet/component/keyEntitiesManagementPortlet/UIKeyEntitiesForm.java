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
        @EventConfig(listeners = UIKeyEntitiesForm.CancelActionListener.class, phase = Phase.DECODE)
    }
)
@Serialized
public class UIKeyEntitiesForm extends UIForm
{

    public UIKeyEntitiesForm() throws Exception
    {
        addChild(UIListKeyEntities.class, null, null);
        setActions(new String[] {"Save", "Cancel"});
    }

    public static class SaveActionListener extends EventListener<UIListKeyEntities> 
    {
        public void execute(Event<UIListKeyEntities> event) throws Exception 
        {
        	System.out.println("UserKeyEntities saved successfully");
        }
    }
    
    public static class CancelActionListener extends EventListener<UIListKeyEntities> 
    {
        public void execute(Event<UIListKeyEntities> event) throws Exception 
        {
        	System.out.println("UserKeyEntities cancelled successfully");
        }
    }
}