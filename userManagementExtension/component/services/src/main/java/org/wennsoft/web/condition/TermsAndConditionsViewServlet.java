package org.wennsoft.web.condition;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * @author MedAmine Krout
 */

public class TermsAndConditionsViewServlet extends HttpServlet 
{
	private static Log logger = ExoLogger.getLogger(TermsAndConditionsViewServlet.class);
	private static final long serialVersionUID = 1L;
	private static final String TERMS_AND_CONDITIONS_JSP_RESOURCE = "/WEB-INF/jsp/termsAndConditions.jsp";

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
    	httpServletRequest.setAttribute("contextPath", httpServletRequest.getContextPath());
        httpServletRequest.setAttribute("lang", httpServletRequest.getLocale().getLanguage());
        httpServletResponse.setContentType("text/html; charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        RepositoryService repositoryService = (RepositoryService)PortalContainer.getInstance().getComponentInstanceOfType(RepositoryService.class);
        SessionProviderService sessionProviderService = (SessionProviderService)PortalContainer.getInstance().getComponentInstanceOfType(SessionProviderService.class);
        SessionProvider sessionProvider = sessionProviderService.getSessionProvider(null);
        String workspace = "collaboration";
        Session session = null;
        try 
        {
            ManageableRepository manageableRepository = repositoryService.getCurrentRepository();
            session = sessionProvider.getSession(workspace, manageableRepository);
            Node tersmAndConditionsNode = (Node) session.getItem("/terms-and-conditions");
            String tersmAndConditionsNodeContent =  tersmAndConditionsNode.getNode("default.html/jcr:content").getProperty("jcr:data").getString();
            httpServletRequest.setAttribute("tcNodeContent", tersmAndConditionsNodeContent);
        } 
        catch (RepositoryException repositoryException)
        {
            logger.error( "Item not found");
        }
        getServletContext().getRequestDispatcher(TERMS_AND_CONDITIONS_JSP_RESOURCE).include(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException 
    {
        doPost(httpServletRequest, httpServletResponse);
    }
}
