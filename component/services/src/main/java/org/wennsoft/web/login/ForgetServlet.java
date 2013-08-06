package org.wennsoft.web.login;
/**
 * Created with IntelliJ IDEA.
 * User: MAK
 * Date: 04/08/13
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.container.web.AbstractHttpServlet;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.gatein.common.logging.LoggerFactory;
import org.gatein.common.logging.Logger;
import org.exoplatform.services.organization.Query;
import org.exoplatform.commons.utils.PageList;
/**
 * The login servlet which proceeds as.
 *
 * 0. If user is already authenticated : nothing happens 1. When username and password are provided, a login is attempted 1.1 if
 * login is successful the user is authenticated 1.2 if login fails the user is not authenticated and the login form is
 * displayed with the user name filled 2. When username is provided, the login form is displayed with the user name filled 3.
 * Finally if nothing is provided the login form is displayed
 *
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
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


        try {
            OrganizationService orgService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
            RequestLifeCycle.begin((ComponentRequestLifecycle) orgService);
            Query query = new Query();
            query.setEmail(emailAccount);
            PageList<User> users = null;
            try {
                users = orgService.getUserHandler().findUsers(query);
                if (users.getAll().size() > 0)
                {
                    User user = users.getAll().get(0);
                    String email = user.getEmail();

                    String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                    Random rand = new Random(System.currentTimeMillis());
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 8; i++) {
                        int pos = rand.nextInt(charset.length());
                        sb.append(charset.charAt(pos));
                    }
                    log.info("Sending mails");
                    log.info("New PSS  "+sb);


                    user.setPassword(sb.toString());
                    orgService.getUserHandler().saveUser(user, true);

                    String redirectURI = "/" + PortalContainer.getCurrentPortalContainerName() + "/login";
                    resp.setCharacterEncoding("UTF-8");
                    resp.sendRedirect(redirectURI);

                }
                else
                {
                    req.setAttribute("org.wennsoft.web.login.forget.error", "true");
                    log.info("User not found");
                    //String redirectURI = "/" + PortalContainer.getCurrentPortalContainerName() + "/login/jsp/forget.jsp";
                    resp.setContentType("text/html; charset=UTF-8");
                    getServletContext().getRequestDispatcher("/login/jsp/forget.jsp").include(req, resp);
                    //log.info(redirectURI);
                   // resp.setCharacterEncoding("UTF-8");
                    //resp.sendRedirect(redirectURI);
                }


            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }




        } finally {

            RequestLifeCycle.end();
        }
        // Redirect to requested page


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


}
