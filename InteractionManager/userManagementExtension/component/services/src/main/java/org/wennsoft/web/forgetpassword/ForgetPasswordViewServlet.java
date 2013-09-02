package org.wennsoft.web.forgetpassword;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.resources.ResourceBundleService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;


/**
 * @author MedAmine Krout
 */
public class ForgetPasswordViewServlet extends HttpServlet 
{
	private final static String FORGET_PASSWORD_JSP_RESOURCE = "/WEB-INF/jsp/forgetPassword.jsp";
    private static final String FORGET_PASSWORD_SERVLET_CTX = "/userManagementExtension";
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        doPost(httpServletRequest, httpServletResponse);
    }
	
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        ResourceBundleService resourceBundleService = (ResourceBundleService)PortalContainer.getInstance().getComponentInstanceOfType(ResourceBundleService.class);
        ResourceBundle resourceBundle = resourceBundleService.getResourceBundle(resourceBundleService.getSharedResourceBundleNames(), httpServletRequest.getLocale()) ;
        httpServletRequest.setAttribute("forgetPassword", resourceBundle.getString("userManagement.forget.forgetPassword"));
        httpServletRequest.setAttribute("emailError", resourceBundle.getString("userManagement.forget.emailError"));
        httpServletRequest.setAttribute("emailAccount", resourceBundle.getString("userManagement.forget.emailAccount"));
        httpServletRequest.setAttribute("send", resourceBundle.getString("userManagement.forget.send"));
        httpServletRequest.setAttribute("contextPath", httpServletRequest.getContextPath());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        ServletContext servletContext = httpServletRequest.getSession().getServletContext().getContext(FORGET_PASSWORD_SERVLET_CTX);
        servletContext.getRequestDispatcher(FORGET_PASSWORD_JSP_RESOURCE).forward(httpServletRequest, httpServletResponse);
    }
}
