package org.wennsoft.web.login;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.web.AbstractHttpServlet;
import org.gatein.common.logging.LoggerFactory;
import org.gatein.common.logging.Logger;
import org.wennsoft.web.utils.Utils;
import java.util.ResourceBundle;

public class ForgetServlet extends AbstractHttpServlet {
     /**
     *
     */
    private static final long serialVersionUID = -1330051083735349589L;
    /** . */
    private static final Logger log = LoggerFactory.getLogger(ForgetServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String emailAccount = req.getParameter("emailAccount");
        log.info(emailAccount);
          if (Utils.changePassword(emailAccount))
          {
        String redirectURI = "/" + PortalContainer.getCurrentPortalContainerName() + "/login";
        resp.setCharacterEncoding("UTF-8");
        resp.sendRedirect(redirectURI);

          }
        else {
              req.setAttribute("org.wennsoft.web.login.forget.error", "true");
              log.info("User not found");
              resp.setContentType("text/html; charset=UTF-8");
              getServletContext().getRequestDispatcher("/login/jsp/forget.jsp").include(req, resp);
          }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
