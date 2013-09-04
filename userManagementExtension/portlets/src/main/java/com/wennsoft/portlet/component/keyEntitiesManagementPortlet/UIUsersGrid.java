package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;


import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.util.ReflectionUtil;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPageIterator;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by The eXo Platform SARL Author : Tuan Nguyen tuan08@users.sourceforge.net May 7, 2006
 * <p/>
 * A grid element (represented by an HTML table) that can be paginated with a UIPageIterator
 *
 * @see org.exoplatform.webui.core.UIPageIterator
 */
@ComponentConfig(template = "app:/groovy/webui/component/keyEntitiesManagementPortlet/UIUsersGrid.gtmpl")
@Serialized
public class UIUsersGrid extends UIComponent {
    /** The page iterator */
    protected UIPageIterator uiIterator_;

    /** The bean field that holds the id of this bean */
    protected String beanIdField_;

    /** An array of String representing the fields in each bean */
    protected String[] beanField_;

    /** An array of String representing the actions on each bean */
    protected String[] action_;

    protected String label_;

    protected boolean useAjax = true;

    protected String selectedUser = "";

    protected int displayedChars_ = 30;

    public UIUsersGrid() throws Exception {
        uiIterator_ = createUIComponent(UIPageIterator.class, null, null);
        uiIterator_.setParent(this);
    }

    public UIPageIterator getUIPageIterator() {
        return uiIterator_;
    }

    public UIUsersGrid configure(String beanIdField, String[] beanField, String[] action) {
        this.beanIdField_ = beanIdField;
        this.beanField_ = beanField;
        this.action_ = action;
        return this;
    }

    public String getBeanIdField() {
        return beanIdField_;
    }

    public String[] getBeanFields() {
        return beanField_;
    }

    public String[] getBeanActions() {
        return action_;
    }

    public List<?> getBeans() throws Exception {
        return uiIterator_.getCurrentPageData();
    }

    public String getLabel() {
        return label_;
    }

    public void setLabel(String label) {
        label_ = label;
    }

    public Object getFieldValue(Object bean, String field) throws Exception {
        Method method = ReflectionUtil.getGetBindingMethod(bean, field);
        return method.invoke(bean, ReflectionUtil.EMPTY_ARGS);
    }

    public String getBeanIdFor(Object bean) throws Exception {
        return getFieldValue(bean, beanIdField_).toString();
    }

    @SuppressWarnings("unchecked")
    public UIComponent findComponentById(String lookupId) {
        if (uiIterator_.getId().equals(lookupId)) {
            return uiIterator_;
        }
        return super.findComponentById(lookupId);
    }

    public boolean isUseAjax() {
        return useAjax;
    }

    public void setUseAjax(boolean value) {
        useAjax = value;
    }

    public void setSelectedUser(String userName) {
        selectedUser = userName;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public int getDisplayedChars() {
        return displayedChars_;
    }

    public void setDisplayedChars(int displayedChars) {
        this.displayedChars_ = displayedChars;
    }
}

