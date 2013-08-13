package org.wennsoft.web.login;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.web.AbstractHttpServlet;
import org.exoplatform.services.resources.ResourceBundleService;
import org.wennsoft.web.utils.Utils;

/**
 * @author MedAmine Krout
 */

public class ForgetServlet extends AbstractHttpServlet 
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        ResourceBundleService resourceBundleService = (ResourceBundleService)PortalContainer.getInstance().getComponentInstanceOfType(ResourceBundleService.class);
        ResourceBundle resourceBundle = resourceBundleService.getResourceBundle(resourceBundleService.getSharedResourceBundleNames(), httpServletRequest.getLocale()) ;
        
        httpServletRequest.setAttribute("forgetlabel",resourceBundle.getString("userManagement.forget.forgetPassword"));
        httpServletRequest.setAttribute("signinFail",resourceBundle.getString("userManagement.forget.signinFail"));
        httpServletRequest.setAttribute("emailAccount",resourceBundle.getString("userManagement.forget.emailAccount"));
        httpServletRequest.setAttribute("send",resourceBundle.getString("userManagement.forget.send"));
        httpServletRequest.setAttribute("contextPath", httpServletRequest.getContextPath());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        String emailHeader = resourceBundle.getString("userManagement.forget.emailHeader");
        String emailFooter = resourceBundle.getString("userManagement.forget.emailFooter");
        String emailAccount = httpServletRequest.getParameter("emailAccount");
        String newPassword = resourceBundle.getString("userManagement.forget.newPassword");
        String password = Utils.changePassword(emailAccount);
        if (password != null)
        {
        	String mailText = emailHeader + "\n\n" +  newPassword + password + "\n\n" +  emailFooter;
        	String subject = resourceBundle.getString("userManagement.forget.emailSubject");
            Utils.sendMAil(emailAccount, subject, mailText);
            String redirectURI = "/" + PortalContainer.getCurrentPortalContainerName() + "/login";
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.sendRedirect(redirectURI);
        }
        else 
        {
            httpServletRequest.setAttribute("org.wennsoft.web.login.forget.error", "true");
        	httpServletResponse.setContentType("text/html; charset=UTF-8");
            getServletContext().getRequestDispatcher("/login/jsp/forget.jsp").include(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        doGet(req, resp);
    }
}    
