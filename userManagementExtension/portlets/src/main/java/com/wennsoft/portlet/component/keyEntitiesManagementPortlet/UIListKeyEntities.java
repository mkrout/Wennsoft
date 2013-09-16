package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIGrid;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormHiddenInput;
import org.exoplatform.webui.form.UIFormInputSet;


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
            }
    )
@Serialized
public class UIListKeyEntities extends UIForm
{
	public final static String DELETE = "delete";
	public final static String TABLE_NAME =  "UIListKeyEntities";
    private static List<KeyEntity> listKeyEntities = new ArrayList<KeyEntity>();
    private static String userName;
    
    public List<KeyEntity> getListKeyEntities()
    {
        return listKeyEntities;
    }
    
    public void addAllListKeyEntities(List<KeyEntity> listKeyEntities_)
    {
    	listKeyEntities.addAll(listKeyEntities_);
    }
    
    public void init(String userName_) throws Exception
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
        UIFormHiddenInput uiFormHiddenInput = new UIFormHiddenInput("selectedKeysRemove", "");
        uiFormHiddenInput.setId("selectedKeysRemove");
        addChild(uiFormHiddenInput);
        String keyEntities = Utils.getAttributeUserProfile(userName, "keyEntities");
        if (keyEntities != null)
        {
            KeyEntity keyEntity;
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
            listKeyEntities.clear();
            uiListKeyEntities.setRendered(false);
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiListKeyEntities.getParent();
            UIListUsers uiListUsers = uiKeyEntitiesManagementPortlet.getChild(UIListUsers.class);
            uiListUsers.getChild(UIGrid.class).setRendered(true);
            event.getRequestContext().setProcessRender(true);
        }
    }
    
    public static class DeleteKeyEntitiesActionListener extends EventListener<UIListKeyEntities> {
        public void execute(Event<UIListKeyEntities> event) throws Exception {
        	UIListKeyEntities uiListKeyEntities = event.getSource();
        	List<KeyEntity> listUnSelectedKeyEntities = new ArrayList<KeyEntity>();
        	UIFormHiddenInput uiFormHiddenInput = uiListKeyEntities.getChild(UIFormHiddenInput.class);
            if (uiFormHiddenInput.getValue() != null && !uiFormHiddenInput.getValue().equals("")) 
            {
            	for (KeyEntity keyEntity : listKeyEntities)
            	{
            	    boolean add = true;
                    for (String selectedKeyEntity : uiFormHiddenInput.getValue().substring(1).split("@"))
            		{
            		    if (keyEntity.getKey().equals(selectedKeyEntity.split("/")[1]))
            		    {
            		        add = false;
            		    }
            	    }
                    if (add)
                    {
                        listUnSelectedKeyEntities.add(keyEntity);
                    }
                }
            }
            else
            {
                UIApplication uiApplication = uiListKeyEntities.getAncestorOfType(UIApplication.class);
                uiApplication.addMessage(new ApplicationMessage("UIKeyEntitiesAdd.msg.noKeyEntitiesSelected", null));
                return;
            }
            listKeyEntities = listUnSelectedKeyEntities;
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
        	listKeyEntities.clear();
        	UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiListKeyEntities.getParent();
            UIListUsers uiListUsers = uiKeyEntitiesManagementPortlet.getChild(UIListUsers.class);
            uiListUsers.getChild(UIGrid.class).setRendered(true);
        	uiListKeyEntities.setRendered(false);
        	Utils.setAttributeUserProfile(userName, "keyEntities", !keyEntities.equals("")?keyEntities.substring(1):keyEntities);
        }
    }
}
