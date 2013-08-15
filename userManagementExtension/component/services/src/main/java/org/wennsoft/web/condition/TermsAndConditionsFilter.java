package org.wennsoft.web.condition;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileHandler;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.web.filter.Filter;

/**
 * @author MedAmine Krout
 */

public class TermsAndConditionsFilter implements Filter 
{
	private static Log logger = ExoLogger.getLogger(TermsAndConditionsFilter.class);
	private static final String INITIAL_URI_PARAM_NAME = "initialURI";
	private static final String REST_URI = ExoContainerContext.getCurrentContainer().getContext().getRestContextName();
    private static final String TERMS_AND_CONDITIONS_SERVLET_CTX = "/userManagementExtension";
    private static final String TERMS_AND_CONDITIONS_SERVLET_URL = "/termsAndConditionsView";

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException 
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
        OrganizationService organizationService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
        Identity identity = ConversationState.getCurrent().getIdentity();
        String userId = identity.getUserId();
        boolean accepted = false;
        boolean logged = false;
        if(!userId.equals("__anonim"))
        {
            logged=true;
            UserProfileHandler userProfileHandler = organizationService.getUserProfileHandler();
            try 
            {
                UserProfile userProfile = userProfileHandler.findUserProfileByName(userId);
                String tcAccepted = userProfile.getAttribute("tcAccepted");
                if (tcAccepted != null && tcAccepted.equals("true"))
                {
                    accepted = true;
                }
            } 
            catch (Exception exception) 
            {
                logger.error("User profile not found");
            }
        }
        String requestUri = httpServletRequest.getRequestURI();
        boolean isRestUri = requestUri.contains(REST_URI);
        if(!isRestUri && !accepted && logged) 
        {
            // Get full url
            String requestURI = httpServletRequest.getRequestURI();
            String queryString = httpServletRequest.getQueryString();
            if (queryString != null) 
            {
                requestURI += "?" + queryString;
            }
            // Get plf extension servlet context (because TermsAndConditionsFilter and wennsoft-terms-and-conditions servlet declaration are not on same context (webapp))
            ServletContext TandCScreensContext = httpServletRequest.getSession().getServletContext().getContext(TERMS_AND_CONDITIONS_SERVLET_CTX);
            // Forward to resource from this context: 
            String uriTarget = (new StringBuilder()).append(TERMS_AND_CONDITIONS_SERVLET_URL + "?" + INITIAL_URI_PARAM_NAME + "=").append(requestURI).toString();
            TandCScreensContext.getRequestDispatcher(uriTarget).forward(httpServletRequest, httpServletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}