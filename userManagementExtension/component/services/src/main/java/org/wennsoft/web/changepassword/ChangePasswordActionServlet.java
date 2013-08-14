package org.wennsoft.web.changepassword;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileHandler;
import org.exoplatform.services.resources.ResourceBundleService;

/**
 * @author Ayoub Zayati
 */

public class ChangePasswordActionServlet extends HttpServlet 
{
	private final static String CHANGE_PASSWORD_JSP_RESOURCE = "/WEB-INF/jsp/changePassword.jsp";
	private static final Log logger = ExoLogger.getLogger(ChangePasswordActionServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        ResourceBundleService resourceBundleService = (ResourceBundleService)PortalContainer.getInstance().getComponentInstanceOfType(ResourceBundleService.class);
        ResourceBundle resourceBundle = resourceBundleService.getResourceBundle(resourceBundleService.getSharedResourceBundleNames(), httpServletRequest.getLocale()) ;
        
        httpServletRequest.setAttribute("changePassword",resourceBundle.getString("userManagement.change.changePassword"));
        httpServletRequest.setAttribute("newPassword",resourceBundle.getString("userManagement.change.newPassword"));
        httpServletRequest.setAttribute("reNewPassword",resourceBundle.getString("userManagement.change.reNewPassword"));
        httpServletRequest.setAttribute("send",resourceBundle.getString("userManagement.change.send"));
        httpServletRequest.setAttribute("newPasswordError",resourceBundle.getString("userManagement.change.newPasswordError"));
        httpServletRequest.setAttribute("contextPath", httpServletRequest.getContextPath());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        String newPassword = httpServletRequest.getParameter("newPassword");
        String reNewPassword = httpServletRequest.getParameter("reNewPassword");
        OrganizationService organizationService = (OrganizationService)PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
        String userId = httpServletRequest.getRemoteUser();
        try 
        {
        	RequestLifeCycle.begin(PortalContainer.getInstance());
            User user = organizationService.getUserHandler().findUserByName(userId);
            if (!newPassword.equals(reNewPassword))
            {
                httpServletRequest.setAttribute("notValidNewPassword", "true");
                getServletContext().getRequestDispatcher(CHANGE_PASSWORD_JSP_RESOURCE).include(httpServletRequest, httpServletResponse);
            }
            else 
            {
                UserProfileHandler userProfileHandler = organizationService.getUserProfileHandler();
                UserProfile userProfile = userProfileHandler.findUserProfileByName(userId);
                userProfile.setAttribute("changePassword", "true");
                userProfileHandler.saveUserProfile(userProfile, true);
                user.setPassword(newPassword);
                organizationService.getUserHandler().saveUser(user, true);
                // Redirect to the home page
                String redirectURI = "/portal/";
                httpServletResponse.sendRedirect(redirectURI);
            }
        } 
        catch (Exception exception) 
        {
            logger.error("Password not changed");
        }
        finally
        {
            RequestLifeCycle.end();
        }
    }
   
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }
}