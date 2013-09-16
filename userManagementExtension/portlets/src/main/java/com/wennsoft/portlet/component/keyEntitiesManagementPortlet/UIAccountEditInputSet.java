package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.services.organization.User;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormStringInput;

@Serialized
public class UIAccountEditInputSet extends UIFormInputSet 
{
	static final String USERNAME = "userName";

    public UIAccountEditInputSet() 
    {
    }

    public UIAccountEditInputSet(String name) throws Exception 
    {
        super(name);
        addUIFormInput(new UIFormStringInput(USERNAME, "userName", null).setEditable(false));
        addUIFormInput(new UIFormStringInput("firstName", "firstName", null).setEditable(false));
        addUIFormInput(new UIFormStringInput("lastName", "lastName", null).setEditable(false));
        addUIFormInput(new UIFormStringInput("displayName", "fullName", null).setEditable(false));
        addUIFormInput(new UIFormStringInput("email", "email", null).setEditable(false));
    }

    public void setValue(User user) throws Exception 
    {
        if (user == null) 
        {
            return;
        }
        invokeGetBindingField(user);
    }
}
