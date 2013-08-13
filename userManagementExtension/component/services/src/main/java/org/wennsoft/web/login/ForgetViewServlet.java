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
public class ForgetViewServlet extends HttpServlet {
  private static final long serialVersionUID = 6467955354840693802L;
  private final static String Forget_JSP_RESOURCE = "/WEB-INF/jsp/forget.jsp";
    private static final String Forget_SERVLET_CTX = "/userManagementExtension";

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      ResourceBundleService service = (ResourceBundleService)PortalContainer.getInstance().getComponentInstanceOfType(ResourceBundleService.class);
      ResourceBundle res = service.getResourceBundle(service.getSharedResourceBundleNames(), req.getLocale()) ;
      req.setAttribute("Forgetlabel",res.getString("UserManagement.forget.Forgetlabel"));
      req.setAttribute("SigninFail",res.getString("UserManagement.forget.SigninFail"));
      req.setAttribute("emailAccount",res.getString("UserManagement.forget.emailAccount"));
      req.setAttribute("send",res.getString("UserManagement.forget.send"));


      req.setAttribute("contextPath",req.getContextPath());
      resp.setContentType("text/html; charset=UTF-8");
      ServletContext TandCScreensContext = req.getSession().getServletContext().getContext(Forget_SERVLET_CTX);
      TandCScreensContext.getRequestDispatcher(Forget_JSP_RESOURCE).forward(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doPost(req, resp);
  }

}
