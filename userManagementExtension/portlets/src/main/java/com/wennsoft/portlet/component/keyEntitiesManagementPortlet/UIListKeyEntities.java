package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.commons.utils.SerializablePageList;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIGrid;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormInputInfo;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormPageIterator;
import org.exoplatform.webui.form.input.UICheckBoxInput;


/**
 * @author MedAmine Krout
 */
@ComponentConfig
    (
        lifecycle = UIFormLifecycle.class,
        template =  "app:/groovy/webui/component/keyEntitiesManagementPortlet/UIListKeyEntities.gtmpl",
        events =
            {
        	    @EventConfig(listeners = UIListKeyEntities.AddKeyEntitiesActionListener.class, phase = Phase.DECODE),
        	    @EventConfig(listeners = UIListKeyEntities.BackActionListener.class, phase = Phase.ANY),
        	    @EventConfig(listeners = UIListKeyEntities.DeleteKeyEntitiesActionListener.class, phase = Phase.DECODE),
        	    @EventConfig(listeners = UIListKeyEntities.SaveActionListener.class),
                @EventConfig(listeners = UIListKeyEntities.SelectBoxActionListener.class, phase=Phase.DECODE),
                @EventConfig(listeners = UIListKeyEntities.ShowPageActionListener.class),
                
            }
    )
@Serialized
public class UIListKeyEntities extends UIForm
{
    public final static String TABLE_NAME =  "UIListKeyEntities";
    public final static String DELETE = "delete";
    private static String userName;
    private List<String> selectedKeys = new ArrayList<String>();
    private static List<KeyEntity> listKeyEntities = new ArrayList<KeyEntity>();
    
    private List<String> getSelectedKeys()
    {
        return Collections.unmodifiableList(selectedKeys);
    }
    
    private void addSelectedKey(String key)
    {
        selectedKeys.add(key);
    }
    
    private void clearSelectedKeys()
    {
        selectedKeys.clear();
    }
    
    private void removeUnSelectedKey(String key)
    {
        selectedKeys.remove(key);
    }
    
    public void addAllListKeyEntities(List<KeyEntity> listKeyEntities_)
    {
    	listKeyEntities.addAll(listKeyEntities_);
    }
    
    public List<KeyEntity> getListKeyEntities()
    {
        return listKeyEntities;
    }
    
    public void init(String userName_, List<KeyEntity> listSelectedKeyEntities) throws Exception
    {
        userName = userName_;
        UIFormInputSet accountInputSet = getChild(UIFormInputSet.class);
        if (accountInputSet != null)
        {
            removeChild(UIFormInputSet.class);
        }
        addChild(new UIAccountEditInputSet("UIAccountEditInputSet"));
        OrganizationService service = getApplicationComponent(OrganizationService.class);
        User user = service.getUserHandler().findUserByName(userName);
        getChild(UIAccountEditInputSet.class).setValue(user);
        UIFormTableIteratorInputSet uiFormTableInputSet = createUIComponent(UIFormTableIteratorInputSet.class, null, TABLE_NAME);
        List<UIFormInputSet> uiFormInputSetList = new ArrayList<UIFormInputSet>();
        UICheckBoxInput uiCheckBoxInput;
        String[] columnsTable = {"connectId","key", DELETE};
        UIFormInputSet uiFormInputSet;
        removeChild(UIFormTableIteratorInputSet.class);
        uiFormTableInputSet.setName(TABLE_NAME);
        uiFormTableInputSet.setColumns(columnsTable);
        addChild(uiFormTableInputSet);
        if (listSelectedKeyEntities == null)
        {	
            String keyEntities = Utils.getAttributeUserProfile(userName, "keyEntities");
            KeyEntity keyEntity;
            if (keyEntities != null)
            {
                String[] splittedKeyEntities = keyEntities.split("@");
                for (String splittedKeyEntity : splittedKeyEntities)
                {
                    if (!splittedKeyEntity.equals(""))
                    {
                        keyEntity = new KeyEntity(splittedKeyEntity.split("/")[0], splittedKeyEntity.split("/")[1]);
                        listKeyEntities.add(keyEntity);
                    }
                }
            }
        }
        for (KeyEntity keyEntity_ : listKeyEntities)
        {
            uiFormInputSet = new UIFormInputSet(columnsTable[0]);
            uiFormInputSet.addChild(new UIFormInputInfo(columnsTable[0], null, keyEntity_.getConnectId()));
            uiFormInputSet.addChild(new UIFormInputInfo(columnsTable[1], null, keyEntity_.getKey()));
            uiCheckBoxInput = new UICheckBoxInput(keyEntity_.getKey(), keyEntity_.getKey(), false);
            uiCheckBoxInput.setOnChange("SelectBox");
            uiFormInputSet.addChild(uiCheckBoxInput);
            uiFormInputSetList.add(uiFormInputSet);
            uiFormTableInputSet.addChild(uiFormInputSet);
        }
        UIFormPageIterator uiIterator = uiFormTableInputSet.getChild(UIFormPageIterator.class);
        SerializablePageList<UIFormInputSet> pageList = new SerializablePageList<UIFormInputSet>(UIFormInputSet.class, uiFormInputSetList, 3);
        uiIterator.setPageList(pageList);
        setActions(new String[] { "AddKeyEntities", "Save", "Back" });
    }

