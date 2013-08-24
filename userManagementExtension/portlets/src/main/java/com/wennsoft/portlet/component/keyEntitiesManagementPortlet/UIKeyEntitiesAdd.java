package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    lifecycle = UIFormLifecycle.class, template =  "app:/groovy/webui/component/keyEntitiesManagementPortlet/UIKeyEntitiesAdd.gtmpl",
    events = 
    {
        @EventConfig(listeners = UIKeyEntitiesAdd.SaveActionListener.class, phase=Phase.DECODE),
        @EventConfig(listeners = UIKeyEntitiesAdd.CancelActionListener.class, phase=Phase.DECODE),
        @EventConfig(listeners = UIKeyEntitiesAdd.ChangeProductActionListener.class, phase=Phase.DECODE)
    }
)
public class UIKeyEntitiesAdd extends UIForm 
{
    public final static String PRODUCTS = "products";
    public final static String PRODUCTS_ONCHANGE = "ChangeProduct";
    public final static String TABLE_NAME =  "UIKeyEntitiesAdd";
	public final static String INCLUDE = "include";
    private static String product;
    private static String userName;
    private static String keyEntitiesAttributeValue;
    
    private void initProductSelectBox(UIFormSelectBox productSelectBox)
    {
        List<SelectItemOption<String>> lisSelectItemOptions = new ArrayList<SelectItemOption<String>>();
        SelectItemOption<String> selectItemOption;
        selectItemOption = new SelectItemOption<String>("Customer Connect", "customer");
        String defaultSelectItemOption = selectItemOption.getValue();
        lisSelectItemOptions.add(selectItemOption);
        selectItemOption = new SelectItemOption<String>("Vendor Connect", "vendor");
        lisSelectItemOptions.add(selectItemOption);
        productSelectBox.setOptions(lisSelectItemOptions);
        productSelectBox.setValue(defaultSelectItemOption);
        product = productSelectBox.getValue();
    }

    private void update() throws Exception 
    {
        List<Map<String, String>> customers = new ArrayList<Map<String, String>>();
        List<Map<String, String>> vendors = new ArrayList<Map<String, String>>();
        UIFormTableInputSet uiFormTableInputSet = createUIComponent(UIFormTableInputSet.class, null, null);
        if (product!=null && product.equals("customer"))
    	{
            Map<String, String> customer = new HashMap<String, String>();
            customer.put("Customer Name","Handy Gloves Inc.");
            customer.put("Customer Number","12-1031");
            customers.add(customer);
            customer = new HashMap<String, String>();
            customer.put("Customer Name","eXoplatform1");
            customer.put("Customer Number", "12-1032");
            customers.add(customer);
            customer = new HashMap<String, String>();
            customer.put("Customer Name","Capgemini1");
            customer.put("Customer Number","12-1033");
            customers.add(customer);
            String[] columnsTable = new String [customer.size()+1];
            int count = 0;
            for (Map.Entry<String, String> entry : customer.entrySet()) 
            {
                columnsTable[count]= entry.getKey();
                count++;
            }
            columnsTable[customer.size()] = INCLUDE;
            UIFormInputSet uiFormInputSet;
            removeChild(UIFormTableInputSet.class);
            uiFormTableInputSet.setName(TABLE_NAME);
            uiFormTableInputSet.setColumns(columnsTable);
            for( Map<String, String> customer_ : customers)
            {
                String keyValue = product + "/" ;
                uiFormInputSet = new UIFormInputSet(columnsTable[0]);
                for (Map.Entry<String, String> entry : customer_.entrySet()) 
                {
                	uiFormInputSet.addChild(new UIFormInputInfo(entry.getKey(), null, entry.getValue()));
                    keyValue=keyValue + entry.getKey() + ":" + entry.getValue()+ ";";
                }
                UIFormCheckBoxInput<String> uiFormCheckBoxInput = new UIFormCheckBoxInput<String>(keyValue, keyValue, null);
                uiFormCheckBoxInput.setChecked(false);
                uiFormCheckBoxInput.setValue(keyValue);
                if (keyEntitiesAttributeValue.contains(keyValue))
                    uiFormCheckBoxInput.setDisabled(true);
                uiFormInputSet.addChild(uiFormCheckBoxInput);
                uiFormTableInputSet.addChild(uiFormInputSet);
            }
    	}
    	if (product!=null && product.equals("vendor"))
    	{
            Map<String, String> vendor = new HashMap<String, String>();
            vendor.put("Vendor Number","SAV-1200");
            vendor.put("Vendor Branch","MKE");
            vendor.put("Vendor Name","Gloves");
            vendors.add(vendor);
            vendor = new HashMap<String, String>();
            vendor.put("Vendor Number","SAV-1202");
            vendor.put("Vendor Branch", "AAA");
            vendor.put("Vendor Name","eXoplatform1");
            vendors.add(vendor);
            vendor = new HashMap<String, String>();
            vendor.put("Vendor Number","SAV-1204");
            vendor.put("Vendor Branch","FFF");
            vendor.put("Vendor Name","Capgemini1");
            vendors.add(vendor);
            vendor = new HashMap<String, String>();
            vendor.put("Vendor Number","SAV-1300");
            vendor.put("Vendor Branch","ZZZ");
            vendor.put("Vendor Name","Handy");
            vendors.add(vendor);
            String[] columnsTable = new String [vendor.size()+1];
            int count = 0;
            for (Map.Entry<String, String> entry : vendor.entrySet()) {
            	columnsTable[count]= entry.getKey();
                count++;
            }
            columnsTable[vendor.size()] = INCLUDE;
            UIFormInputSet uiFormInputSet;
            removeChild(UIFormTableInputSet.class);
            uiFormTableInputSet.setName(TABLE_NAME);
            uiFormTableInputSet.setColumns(columnsTable);
            for( Map<String, String> vendor_ : vendors)
            {
                String keyValue= product + "/" ;
                uiFormInputSet = new UIFormInputSet(columnsTable[0]);
                for (Map.Entry<String, String> entry : vendor_.entrySet()) 
                {
                    uiFormInputSet.addChild(new UIFormInputInfo(entry.getKey(), null, entry.getValue()));
                    keyValue = keyValue + entry.getKey() + ":" + entry.getValue() + ";";
                }
                UIFormCheckBoxInput<String> uiFormCheckBoxInput = new UIFormCheckBoxInput<String>(keyValue, keyValue, null);
                uiFormCheckBoxInput.setChecked(false);
                uiFormCheckBoxInput.setValue(keyValue);
                if (keyEntitiesAttributeValue.contains(keyValue))
                    uiFormCheckBoxInput.setDisabled(true);
                uiFormInputSet.addChild(uiFormCheckBoxInput);
                uiFormTableInputSet.addChild(uiFormInputSet);
            }
        }
        addUIFormInput(uiFormTableInputSet);
    }
    
