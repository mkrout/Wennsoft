package org.wennsoft.web.login;

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
public class ForgetViewServlet extends HttpServlet 
{
	private final static String Forget_JSP_RESOURCE = "/WEB-INF/jsp/forget.jsp";
    private static final String Forget_SERVLET_CTX = "/userManagementExtension";
	private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        ResourceBundleService resourceBundleService = (ResourceBundleService)PortalContainer.getInstance().getComponentInstanceOfType(ResourceBundleService.class);
        ResourceBundle resourceBundle = resourceBundleService.getResourceBundle(resourceBundleService.getSharedResourceBundleNames(), httpServletRequest.getLocale()) ;
        httpServletRequest.setAttribute("forgetPassword",resourceBundle.getString("userManagement.forget.forgetPassword"));
        httpServletRequest.setAttribute("signinFail",resourceBundle.getString("userManagement.forget.signinFail"));
        httpServletRequest.setAttribute("emailAccount",resourceBundle.getString("userManagement.forget.emailAccount"));
        httpServletRequest.setAttribute("send",resourceBundle.getString("userManagement.forget.send"));
        httpServletRequest.setAttribute("contextPath", httpServletRequest.getContextPath());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        ServletContext servletContext = httpServletRequest.getSession().getServletContext().getContext(Forget_SERVLET_CTX);
        servletContext.getRequestDispatcher(Forget_JSP_RESOURCE).forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        doPost(req, resp);
    }
}
