package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.form.UIFormInputBase;
import org.exoplatform.webui.form.UIFormPageIterator;
import org.exoplatform.webui.form.UIFormTableInputSet;

@ComponentConfig(template = "app:/groovy/webui/component/keyEntitiesManagementPortlet/UIFormTableIteratorInputSet.gtmpl")
@Serialized
public class UIFormTableIteratorInputSet extends UIFormTableInputSet 
{
    UIFormPageIterator uiIterator_;
    public UIFormTableIteratorInputSet() throws Exception 
    {
        uiIterator_ = createUIComponent(UIFormPageIterator.class, null, null);
        addChild(uiIterator_);
    }

    public UIFormPageIterator getUIFormPageIterator() 
    {
        return uiIterator_;
    }

    @SuppressWarnings("unchecked")
    public UIComponent findComponentById(String lookupId) 
    {
        if (uiIterator_.getId().equals(lookupId))
        {	
            return uiIterator_;
        }    
        return super.findComponentById(lookupId);
    }

    @SuppressWarnings("unchecked")
    public void processDecode(WebuiRequestContext context) throws Exception 
    {
        List<UIComponent> children = uiIterator_.getCurrentPageData();
        for (UIComponent child : children) 
        {
            List<UIFormInputBase> inputs = new ArrayList<UIFormInputBase>();
            child.findComponentOfType(inputs, UIFormInputBase.class);
            for (UIFormInputBase input : inputs) {
                String inputValue = context.getRequestParameter(input.getId());
                if (inputValue == null || inputValue.trim().length() == 0) 
                {
                    inputValue = context.getRequestParameter(input.getName());
                }
                input.decode(inputValue, context);
            }
            child.processDecode(context);
        }
    }
}
