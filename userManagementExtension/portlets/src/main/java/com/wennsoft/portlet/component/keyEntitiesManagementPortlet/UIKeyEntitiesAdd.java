package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.commons.utils.SerializablePageList;
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
import org.exoplatform.webui.form.UIFormInputInfo;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormPageIterator;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.input.UICheckBoxInput;


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
            @EventConfig(listeners = UIKeyEntitiesAdd.ChangeProductActionListener.class, phase=Phase.DECODE),
            @EventConfig(listeners = UIKeyEntitiesAdd.SaveActionListener.class, phase=Phase.DECODE),
            @EventConfig(listeners = UIKeyEntitiesAdd.SelectBoxActionListener.class, phase=Phase.DECODE),
            @EventConfig(listeners = UIKeyEntitiesAdd.ShowPageActionListener.class)
        }
)
@Serialized
public class UIKeyEntitiesAdd extends UIForm
{
    public final static String PRODUCT = "product";
    public final static String PRODUCTS_ONCHANGE = "ChangeProduct";
    public final static String TABLE_NAME =  "UIKeyEntitiesAdd";
    public final static String INCLUDE = "include";
    private static String product;
    private static String userName;
    private static List<KeyEntity> listKeyEntities_;
    private List<String> selectedKeys = new ArrayList<String>();
    
    private void addSelectedKey(String key) 
    {
        selectedKeys.add(key);
    }
    
    private void clearSelectedKeys() 
    {
        selectedKeys.clear();
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
        product = productSelectBox.getValue();
    }

    private void update(List<KeyEntity> listKeyEntities) throws Exception
    {
    	listKeyEntities_ = listKeyEntities;
    	clearSelectedKeys();
        UIFormTableIteratorInputSet uiFormTableInputSet = createUIComponent(UIFormTableIteratorInputSet.class, null, TABLE_NAME);
        List<UIFormInputSet> uiFormInputSetList = new ArrayList<UIFormInputSet>();
        UICheckBoxInput uiCheckBoxInput;
        if (product != null && product.equals("customer"))
        {
            String[] columnsTable = {"CustomerNumber","CustomerName", INCLUDE};
            UIFormInputSet uiFormInputSet;
            removeChild(UIFormTableIteratorInputSet.class);
            uiFormTableInputSet.setName(TABLE_NAME);
            uiFormTableInputSet.setColumns(columnsTable);
            addChild(uiFormTableInputSet);
            String data = null;
            String bridgeUri= "http://localhost:8080/interaction-manager/service/bridge/customer-connect";
            String controller= "Customers";
            String parameters= "?fields=CustomerNumber,CustomerName";
            try
            {
                data = Utils.getEntitiesList(bridgeUri, controller, parameters);
                Map<String, Object> entitiesMap = new HashMap<String, Object>(Utils.convertJsonString(data));
                List<Object> entitiesList = (List <Object>) entitiesMap.get("aaData");
                for (Object entity : entitiesList)
                {
                    String keyValue = product + "/" ;
                    uiFormInputSet = new UIFormInputSet(columnsTable[0]);
                    List<String> entity_ =  (List<String>) entity;
                    Iterator<String> iterator = entity_.iterator();
                    int count = 0;
                    while (iterator.hasNext()) 
                    {
                        String value = iterator.next();
                        uiFormInputSet.addChild(new UIFormInputInfo(columnsTable[count], null, value));
                        keyValue = keyValue + columnsTable[count] + ":" + value + ";";
                        count ++;
                    }
                    uiCheckBoxInput = new UICheckBoxInput(keyValue, keyValue, false);
                    uiCheckBoxInput.setOnChange("SelectBox");
                    for (KeyEntity keyEntity : listKeyEntities)
                    {
                    	if (keyValue.equals(keyEntity.getConnectId() + "/" + keyEntity.getKey()))
                    	{
                    		uiCheckBoxInput.setDisabled(true);
                    		break;
                    	}
                    }
                    uiFormInputSet.addChild(uiCheckBoxInput);
                    uiFormInputSetList.add(uiFormInputSet);
                    uiFormTableInputSet.addChild(uiFormInputSet);
                }
            } 
            catch (Throwable throwable) 
            {
                throwable.printStackTrace();
            }
        }
        UIFormPageIterator uiIterator = uiFormTableInputSet.getChild(UIFormPageIterator.class);
        SerializablePageList<UIFormInputSet> pageList = new SerializablePageList<UIFormInputSet>(UIFormInputSet.class, uiFormInputSetList, 3);
        uiIterator.setPageList(pageList);
    }
    
    private void removeUnSelectedKey(String key) 
    {
        selectedKeys.remove(key);
    }

    private List<String> getSelectedKeys() 
    {
        return Collections.unmodifiableList(selectedKeys);
    }
    
