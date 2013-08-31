package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.UIFormInputInfo;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormPageIterator;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.json.JSONObject;


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
    public final static String PRODUCT = "product";
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
        UIFormTableIteratorInputSet uiFormTableInputSet = createUIComponent(UIFormTableIteratorInputSet.class, null, TABLE_NAME);
        List<UIFormInputSet> uiFormInputSetList = new ArrayList<UIFormInputSet>();
        if (product!=null && product.equals("customer"))
        {

            String[] columnsTable = {"CustomerNumber","CustomerName",INCLUDE};
            /*int count = 0;
            for (Map.Entry<String, String> entry : customer.entrySet())
            {
                columnsTable[count]= entry.getKey();
                count++;
            }
            columnsTable[customer.size()] = INCLUDE;  */

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

            data= Utils.getEntitiesList(bridgeUri, controller, parameters);

            Map<String, Object> entitiesList =new HashMap<String, Object>(Utils.convertJsonString(data));

            List<Object> entities = (List <Object>) entitiesList.get("aaData");
            for  (Object entity : entities)
            {
                String keyValue = product + "/" ;
                uiFormInputSet = new UIFormInputSet(columnsTable[0]);
                ArrayList <String> entity_=  (ArrayList<String>) entity;
                Iterator<String> it = entity_.iterator();
                int i=0;
                while (it.hasNext()) {
                    String value = it.next();
                    uiFormInputSet.addChild(new UIFormInputInfo(columnsTable[i], null, value));
                    keyValue=keyValue + columnsTable[i] + ":" + value+ ";";
                }
                UIFormCheckBoxInput<String> uiFormCheckBoxInput = new UIFormCheckBoxInput<String>(keyValue, keyValue, null);
                uiFormCheckBoxInput.setChecked(false);
                uiFormCheckBoxInput.setValue(keyValue);
                if (keyEntitiesAttributeValue.contains(keyValue))
                {
                    uiFormCheckBoxInput.setDisabled(true);
                }
                uiFormInputSet.addChild(uiFormCheckBoxInput);
                uiFormInputSetList.add(uiFormInputSet);
                uiFormTableInputSet.addChild(uiFormInputSet);
            }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
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
            vendor.put("Vendor Number","SAV-1201");
            vendor.put("Vendor Branch", "AAA");
            vendor.put("Vendor Name","eXoplatform1");
            vendors.add(vendor);
            vendor = new HashMap<String, String>();
            vendor.put("Vendor Number","SAV-1202");
            vendor.put("Vendor Branch","FFF");
            vendor.put("Vendor Name","Capgemini1");
            vendors.add(vendor);
            vendor = new HashMap<String, String>();
            vendor.put("Vendor Number","SAV-1203");
            vendor.put("Vendor Branch","ZZZ");
            vendor.put("Vendor Name","Han & dy");
            vendors.add(vendor);
            String[] columnsTable = new String [vendor.size()+1];
            int count = 0;
            for (Map.Entry<String, String> entry : vendor.entrySet()) {
                columnsTable[count]= entry.getKey();
                count++;
            }
            columnsTable[vendor.size()] = INCLUDE;
            UIFormInputSet uiFormInputSet;
            removeChild(UIFormTableIteratorInputSet.class);
            uiFormTableInputSet.setName(TABLE_NAME);
            uiFormTableInputSet.setColumns(columnsTable);
            addChild(uiFormTableInputSet);
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
                {
                    uiFormCheckBoxInput.setDisabled(true);
                }
                uiFormInputSet.addChild(uiFormCheckBoxInput);
                uiFormInputSetList.add(uiFormInputSet);
                uiFormTableInputSet.addChild(uiFormInputSet);

            }
        }
        UIFormPageIterator uiIterator = uiFormTableInputSet.getChild(UIFormPageIterator.class);
        SerializablePageList<UIFormInputSet> pageList = new SerializablePageList<UIFormInputSet>(UIFormInputSet.class,uiFormInputSetList, 2);
        uiIterator.setPageList(pageList);
    }

    public void init(String userName_) throws Exception
    {
        userName = userName_;
        keyEntitiesAttributeValue = Utils.getAttributeUserProfile(userName, "keyEntities") != null && !Utils.getAttributeUserProfile(userName, "keyEntities").equals("")?Utils.getAttributeUserProfile(userName, "keyEntities") + "@":"";
        UIFormSelectBox uiFormProductSelectBox = new UIFormSelectBox(PRODUCT, null, null);
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
            UIFormSelectBox productSelection = uiKeyEntitiesAdd.getUIFormSelectBox(PRODUCT);
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
                        attributeValue = attributeValue + "@" + uiFormCheckBoxInput.getValue();

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
