package com.wennsoft.portlet.component.keyEntitiesManagementPortlet;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileHandler;

public class Utils 
{
    
    public static String getAttributeUserProfile(String userId, String attribute) throws Exception
    {
        OrganizationService organizationService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
        UserProfileHandler userProfileHandler = organizationService.getUserProfileHandler();
        UserProfile userProfile = userProfileHandler.findUserProfileByName(userId);
        return userProfile.getAttribute(attribute);
    }

    public static void setAttributeUserProfile(String userId, String attribute, String attributeValue) throws Exception
    {
    	RequestLifeCycle.begin(PortalContainer.getInstance());
    	OrganizationService organizationService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
        UserProfileHandler userProfileHandler = organizationService.getUserProfileHandler();
        UserProfile userProfile = userProfileHandler.createUserProfileInstance(userId);
        userProfile.setAttribute(attribute, attributeValue);
        userProfileHandler.saveUserProfile(userProfile, false);
        RequestLifeCycle.end();
    }
}