    public static class AddKeyEntitiesActionListener extends EventListener<UIListKeyEntities>
    {
        public void execute(Event<UIListKeyEntities> event) throws Exception
        {
            UIListKeyEntities uiListKeyEntities = event.getSource();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiListKeyEntities.getParent();
            uiKeyEntitiesManagementPortlet.setAddPopup(userName, listKeyEntities);
        }
    }
    
    public static class BackActionListener extends EventListener<UIListKeyEntities> {
        public void execute(Event<UIListKeyEntities> event) throws Exception {
            UIListKeyEntities uiListKeyEntities = event.getSource();
            uiListKeyEntities.clearSelectedKeys();
            listKeyEntities.clear();
            //UIAccountEditInputSet accountInput = uiListKeyEntities.getChild(UIAccountEditInputSet.class);
            //UIUserProfileInputSet userProfile = userInfo.getChild(UIUserProfileInputSet.class);
            uiListKeyEntities.setRendered(false);
            //accountInput.reset();
            // userProfile.reset();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiListKeyEntities.getParent();
            UIListUsers uiListUsers = uiKeyEntitiesManagementPortlet.getChild(UIListUsers.class);
            uiListUsers.getChild(UIGrid.class).setRendered(true);
            event.getRequestContext().setProcessRender(true);
        }
    }
    
    public static class DeleteKeyEntitiesActionListener extends EventListener<UIListKeyEntities> {
        public void execute(Event<UIListKeyEntities> event) throws Exception {
        	UIListKeyEntities uiListKeyEntities = event.getSource();
        	List<KeyEntity> listSelectedKeyEntities = new ArrayList<KeyEntity>();
        	for (KeyEntity keyEntity : listKeyEntities)
        	{
                for (String selectedKeyEntity : uiListKeyEntities.getSelectedKeys())
        		{
        		    if (keyEntity.getKey().equals(selectedKeyEntity))
        		    {
        		        listSelectedKeyEntities.add(keyEntity);
        		    	break;
        		    }
        		}
        	}
        	listKeyEntities.removeAll(listSelectedKeyEntities);
        	uiListKeyEntities.init(userName, listSelectedKeyEntities);
        	uiListKeyEntities.clearSelectedKeys();
        	event.getRequestContext().addUIComponentToUpdateByAjax(uiListKeyEntities);
        }
    }
    
    public static class SaveActionListener extends EventListener<UIListKeyEntities> {
        public void execute(Event<UIListKeyEntities> event) throws Exception {
        	UIListKeyEntities uiListKeyEntities = event.getSource();
        	String keyEntities = new String();
        	for (KeyEntity keyEntity : listKeyEntities)
        	{
        		keyEntities += "@" + keyEntity.getConnectId() + "/" + keyEntity.getKey();
        	}
        	uiListKeyEntities.clearSelectedKeys();
        	listKeyEntities.clear();
        	UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiListKeyEntities.getParent();
            UIListUsers uiListUsers = uiKeyEntitiesManagementPortlet.getChild(UIListUsers.class);
            uiListUsers.getChild(UIGrid.class).setRendered(true);
        	uiListKeyEntities.setRendered(false);
        	Utils.setAttributeUserProfile(userName, "keyEntities", !keyEntities.equals("")?keyEntities.substring(1):keyEntities);
        	//event.getRequestContext().addUIComponentToUpdateByAjax(uiListKeyEntities);
        }
    }
    
    static public class SelectBoxActionListener extends EventListener<UIListKeyEntities>
    {
        @Override
        public void execute(Event<UIListKeyEntities> event) throws Exception
        {
            UIListKeyEntities uiListKeyEntities = event.getSource();
            UIFormTableIteratorInputSet uiFormTableIteratorInputSet = uiListKeyEntities.getChild(UIFormTableIteratorInputSet.class);
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
                        if (!uiListKeyEntities.getSelectedKeys().contains(uiCheckBoxInput.getName()))
                        {
                        	uiListKeyEntities.addSelectedKey(uiCheckBoxInput.getName());
                        }
                    }
                    else
                    {
                        if (uiListKeyEntities.getSelectedKeys().contains(uiCheckBoxInput.getName()))
                        {
                        	uiListKeyEntities.removeUnSelectedKey(uiCheckBoxInput.getName());
                        }
                    }
                }
            }
        }
    }
    
    public static class ShowPageActionListener extends EventListener<UIListKeyEntities>
    {
        public void execute(Event<UIListKeyEntities> event) throws Exception
        {
            UIListKeyEntities uiListKeyEntities = event.getSource();
            UICheckBoxInput uiCheckBoxInput;
            for (String key : uiListKeyEntities.getSelectedKeys())
            {
                uiCheckBoxInput = uiListKeyEntities.findComponentById(key);
                uiCheckBoxInput.setChecked(true);
            }
            event.getRequestContext().addUIComponentToUpdateByAjax(uiListKeyEntities);
        }
    }
}
