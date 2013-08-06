package org.wennsoft.web.login;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.web.AbstractHttpServlet;
import org.exoplatform.services.resources.ResourceBundleService;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.wennsoft.web.utils.Utils;

public class ForgetServlet extends AbstractHttpServlet {
     /**
     *
     */
    private static final long serialVersionUID = -1330051083735349589L;
    /** . */
    private static final Logger log = LoggerFactory.getLogger(ForgetServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResourceBundleService service = (ResourceBundleService)PortalContainer.getInstance().getComponentInstanceOfType(ResourceBundleService.class);

        ResourceBundle res = service.getResourceBundle(service.getSharedResourceBundleNames(), req.getLocale()) ;

       String headerMail = res.getString("UserManagement.forget.header");
        String footerMail = res.getString("UserManagement.forget.footer");
        String np =   res.getString("UserManagement.forget.newpassd");

        String emailAccount = req.getParameter("emailAccount");
        log.info(emailAccount);
        String sb=Utils.changePassword(emailAccount);
          if (sb!=null)
          {
              String to = emailAccount;
              String subject = res.getString("UserManagement.forget.subject");
              String mailText = headerMail + "\n\n" +  np + sb.toString()+ "\n\n" +  footerMail;
              Utils.sendMAil(to, subject, mailText);

        String redirectURI = "/" + PortalContainer.getCurrentPortalContainerName() + "/login";
        resp.setCharacterEncoding("UTF-8");
        resp.sendRedirect(redirectURI);

          }
        else {
              req.setAttribute("org.wennsoft.web.login.forget.error", "true");
              resp.setContentType("text/html; charset=UTF-8");
              getServletContext().getRequestDispatcher("/login/jsp/forget.jsp").include(req, resp);
          }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
