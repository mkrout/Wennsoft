package org.wennsoft.web.condition;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author MedAmine Krout
 */
public class TermsAndConditionsViewServlet extends HttpServlet {
  private static final long serialVersionUID = 6467955354840693802L;
  private final static String TC_JSP_RESOURCE = "/WEB-INF/jsp/condition/wennsoftconditions.jsp";

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      getServletContext().getRequestDispatcher(TC_JSP_RESOURCE).include(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

}