    public void init(String userName_, List<KeyEntity> listKeyEntities) throws Exception
    {
        userName = userName_;
        UIFormSelectBox uiFormProductSelectBox = new UIFormSelectBox(PRODUCT, null, null);
        initProductSelectBox(uiFormProductSelectBox);
        uiFormProductSelectBox.setOnChange(PRODUCTS_ONCHANGE);
        setActions(new String[] {"Save", "Cancel"}) ;
        addUIFormInput(uiFormProductSelectBox) ;
        update(listKeyEntities);
    }

    static public class CancelActionListener extends EventListener<UIKeyEntitiesAdd>
    {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception
        {
            UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource();
            uiKeyEntitiesAdd.clearSelectedKeys();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiKeyEntitiesAdd.getAncestorOfType(UIKeyEntitiesManagementPortlet.class);
            UIPopupWindow uiPopupWindow = uiKeyEntitiesManagementPortlet.findComponentById(UIKeyEntitiesManagementPortlet.KEY_ENTITIES_ADD_POPUP);
            uiPopupWindow.setShowMask(true);
            uiPopupWindow.setRendered(false);
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesManagementPortlet);
        }
    }

    public static class ChangeProductActionListener extends EventListener<UIKeyEntitiesAdd>
    {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception
        {
            UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource() ;
            UIFormSelectBox productSelection = uiKeyEntitiesAdd.getUIFormSelectBox(PRODUCT);
            product = productSelection.getValue();
            uiKeyEntitiesAdd.update(listKeyEntities_);
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesAdd);
        }
    }

    static public class SaveActionListener extends EventListener<UIKeyEntitiesAdd>
    {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception
        {
            UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource();
            List<KeyEntity> listSelectedKeyEntities = new ArrayList<KeyEntity>();
            for(String key : uiKeyEntitiesAdd.getSelectedKeys())
            {
                listSelectedKeyEntities.add(new KeyEntity(key.split("/")[0], key.split("/")[1]));
            }
            if(uiKeyEntitiesAdd.getSelectedKeys().size() == 0)
            {
                UIApplication uiApplication = uiKeyEntitiesAdd.getAncestorOfType(UIApplication.class);
                uiApplication.addMessage(new ApplicationMessage("UIKeyEntitiesAdd.msg.noKeyEntitiesSelected", null));
                return;
            }
            uiKeyEntitiesAdd.clearSelectedKeys();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiKeyEntitiesAdd.getAncestorOfType(UIKeyEntitiesManagementPortlet.class);
            UIListKeyEntities uiListKeyEntities = uiKeyEntitiesManagementPortlet.getChild(UIListKeyEntities.class);
            uiListKeyEntities.addAllListKeyEntities(listSelectedKeyEntities);
            uiListKeyEntities.init(userName, listSelectedKeyEntities);
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesManagementPortlet);
            UIPopupWindow uiPopupWindow = uiKeyEntitiesManagementPortlet.findComponentById(UIKeyEntitiesManagementPortlet.KEY_ENTITIES_ADD_POPUP);
            uiPopupWindow.setShowMask(true);
            uiPopupWindow.setRendered(false);
        }
    }
    
    static public class SelectBoxActionListener extends EventListener<UIKeyEntitiesAdd> 
    {
    	@Override
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception 
        {
            UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource();
            UIFormTableIteratorInputSet uiFormTableIteratorInputSet = uiKeyEntitiesAdd.getChild(UIFormTableIteratorInputSet.class);
            UIFormPageIterator uiIterator = uiFormTableIteratorInputSet.getChild(UIFormPageIterator.class);
            List<UIFormInputSet> uiFormInputSetList = uiIterator.getCurrentPageData();
            List<UICheckBoxInput> listCheckBoxInputs =  new ArrayList<UICheckBoxInput>();
            for (UIFormInputSet uiFormInputSet : uiFormInputSetList) 
            {
            	listCheckBoxInputs.add(uiFormInputSet.getChild(UICheckBoxInput.class));
            }
            for (UICheckBoxInput uiCheckBoxInput : listCheckBoxInputs) 
            {
            	if (uiCheckBoxInput != null)
            	{
                    if (uiCheckBoxInput.isChecked()) 
                    {
                    	if (!uiKeyEntitiesAdd.getSelectedKeys().contains(uiCheckBoxInput.getName())) 
                    	{
                            uiKeyEntitiesAdd.addSelectedKey(uiCheckBoxInput.getName());
                    	}    
                    }
                    else
                    {
                        if (uiKeyEntitiesAdd.getSelectedKeys().contains(uiCheckBoxInput.getName())) 
                        {
                            uiKeyEntitiesAdd.removeUnSelectedKey(uiCheckBoxInput.getName());
                        }
                    }
            	}
            }
        }
    }

    public static class ShowPageActionListener extends EventListener<UIKeyEntitiesAdd> 
    {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception 
        {
        	UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource();
            UICheckBoxInput uiCheckBoxInput;
            for (String key : uiKeyEntitiesAdd.getSelectedKeys()) 
            {
                uiCheckBoxInput = uiKeyEntitiesAdd.getUIInput(key);
            	uiCheckBoxInput.setChecked(true);
            }
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesAdd);
        }
    }
}
