package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.commons.utils.SerializablePageList;
import org.exoplatform.portal.webui.container.UIContainer;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
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
                                @EventConfig(listeners = UIListKeyEntities.SelectBoxActionListener.class, phase=Phase.DECODE),
                                @EventConfig(listeners = UIListKeyEntities.ShowPageActionListener.class),
                @EventConfig(listeners = UIListKeyEntities.SaveActionListener.class),
                                @EventConfig(listeners = UIListKeyEntities.BackActionListener.class, phase = Phase.ANY),
                @EventConfig(listeners = UIListKeyEntities.AddKeyEntitiesActionListener.class, phase = Phase.DECODE)
                        }
        )
@Serialized
public class UIListKeyEntities extends UIForm
{
    public final static String TABLE_NAME =  "UIListKeyEntities";
    public final static String DELETE = "delete";
    private static String userName;
    private List<String> selectedKeys = new ArrayList<String>();
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
    private List<String> getSelectedKeys()
    {
        return Collections.unmodifiableList(selectedKeys);
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

        UIFormTableIteratorInputSet uiFormTableInputSet = createUIComponent(UIFormTableIteratorInputSet.class, null, TABLE_NAME);

        List<UIFormInputSet> uiFormInputSetList = new ArrayList<UIFormInputSet>();
        UICheckBoxInput uiCheckBoxInput;
        String[] columnsTable = {"connectId","key", DELETE};
        UIFormInputSet uiFormInputSet;
        removeChild(UIFormTableIteratorInputSet.class);
        uiFormTableInputSet.setName(TABLE_NAME);
        uiFormTableInputSet.setColumns(columnsTable);
        addChild(uiFormTableInputSet);
        List<KeyEntity> listKeyEntities = new ArrayList<KeyEntity>();
        String keyEntities = Utils.getAttributeUserProfile(userName, "keyEntities");
        KeyEntity keyEntity;
        if (keyEntities != null)
        {
            String[] splittedKeyEntities = keyEntities.split("@");
            int count = 0;
            for (String splittedKeyEntity : splittedKeyEntities)
            {
                if (!splittedKeyEntity.equals(""))
                {
                    keyEntity = new KeyEntity(String.valueOf(count), splittedKeyEntity.split("/")[0], splittedKeyEntity.split("/")[1]);
                    listKeyEntities.add(keyEntity);
                    count++;
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

    static public class SelectBoxActionListener extends EventListener<UIListKeyEntities>
    {
        @Override
        public void execute(Event<UIListKeyEntities> event) throws Exception
        {
            UIListKeyEntities UIListKeyEntities = event.getSource();
            UIFormTableIteratorInputSet uiFormTableIteratorInputSet = UIListKeyEntities.getChild(UIFormTableIteratorInputSet.class);
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
                        if (!UIListKeyEntities.getSelectedKeys().contains(uiCheckBoxInput.getName()))
                        {
                            UIListKeyEntities.addSelectedKey(uiCheckBoxInput.getName());
                        }
                    }
                    else
                    {
                        if (UIListKeyEntities.getSelectedKeys().contains(uiCheckBoxInput.getName()))
                        {
                            UIListKeyEntities.removeUnSelectedKey(uiCheckBoxInput.getName());
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
            UIListKeyEntities UIListKeyEntities = event.getSource();
            UICheckBoxInput uiCheckBoxInput;
            for (String key : UIListKeyEntities.getSelectedKeys())
            {
                uiCheckBoxInput = UIListKeyEntities.findComponentById(key);
                uiCheckBoxInput.setChecked(true);
            }
            event.getRequestContext().addUIComponentToUpdateByAjax(UIListKeyEntities);
        }
    }

    public static class SaveActionListener extends EventListener<UIListKeyEntities> {
        public void execute(Event<UIListKeyEntities> event) throws Exception {
            System.out.println("Save action");
        /*    UIListKeyEntities uiListKeyEntities = event.getSource();
            OrganizationService service = uiListKeyEntities.getApplicationComponent(OrganizationService.class);
            boolean save = uiListKeyEntities.getChild(UIAccountEditInputSet.class).save(service);
            if (!save) {
                return;
            }

            UIAccountEditInputSet accountInput = uiUserInfo.getChild(UIAccountEditInputSet.class);
            //UIUserProfileInputSet userProfile = uiUserInfo.getChild(UIUserProfileInputSet.class);
            uiUserInfo.setRenderSibling(UIListUsers.class);
            accountInput.reset();
            //userProfile.reset();
            event.getRequestContext().setProcessRender(true); */
        }
    }

    public static class BackActionListener extends EventListener<UIListKeyEntities> {
        public void execute(Event<UIListKeyEntities> event) throws Exception {
            UIListKeyEntities uiListKeyEntities = event.getSource();
            UIAccountEditInputSet accountInput = uiListKeyEntities.getChild(UIAccountEditInputSet.class);
            //UIUserProfileInputSet userProfile = userInfo.getChild(UIUserProfileInputSet.class);
            uiListKeyEntities.setRenderSibling(UIListUsers.class);
            accountInput.reset();
            // userProfile.reset();
            event.getRequestContext().setProcessRender(true);

        }
    }

    public static class AddKeyEntitiesActionListener extends EventListener<UIListKeyEntities>
    {
        public void execute(Event<UIListKeyEntities> event) throws Exception
        {
            UIListKeyEntities uiListKeyEntities = event.getSource();
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiListKeyEntities.getParent();
            uiKeyEntitiesManagementPortlet.setAddPopup(userName);
        }
    }

}
