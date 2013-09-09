package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIGrid;
import org.exoplatform.webui.core.UISearch;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormInput;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;

@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "system:/groovy/webui/form/UISearchForm.gtmpl", events = @EventConfig(listeners = UISearchForm.QuickSearchActionListener.class))
@Serialized
public class UISearchForm extends UIForm {
	
	public static final String ADVANCED_SEARCH_SET = "AdvancedSearchSet";
	public static final String QUICK_SEARCH_SET = "QuickSearchSet";

    public UISearchForm() 
    {
        UIFormInputSet uiQuickSearchSet = new UIFormInputSet(QUICK_SEARCH_SET);
        uiQuickSearchSet.addUIFormInput(new UIFormStringInput("searchTerm", null, null));
        uiQuickSearchSet.addUIFormInput(new UIFormSelectBox("searchOption", null, null));
        addChild(uiQuickSearchSet);
        UIFormInputSet uiAdvancedSearchSet = new UIFormInputSet(ADVANCED_SEARCH_SET);
        addChild(uiAdvancedSearchSet);
        uiAdvancedSearchSet.setRendered(false);
    }

    public void setOptions(List<SelectItemOption<String>> options) 
    {
        UIFormSelectBox uiSelect = (UIFormSelectBox) getQuickSearchInputSet().getChild(1);
        uiSelect.setOptions(options);
    }

    public UIFormInputSet getQuickSearchInputSet() 
    {
        return (UIFormInputSet) getChild(0);
    }

    public UIFormInputSet getAdvancedSearchInputSet() 
    {
        return (UIFormInputSet) getChild(1);
    }

    public void addAdvancedSearchInput(UIFormInput input) 
    {
        getAdvancedSearchInputSet().addUIFormInput(input);
    }

    public static class QuickSearchActionListener extends EventListener<UISearchForm> 
    {
        public void execute(Event<UISearchForm> event) throws Exception 
        {
            UISearchForm uiForm = event.getSource();
            UIListUsers uiListUsers = uiForm.getParent();
            uiListUsers.quickSearch(uiForm.getQuickSearchInputSet());
            uiListUsers.getChild(UIGrid.class).setRendered(true);
            UIKeyEntitiesManagementPortlet uiKeyEntitiesManagementPortlet = uiListUsers.getParent();
            uiKeyEntitiesManagementPortlet.getChild(UIListKeyEntities.class).setRendered(false);
            event.getRequestContext().addUIComponentToUpdateByAjax(uiKeyEntitiesManagementPortlet);
        }
    }

    public static class ShowAdvancedSearchActionListener extends EventListener<UISearchForm> 
    {
        public void execute(Event<UISearchForm> event) throws Exception 
        {
            UISearchForm uiForm = event.getSource();
            UISearch uiSearch = uiForm.getParent();
            event.getRequestContext().addUIComponentToUpdateByAjax(uiSearch);
        }
    }

    public static class CancelAdvancedSearchActionListener extends EventListener<UISearchForm> 
    {
        public void execute(Event<UISearchForm> event) throws Exception 
        {
            UISearchForm uiForm = event.getSource();
            UISearch uiSearch = uiForm.getParent();
            event.getRequestContext().addUIComponentToUpdateByAjax(uiSearch);
        }
    }
}
