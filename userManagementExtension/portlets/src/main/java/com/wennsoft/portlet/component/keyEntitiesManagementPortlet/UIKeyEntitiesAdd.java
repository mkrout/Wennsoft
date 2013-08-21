package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;


import java.util.ArrayList;
import java.util.List;


import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.UIFormInputInfo;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormTableInputSet;

/**
 * Created by KroutMedAmine

 */
@ComponentConfig(
        lifecycle = UIFormLifecycle.class,
        template =  "classpath:groovy/ecm/webui/form/UIForm.gtmpl",
        events = {
                @EventConfig(listeners = UIKeyEntitiesAdd.SaveActionListener.class),
                @EventConfig(phase=Phase.DECODE, listeners = UIKeyEntitiesAdd.CancelActionListener.class)
                }
)
public class UIKeyEntitiesAdd extends UIForm {

    final static String TABLE_NAME =  "UIKeyEntitiesAdd" ;
    final static String NUMBER = "number" ;
    final static String NAME = "name" ;
    final static String INPUT = "input" ;
    final static String[] TABLE_COLUMNS = {NAME, NUMBER, INPUT} ;

    private List<KeyEntity> keyEntitiesList_ = new ArrayList<KeyEntity>() ;

    public UIKeyEntitiesAdd() throws Exception {
    }

    public void update() throws Exception {
        UIFormTableInputSet uiTableInputSet = createUIComponent(UIFormTableInputSet.class, null, null) ;

        /******************************************************************/
        List<KeyEntity> keyEntities = new ArrayList<KeyEntity>();
        KeyEntity keyEntity = new KeyEntity("Customer", "Handy Gloves Inc.", "12-1031");
        keyEntities.add(keyEntity);
        keyEntity = new KeyEntity("Customer", "eXoplatform", "12-1032");
        keyEntities.add(keyEntity);
        keyEntity = new KeyEntity("Customer", "Capgemini", "12-1033");
        keyEntities.add(keyEntity);
        /******************************************************************/

        UIFormInputSet uiInputSet ;
        uiTableInputSet.setName(TABLE_NAME);
        uiTableInputSet.setColumns(TABLE_COLUMNS);
        for(int i = 0; i < keyEntities.size(); i++){
            KeyEntity keyEntity_ = keyEntities.get(i) ;
            keyEntitiesList_.add(keyEntity) ;
            String keyEntityName = keyEntity_.getKeyEntityName();
            String keyEntityNumber = keyEntity_.getKeyEntityNumber();
            uiInputSet = new UIFormInputSet(keyEntityName) ;
           UIFormInputInfo uiName = new UIFormInputInfo(NAME, null, keyEntityName);
            UIFormInputInfo uiNumber = new UIFormInputInfo(NUMBER, null, keyEntityNumber);
          uiInputSet.addChild(uiName);
            uiInputSet.addChild(uiNumber);
            UIFormCheckBoxInput<String> uiCheckbox = new UIFormCheckBoxInput<String>(keyEntityName, keyEntityName, null);
            uiCheckbox.setChecked(false);
            uiCheckbox.setValue(keyEntityName);
            uiInputSet.addChild(uiCheckbox);
            uiTableInputSet.addChild(uiInputSet);
        }
        setActions(new String[] {"Save", "Cancel"}) ;
        addUIFormInput(uiTableInputSet) ;
    }

    public KeyEntity getKeyEntityByName(String keyEntityName) throws Exception {
        KeyEntity keyEntitySelected = null ;
        for(KeyEntity keyEntity : keyEntitiesList_) {
            if(keyEntity.getKeyEntityName().equals(keyEntityName)) {
                keyEntitySelected = keyEntity ;
                break ;
            }
        }
        return keyEntitySelected ;
    }



    static public class SaveActionListener extends EventListener<UIKeyEntitiesAdd> {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception {
            UIKeyEntitiesAdd uiAdd = event.getSource() ;
            UIKeyEntitiesManagementPortlet uiManager = uiAdd.getAncestorOfType(UIKeyEntitiesManagementPortlet.class) ;
            UIApplication uiApp = uiAdd.getAncestorOfType(UIApplication.class) ;
            List<KeyEntity> selectedKeys = new ArrayList<KeyEntity>() ;
            List<UIFormCheckBoxInput> listCheckbox =  new ArrayList<UIFormCheckBoxInput>();
            uiAdd.findComponentOfType(listCheckbox, UIFormCheckBoxInput.class);
            for(int i = 0; i < listCheckbox.size(); i ++) {
                boolean checked = listCheckbox.get(i).isChecked() ;
               if(checked) selectedKeys.add(uiAdd.getKeyEntityByName(listCheckbox.get(i).getName())) ;
                if(checked) System.out.println(listCheckbox.get(i).getName()+"  added");
            }
            if(selectedKeys.size() == 0) {
                uiApp.addMessage(new ApplicationMessage("No keys entities selected", null)) ;
                return ;
            }

            event.getRequestContext().addUIComponentToUpdateByAjax(uiManager) ;
            UIPopupWindow uiPopup = uiManager.findComponentById(UIKeyEntitiesManagementPortlet.ADD_POPUP) ;
            uiPopup.setShowMask(true);
            uiPopup.setRendered(false) ;
        }
    }
    static public class CancelActionListener extends EventListener<UIKeyEntitiesAdd> {
        public void execute(Event<UIKeyEntitiesAdd> event) throws Exception {
            UIKeyEntitiesAdd uiAdd = event.getSource() ;
            UIKeyEntitiesManagementPortlet uiManager = uiAdd.getAncestorOfType(UIKeyEntitiesManagementPortlet.class) ;
            UIPopupWindow uiPopup = uiManager.findComponentById(UIKeyEntitiesManagementPortlet.ADD_POPUP) ;
            uiPopup.setShowMask(true);
            uiPopup.setRendered(false) ;
            event.getRequestContext().addUIComponentToUpdateByAjax(uiManager) ;
        }
    }


}
