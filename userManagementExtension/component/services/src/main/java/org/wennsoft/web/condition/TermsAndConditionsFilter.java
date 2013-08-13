package org.wennsoft.web.condition;

import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.web.filter.Filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileHandler;


/**
 * @author MedAmine Krout
 */

public class TermsAndConditionsFilter implements Filter {
  private static Log logger = ExoLogger.getLogger(TermsAndConditionsFilter.class);
  


  private static final String WENNSOFT_TandC_SCREENS_SERVLET_CTX = "/userManagementExtension";
  private static final String TC_SERVLET_URL = "/wennsoft-terms-and-conditions";
  private static final String INITIAL_URI_PARAM_NAME = "initialURI";
  private static String REST_URI;

  public TermsAndConditionsFilter() {
      REST_URI = ExoContainerContext.getCurrentContainer().getContext().getRestContextName();
  }


  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest)request;
    HttpServletResponse httpServletResponse = (HttpServletResponse)response;
      OrganizationService orgService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);

      boolean accepted = false;
      boolean logged = false;
      Identity identity = ConversationState.getCurrent().getIdentity();

      String userId = identity.getUserId();

          if( !userId.equals("__anonim"))
          {
          logged=true;
          UserProfileHandler uph = orgService.getUserProfileHandler();

          try {
              UserProfile userProfile = uph.findUserProfileByName(userId);
              String tca = userProfile.getAttribute("TCaccepted");

              if (tca.equals("true"))
              {
                  accepted=true;
              }

          } catch (Exception e) {
              logger.info("T&C not yet accepted by user "+userId);
          }
          }
    
    String requestUri = httpServletRequest.getRequestURI();
    boolean isRestUri = (requestUri.contains(REST_URI));
    boolean isDevMod = PropertyManager.isDevelopping();

    if(! isRestUri && !accepted && logged ) {
      // Get full url
      String reqUri = httpServletRequest.getRequestURI().toString();
      String queryString = httpServletRequest.getQueryString();
      if (queryString != null) {
          reqUri += "?"+queryString;
      }
      
      // Get plf extension servlet context (because TermsAndConditionsFilter and wennsoft-terms-and-conditions servlet declaration are not on same context (webapp))
      ServletContext TandCScreensContext = httpServletRequest.getSession().getServletContext().getContext(WENNSOFT_TandC_SCREENS_SERVLET_CTX);
      // Forward to resource from this context: 
      String uriTarget = (new StringBuilder()).append(TC_SERVLET_URL + "?" + INITIAL_URI_PARAM_NAME + "=").append(reqUri).toString();
      TandCScreensContext.getRequestDispatcher(uriTarget).forward(httpServletRequest, httpServletResponse);
      return;
    }
    chain.doFilter(request, response);
  }
}
