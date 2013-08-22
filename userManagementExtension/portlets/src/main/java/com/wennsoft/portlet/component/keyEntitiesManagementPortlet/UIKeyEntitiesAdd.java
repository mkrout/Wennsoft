package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.UIFormInputInfo;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormTableInputSet;

/**
 * @author MedAmine Krout
*/
@ComponentConfig
(
    lifecycle = UIFormLifecycle.class, template =  "classpath:groovy/ecm/webui/form/UIForm.gtmpl",
    events = 
    {
        @EventConfig(listeners = UIKeyEntitiesAdd.SaveActionListener.class, phase=Phase.DECODE),
        @EventConfig(listeners = UIKeyEntitiesAdd.CancelActionListener.class, phase=Phase.DECODE),
        @EventConfig(listeners = UIKeyEntitiesAdd.ChangeProductActionListener.class, phase=Phase.DECODE)
    }
)
public class UIKeyEntitiesAdd extends UIForm 
{
	public final static String INPUT = "input";
	public final static String NAME = "name";
    public final static String NUMBER = "number";
    public final static String PRODUCTS = "products";
    public final static String PRODUCTS_ONCHANGE = "ChangeProduct";
    public final static String TABLE_NAME =  "UIKeyEntitiesAdd";
    public final static String[] TABLE_COLUMNS = {NAME, NUMBER, INPUT};
    private static String product;

    private List<KeyEntity> keyEntities = new ArrayList<KeyEntity>();
    
    public void init() throws Exception 
    {
        UIFormSelectBox uiFormProductSelectBox = new UIFormSelectBox(PRODUCTS, null, null);
        initProductSelectBox(uiFormProductSelectBox);
        uiFormProductSelectBox.setOnChange(PRODUCTS_ONCHANGE);
        setActions(new String[] {"Save", "Cancel"}) ;
        addUIFormInput(uiFormProductSelectBox) ;
        update();
    } 

    public void update() throws Exception 
    {
        if (product!=null && product.equals("customer")) 
    	{
    	    KeyEntity keyEntity = new KeyEntity("customer", "Handy Gloves Inc1.", "12-1031");
    	    keyEntities.add(keyEntity);
    	    keyEntity = new KeyEntity("customer", "eXoplatform1", "12-1032");
    	    keyEntities.add(keyEntity);
    	    keyEntity = new KeyEntity("customer", "Capgemini1", "12-1033");
    	    keyEntities.add(keyEntity); 
    	}
    	if (product!=null && product.equals("other")) 
    	{
    	    KeyEntity keyEntity = new KeyEntity("other", "XXXXXX", "12-1034");
    	    keyEntities.add(keyEntity);
    	    keyEntity = new KeyEntity("other", "YYYYYY", "12-1035");
    	    keyEntities.add(keyEntity);
    	    keyEntity = new KeyEntity("other", "ZZZZZZ", "12-1036");
    	    keyEntities.add(keyEntity); 
    	} 
        UIFormInputSet uiInputSet;
        removeChild(UIFormTableInputSet.class);
        UIFormTableInputSet uiFormTableInputSet = createUIComponent(UIFormTableInputSet.class, null, null);
        uiFormTableInputSet.setName(TABLE_NAME);
        uiFormTableInputSet.setColumns(TABLE_COLUMNS);
        UIFormInputInfo uiName;
        UIFormInputInfo uiNumber;
        for(KeyEntity keyEntity_ : keyEntities)
        {
            String keyEntityName = keyEntity_.getKeyEntityName();
            String keyEntityNumber = keyEntity_.getKeyEntityNumber();
            uiInputSet = new UIFormInputSet(keyEntityName);
            uiName = new UIFormInputInfo(NAME, null, keyEntityName);
            uiNumber = new UIFormInputInfo(NUMBER, null, keyEntityNumber);
            uiInputSet.addChild(uiName);
            uiInputSet.addChild(uiNumber);
            UIFormCheckBoxInput<String> uiCheckbox = new UIFormCheckBoxInput<String>(keyEntityNumber, keyEntityNumber, null);
            uiCheckbox.setChecked(false);
            uiCheckbox.setValue(keyEntityName);
            uiInputSet.addChild(uiCheckbox);
            uiFormTableInputSet.addChild(uiInputSet);
        }
        addUIFormInput(uiFormTableInputSet);
    }

    public KeyEntity getKeyEntityByNumber(String keyEntityNumber) throws Exception 
    {
        KeyEntity keyEntitySelected = null;
        for(KeyEntity keyEntity : keyEntities) 
        {
            if(keyEntity.getKeyEntityName().equals(keyEntityNumber)) 
            {
                keyEntitySelected = keyEntity;
                break;
            }
        }
        return keyEntitySelected;
    }

    static public class CancelActionListener extends EventListener<UIKeyEntitiesAdd> 
    {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception 
        {
            UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource();
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
            UIFormSelectBox productSelection = uiKeyEntitiesAdd.getUIFormSelectBox(PRODUCTS);
            product = productSelection.getValue();
            uiKeyEntitiesAdd.update();
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesAdd);
        }
    } 
    
    static public class SaveActionListener extends EventListener<UIKeyEntitiesAdd> 
    {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception 
        {
            UIKeyEntitiesAdd uiKeyEntitiesAdd = event.getSource();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiKeyEntitiesAdd.getAncestorOfType(UIKeyEntitiesManagementPortlet.class);
            UIApplication uiApplication = uiKeyEntitiesAdd.getAncestorOfType(UIApplication.class);
            List<KeyEntity> selectedKeys = new ArrayList<KeyEntity>();
            List<UIFormCheckBoxInput> listCheckbox =  new ArrayList<UIFormCheckBoxInput>();
            uiKeyEntitiesAdd.findComponentOfType(listCheckbox, UIFormCheckBoxInput.class);
            for(UIFormCheckBoxInput checkbox : listCheckbox) 
            {
                if(checkbox.isChecked())
                { 
                    selectedKeys.add(uiKeyEntitiesAdd.getKeyEntityByNumber(checkbox.getName()));
                    System.out.println(checkbox.getName()+"  added");
                }	
            }
            if(selectedKeys.size() == 0) 
            {
                uiApplication.addMessage(new ApplicationMessage("No keys entities selected", null));
                return;
            }
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesManagementPortlet);
            UIPopupWindow uiPopupWindow = uiKeyEntitiesManagementPortlet.findComponentById(UIKeyEntitiesManagementPortlet.KEY_ENTITIES_ADD_POPUP);
            uiPopupWindow.setShowMask(true);
            uiPopupWindow.setRendered(false);
        }
    }
    
    private void initProductSelectBox(UIFormSelectBox productSelectBox)
    {
        List<SelectItemOption<String>> product_ = new ArrayList<SelectItemOption<String>>();
        SelectItemOption<String> option;
        option = new SelectItemOption<String>("Customer Connect", "customer");
        product_.add(option);
        option = new SelectItemOption<String>("Other Connect", "other");
        product_.add(option);
        productSelectBox.setOptions(product_);
        productSelectBox.setValue(option.getValue());
        product = option.getValue();
    } 
}
