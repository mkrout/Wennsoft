package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.form.UIFormInputSet;

@Serialized
@ComponentConfig()
public abstract class UISearch extends UIContainer 
{
    public UISearch(List<SelectItemOption<String>> searchOption) throws Exception 
    {
        UISearchForm uiForm = addChild(UISearchForm.class, null, null);
        uiForm.setOptions(searchOption);
    }

    public UISearchForm getUISearchForm() 
    {
        return (UISearchForm) getChild(0);
    }

    public abstract void quickSearch(UIFormInputSet quickSearchInput) throws Exception;

    public abstract void advancedSearch(UIFormInputSet advancedSearchInput);
}
