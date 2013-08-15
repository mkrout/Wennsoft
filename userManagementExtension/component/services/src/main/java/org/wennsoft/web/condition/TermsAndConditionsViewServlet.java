package org.wennsoft.web.condition;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.jcr.Node;
import javax.jcr.Session;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.core.ManageableRepository;

/**
 * @author MedAmine Krout
 */

public class TermsAndConditionsViewServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private static final String TERMS_AND_CONDITIONS_JSP_RESOURCE = "/WEB-INF/jsp/termsAndConditions.jsp";

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        httpServletRequest.setAttribute("contextPath", httpServletRequest.getContextPath());
        httpServletRequest.setAttribute("lang", httpServletRequest.getLocale().getLanguage());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance().getComponentInstanceOfType(RepositoryService.class);
        SessionProviderService sessionProviderService = (SessionProviderService) PortalContainer.getInstance().getComponentInstanceOfType(SessionProviderService.class);
        SessionProvider  sessionProvider = sessionProviderService.getSessionProvider(null);
        String workspace = "collaboration";
        Session sess = null;
        try {
            ManageableRepository manageableRepository = repositoryService.getCurrentRepository();
            sess = sessionProvider.getSession(workspace, manageableRepository);
            Node node = (Node) sess.getItem("/condition");
            String content =  node.getNode("default.html/jcr:content").getProperty("jcr:data").getString();
            httpServletRequest.setAttribute("termCondition", content);
        } catch (RepositoryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        getServletContext().getRequestDispatcher(TERMS_AND_CONDITIONS_JSP_RESOURCE).include(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        doPost(httpServletRequest, httpServletResponse);
    }
}
