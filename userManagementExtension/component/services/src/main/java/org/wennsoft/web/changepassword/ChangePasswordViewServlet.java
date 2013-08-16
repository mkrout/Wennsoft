package org.wennsoft.web.changepassword;

import org.exoplatform.services.resources.ResourceBundleService;
import org.exoplatform.container.PortalContainer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Ayoub Zayati
 */

public class ChangePasswordViewServlet extends HttpServlet 
{
    private static final String CHANGE_PASSWORD_JSP_RESOURCE = "/WEB-INF/jsp/changePassword.jsp";
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
        httpServletRequest.setAttribute("changePassword", resourceBundle.getString("userManagement.change.changePassword"));
        httpServletRequest.setAttribute("newPassword", resourceBundle.getString("userManagement.change.newPassword"));
        httpServletRequest.setAttribute("reNewPassword", resourceBundle.getString("userManagement.change.reNewPassword"));
        httpServletRequest.setAttribute("send", resourceBundle.getString("userManagement.change.send"));
        httpServletRequest.setAttribute("contextPath", httpServletRequest.getContextPath());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        getServletContext().getRequestDispatcher(CHANGE_PASSWORD_JSP_RESOURCE).include(httpServletRequest, httpServletResponse);
    }
}