    public void init(String userName_) throws Exception
    {
        userName = userName_;
        keyEntitiesAttributeValue = Utils.getAttributeUserProfile(userName, "keyEntities") != null && !Utils.getAttributeUserProfile(userName, "keyEntities").equals("")?Utils.getAttributeUserProfile(userName, "keyEntities")+ "&":"";
        UIFormSelectBox uiFormProductSelectBox = new UIFormSelectBox(PRODUCTS, null, null);
        initProductSelectBox(uiFormProductSelectBox);
        uiFormProductSelectBox.setOnChange(PRODUCTS_ONCHANGE);
        setActions(new String[] {"Save", "Cancel"}) ;
        addUIFormInput(uiFormProductSelectBox) ;
        update();
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
            List<String> selectedKeys = new ArrayList<String>();
            List<UIFormCheckBoxInput> listCheckBoxInputs =  new ArrayList<UIFormCheckBoxInput>();
            uiKeyEntitiesAdd.findComponentOfType(listCheckBoxInputs, UIFormCheckBoxInput.class);
            String attributeValue = "";
            for(UIFormCheckBoxInput uiFormCheckBoxInput : listCheckBoxInputs) 
            {
                if(uiFormCheckBoxInput.isChecked())
                { 
                    selectedKeys.add(uiFormCheckBoxInput.getValue().toString());
                    if (attributeValue.equals(""))
                    {
                        attributeValue = uiFormCheckBoxInput.getValue().toString();
                    }
                    else
                    {
                        attributeValue = attributeValue + "&" + uiFormCheckBoxInput.getValue();

                    }
                }
            }
            if(selectedKeys.size() == 0) 
            {
                UIApplication uiApplication = uiKeyEntitiesAdd.getAncestorOfType(UIApplication.class);
                uiApplication.addMessage(new ApplicationMessage("UIKeyEntitiesAdd.msg.noKeyEntitiesSelected", null));
                return;
            }
            Utils.setAttributeUserProfile(userName, "keyEntities", keyEntitiesAttributeValue + attributeValue);
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiKeyEntitiesAdd.getAncestorOfType(UIKeyEntitiesManagementPortlet.class);
            UIKeyEntitiesForm uiKeyEntitiesForm = uiKeyEntitiesManagementPortlet.getChild(UIKeyEntitiesForm.class);
            UIListKeyEntities uiListKeyEntities = uiKeyEntitiesForm.getChild(UIListKeyEntities.class);
            uiListKeyEntities.load(userName);
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesManagementPortlet);
            UIPopupWindow uiPopupWindow = uiKeyEntitiesManagementPortlet.findComponentById(UIKeyEntitiesManagementPortlet.KEY_ENTITIES_ADD_POPUP);
            uiPopupWindow.setShowMask(true);
            uiPopupWindow.setRendered(false);
        }
    }
}
