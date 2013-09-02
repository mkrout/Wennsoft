package org.wennsoft.web.termsandconditions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileHandler;

/**
 * @author MedAmine Krout
 */

public class TermsAndConditionsActionServlet extends HttpServlet 
{
	private static Log logger = ExoLogger.getLogger(TermsAndConditionsActionServlet.class);
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
    	OrganizationService organizationService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
        String userId = httpServletRequest.getRemoteUser();
        UserProfileHandler userProfileHandler = organizationService.getUserProfileHandler();
        try 
        {
            RequestLifeCycle.begin(PortalContainer.getInstance());
            UserProfile userProfile = userProfileHandler.findUserProfileByName(userId);
            userProfile.setAttribute("tcAccepted", "true");
            userProfileHandler.saveUserProfile(userProfile, true);
            //Redirect to the account Setup
            String redirectURI = "/portal/";
            httpServletResponse.sendRedirect(redirectURI);
        } 
        catch (Exception exception) 
        {
            logger.error("User Profile not found or not successfully updated");
        }
        finally
        {
            RequestLifeCycle.end();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        doGet(httpServletRequest, httpServletResponse);
    }
}