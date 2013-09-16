package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormHiddenInput;
import org.exoplatform.webui.form.UIFormSelectBox;

/**
 * @author MedAmine Krout
 */
@ComponentConfig
(
    lifecycle = UIFormLifecycle.class, 
    template =  "app:/groovy/webui/component/keyEntitiesManagementPortlet/UIKeyEntitiesAdd.gtmpl",
    events =
        {
            @EventConfig(listeners = UIKeyEntitiesAdd.CancelActionListener.class, phase=Phase.DECODE),
            @EventConfig(listeners = UIKeyEntitiesAdd.SaveActionListener.class, phase=Phase.DECODE)
        }
)
@Serialized
public class UIKeyEntitiesAdd extends UIForm
{
    public final static String PRODUCT = "product";
    private String keyEntities = "";
    
    public String getKeyEntities() 
    {
	    return keyEntities;
	}
    
    private void initProductSelectBox(UIFormSelectBox productSelectBox)
    {
        List<SelectItemOption<String>> lisSelectItemOptions = new ArrayList<SelectItemOption<String>>();
        SelectItemOption<String> selectItemOption;
        selectItemOption = new SelectItemOption<String>("Customer Connect", "customer");
        String defaultSelectItemOption = selectItemOption.getValue();
        lisSelectItemOptions.add(selectItemOption);
        productSelectBox.setOptions(lisSelectItemOptions);
        productSelectBox.setValue(defaultSelectItemOption);
    }

	public void init(List<KeyEntity> listKeyEntities_) throws Exception
    {
        String[] listSplittedKey;
        for (KeyEntity keyEntity : listKeyEntities_)
    	{
        	listSplittedKey = keyEntity.getKey().split(";");
        	String key = "";
        	for (String splittedKey : listSplittedKey)
        	{
        		key += "," + splittedKey.split(":")[1];
        	}
        	keyEntities += ";" + key.substring(1);
    	}
        if (!keyEntities.equals(""))
        {   
            keyEntities = keyEntities.substring(1);
        }    
        UIFormHiddenInput uiFormHiddenInput = new UIFormHiddenInput("selectedKeys", "");
        uiFormHiddenInput.setId("selectedKeys");
        addChild(uiFormHiddenInput);
        UIFormSelectBox uiFormProductSelectBox = new UIFormSelectBox(PRODUCT, null, null);
        initProductSelectBox(uiFormProductSelectBox);
        setActions(new String[] {"Save", "Cancel"}) ;
        addUIFormInput(uiFormProductSelectBox) ;
    }

    public static class CancelActionListener extends EventListener<UIKeyEntitiesAdd>
    {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception
        {
            UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiKeyEntitiesAdd.getAncestorOfType(UIKeyEntitiesManagementPortlet.class);
            UIPopupWindow uiPopupWindow = uiKeyEntitiesManagementPortlet.findComponentById(UIKeyEntitiesManagementPortlet.KEY_ENTITIES_ADD_POPUP);
            uiPopupWindow.setShowMask(true);
            uiPopupWindow.setRendered(false);
        }
    }

    public static class SaveActionListener extends EventListener<UIKeyEntitiesAdd>
    {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception
        {
            UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource();
        	UIFormHiddenInput uiFormHiddenInput = uiKeyEntitiesAdd.getChild(UIFormHiddenInput.class);
            List<KeyEntity> listSelectedKeyEntities = new ArrayList<KeyEntity>();
            if (uiFormHiddenInput.getValue() != null && !uiFormHiddenInput.getValue().equals("")) 
            {
            	for(String key : uiFormHiddenInput.getValue().substring(1).split("@"))
                {
                    listSelectedKeyEntities.add(new KeyEntity(key.split("/")[0], key.split("/")[1]));
                }
            }
            else
            {
                UIApplication uiApplication = uiKeyEntitiesAdd.getAncestorOfType(UIApplication.class);
                uiApplication.addMessage(new ApplicationMessage("UIKeyEntitiesAdd.msg.noKeyEntitiesSelected", null));
                return;
            }
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiKeyEntitiesAdd.getAncestorOfType(UIKeyEntitiesManagementPortlet.class);
            UIListKeyEntities uiListKeyEntities = uiKeyEntitiesManagementPortlet.getChild(UIListKeyEntities.class);
            uiListKeyEntities.addAllListKeyEntities(listSelectedKeyEntities);
            UIPopupWindow uiPopupWindow = uiKeyEntitiesManagementPortlet.findComponentById(UIKeyEntitiesManagementPortlet.KEY_ENTITIES_ADD_POPUP);
            uiPopupWindow.setShowMask(true);
            uiPopupWindow.setRendered(false);
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesManagementPortlet);
        }
    }
}
