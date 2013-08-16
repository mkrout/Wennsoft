package org.wennsoft.web.forgetpassword;

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

public class ForgetPasswordActionServlet extends AbstractHttpServlet 
{
	private final static String FORGET_PASSWORD_JSP_RESOURCE = "/WEB-INF/jsp/forgetPassword.jsp";
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        ResourceBundleService resourceBundleService = (ResourceBundleService)PortalContainer.getInstance().getComponentInstanceOfType(ResourceBundleService.class);
        ResourceBundle resourceBundle = resourceBundleService.getResourceBundle(resourceBundleService.getSharedResourceBundleNames(), httpServletRequest.getLocale()) ;
        
        httpServletRequest.setAttribute("forgetPassword", resourceBundle.getString("userManagement.forget.forgetPassword"));
        httpServletRequest.setAttribute("emailError", resourceBundle.getString("userManagement.forget.emailError"));
        httpServletRequest.setAttribute("emailAccount", resourceBundle.getString("userManagement.forget.emailAccount"));
        httpServletRequest.setAttribute("send", resourceBundle.getString("userManagement.forget.send"));
        httpServletRequest.setAttribute("contextPath", httpServletRequest.getContextPath());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        String emailAccount = httpServletRequest.getParameter("emailAccount");
        String emailFooter = resourceBundle.getString("userManagement.forget.emailFooter");
        String emailHeader = resourceBundle.getString("userManagement.forget.emailHeader");
        String yourNewPassword = resourceBundle.getString("userManagement.forget.yourNewPassword");
        String password = Utils.changePassword(emailAccount);
        if (password != null)
        {
        	String mailText = emailHeader + "\n\n" +  yourNewPassword + password + "\n\n" +  emailFooter;
        	String subject = resourceBundle.getString("userManagement.forget.emailSubject");
            Utils.sendMAil(emailAccount, subject, mailText);
            String redirectURI = "/" + PortalContainer.getCurrentPortalContainerName() + "/login";
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.sendRedirect(redirectURI);
        }
        else 
        {
            httpServletRequest.setAttribute("notValidEmail", "true");
            getServletContext().getRequestDispatcher(FORGET_PASSWORD_JSP_RESOURCE).include(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        doGet(httpServletRequest, httpServletResponse);
    }
}    
