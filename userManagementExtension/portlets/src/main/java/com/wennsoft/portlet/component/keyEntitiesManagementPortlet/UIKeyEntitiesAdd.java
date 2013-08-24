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
    private static String product;
    private static String userName;


    public void init(String userName_) throws Exception
    {
        userName = userName_;
        UIFormSelectBox uiFormProductSelectBox = new UIFormSelectBox(PRODUCTS, null, null);
        initProductSelectBox(uiFormProductSelectBox);
        uiFormProductSelectBox.setOnChange(PRODUCTS_ONCHANGE);
        setActions(new String[] {"Save", "Cancel"}) ;
        addUIFormInput(uiFormProductSelectBox) ;
        update();
    } 

    public void update() throws Exception 
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
            customer = new HashMap();
            customer.put("Customer Name","eXoplatform1");
            customer.put("Customer Number", "12-1032");
            customers.add(customer);
            customer = new HashMap();
            customer.put("Customer Name","Capgemini1");
            customer.put("Customer Number","12-1033");
            customers.add(customer);
            String[] TABLE_COLUMNS = new String [customer.size()+1];
             int i=0;
            for (Map.Entry<String, String> entry : customer.entrySet()) {
                TABLE_COLUMNS[i]= entry.getKey();
                i++;
            }
            TABLE_COLUMNS[i]="Input";
            UIFormInputSet uiInputSet;
            removeChild(UIFormTableInputSet.class);
            uiFormTableInputSet.setName(TABLE_NAME);
            uiFormTableInputSet.setColumns(TABLE_COLUMNS);

            for( Map<String, String> customer_ : customers)
            {
                String keyValue= product+"/" ;
                uiInputSet = new UIFormInputSet(TABLE_COLUMNS[0]);

                for (Map.Entry<String, String> entry : customer_.entrySet()) {
                    uiInputSet.addChild(new UIFormInputInfo(entry.getKey(), null, entry.getValue()));
                    keyValue=keyValue+":"+entry.getKey()+";"+entry.getValue();
                }
                UIFormCheckBoxInput<String> uiCheckbox = new UIFormCheckBoxInput<String>(keyValue, keyValue, null);
                uiCheckbox.setChecked(false);
                uiCheckbox.setValue(keyValue);
                uiInputSet.addChild(uiCheckbox);
                uiFormTableInputSet.addChild(uiInputSet);
            }
    	}
    	if (product!=null && product.equals("vendor"))
    	{
            Map<String, String> vendor = new HashMap<String, String>();
            vendor.put("Vendor Number","SAV-1200");
            vendor.put("Vendor Branch","MKE");
            vendor.put("Vendor Name","Gloves");
            vendors.add(vendor);
            vendor = new HashMap();
            vendor.put("Vendor Number","SAV-1202");
            vendor.put("Vendor Branch", "AAA");
            vendor.put("Vendor Name","eXoplatform1");
            vendors.add(vendor);
            vendor = new HashMap();
            vendor.put("Vendor Number","SAV-1204");
            vendor.put("Vendor Branch","FFF");
            vendor.put("Vendor Name","Capgemini1");
            vendors.add(vendor);
            vendor = new HashMap();
            vendor.put("Vendor Number","SAV-1300");
            vendor.put("Vendor Branch","ZZZ");
            vendor.put("Vendor Name","Handy");
            vendors.add(vendor);
            String[] TABLE_COLUMNS = new String [vendor.size()+1];
            int i=0;
            for (Map.Entry<String, String> entry : vendor.entrySet()) {
                TABLE_COLUMNS[i]= entry.getKey();
                i++;
            }
            TABLE_COLUMNS[i]="Input";
            UIFormInputSet uiInputSet;
            removeChild(UIFormTableInputSet.class);
            uiFormTableInputSet.setName(TABLE_NAME);
            uiFormTableInputSet.setColumns(TABLE_COLUMNS);

            for( Map<String, String> vendor_ : vendors)
            {
                String keyValue= product+"/" ;
                uiInputSet = new UIFormInputSet(TABLE_COLUMNS[0]);

                for (Map.Entry<String, String> entry : vendor_.entrySet()) {
                    uiInputSet.addChild(new UIFormInputInfo(entry.getKey(), null, entry.getValue()));
                    keyValue=keyValue+entry.getKey()+":"+entry.getValue()+";";
                }
                UIFormCheckBoxInput<String> uiCheckbox = new UIFormCheckBoxInput<String>(keyValue, keyValue, null);
                uiCheckbox.setChecked(false);
                uiCheckbox.setValue(keyValue);
                uiInputSet.addChild(uiCheckbox);
                uiFormTableInputSet.addChild(uiInputSet);
            }
        }

        addUIFormInput(uiFormTableInputSet);
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
            List<String> selectedKeys = new ArrayList<String>();
            List<UIFormCheckBoxInput> listCheckbox =  new ArrayList<UIFormCheckBoxInput>();
            uiKeyEntitiesAdd.findComponentOfType(listCheckbox, UIFormCheckBoxInput.class);
            String attributeValue ="";
            for(UIFormCheckBoxInput checkbox : listCheckbox) 
            {
                if(checkbox.isChecked())
                { 
                   selectedKeys.add(checkbox.getValue().toString());
                    System.out.println(checkbox.getValue()+"  added");
                    if (attributeValue.equals(""))
                    {
                        attributeValue=checkbox.getValue().toString();
                    }
                    else
                    {
                        attributeValue=attributeValue+"&"+checkbox.getValue();

                    }
                }
            }
            if(selectedKeys.size() == 0) 
            {
               uiApplication.addMessage(new ApplicationMessage("No keys entities selected", null));
               return;
            }
            System.out.println(attributeValue);
            String rootatt = Utils.getAttributeUserProfile("root", "keyEntities") ;
            String amineatt = Utils.getAttributeUserProfile("amine", "keyEntities");

            Utils.setAttributeUserProfile("root", "keyEntities", rootatt+attributeValue);
            Utils.setAttributeUserProfile("amine","keyEntities",amineatt+attributeValue);
            UIKeyEntitiesForm uiKeyEntitiesForm = uiKeyEntitiesManagementPortlet.getChild(UIKeyEntitiesForm.class);
            UIListKeyEntities uiListKeyEntities = uiKeyEntitiesForm.getChild(UIListKeyEntities.class);
            uiListKeyEntities.load("root");
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesManagementPortlet);
            event.getRequestContext().addUIComponentToUpdateByAjax(uiListKeyEntities);
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
        option = new SelectItemOption<String>("Vendor Connect", "vendor");
        product_.add(option);
        productSelectBox.setOptions(product_);
        productSelectBox.setValue(option.getValue());
        product = option.getValue();
    } 
}
