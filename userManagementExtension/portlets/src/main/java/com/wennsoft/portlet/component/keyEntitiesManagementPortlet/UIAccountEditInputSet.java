package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.services.organization.User;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.EmailAddressValidator;
import org.exoplatform.webui.form.validator.MandatoryValidator;
import org.exoplatform.webui.form.validator.PersonalNameValidator;
import org.exoplatform.webui.form.validator.StringLengthValidator;
import org.exoplatform.webui.form.validator.UserConfigurableValidator;

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
        addUIFormInput(new UIFormStringInput(USERNAME, "userName", null).setEditable(false).addValidator(MandatoryValidator.class).addValidator(UserConfigurableValidator.class, UserConfigurableValidator.USERNAME));
        addUIFormInput(new UIFormStringInput("firstName", "firstName", null).setEditable(false).addValidator(StringLengthValidator.class, 1, 45).addValidator(MandatoryValidator.class).addValidator(PersonalNameValidator.class));
        addUIFormInput(new UIFormStringInput("lastName", "lastName", null).setEditable(false).addValidator(StringLengthValidator.class, 1, 45).addValidator(MandatoryValidator.class).addValidator(PersonalNameValidator.class));
        addUIFormInput(new UIFormStringInput("displayName", "fullName", null).setEditable(false).addValidator(StringLengthValidator.class, 0, 90).addValidator(UserConfigurableValidator.class, "displayname",
        UserConfigurableValidator.KEY_PREFIX + "displayname", false));
        addUIFormInput(new UIFormStringInput("email", "email", null).setEditable(false).addValidator(MandatoryValidator.class).addValidator(EmailAddressValidator.class));
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
