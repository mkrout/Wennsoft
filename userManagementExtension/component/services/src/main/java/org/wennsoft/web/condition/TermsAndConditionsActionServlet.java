/**
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.wennsoft.web.condition;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * @author MedAmine Krout
 */

public class TermsAndConditionsActionServlet extends HttpServlet {
    private static final long serialVersionUID = 6467955354840693802L;

    private static Log logger = ExoLogger.getLogger(TermsAndConditionsActionServlet.class);


   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        OrganizationService orgService = (OrganizationService) PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
        String userId = request.getRemoteUser();
        UserProfileHandler uph = orgService.getUserProfileHandler();
         try {
            RequestLifeCycle.begin(PortalContainer.getInstance());
            UserProfile userProfile = uph.findUserProfileByName(userId);
            userProfile.setAttribute("TCaccepted", "true");
            uph.saveUserProfile(userProfile, true);
            RequestLifeCycle.end();

        } catch (Exception e) {
             logger.error("User Profile not updated");
        }

        // Redirect to the account Setup
        String redirectURI = "/portal/";
        response.sendRedirect(redirectURI);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}