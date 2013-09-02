package org.wennsoft.web.listener;

import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.portal.webui.util.Util;

/**
 * @author Ayoub Zayati
 */
public class SignatureConnectUserEventListener extends UserEventListener 
{
    private static final Log LOGGER = ExoLogger.getLogger(SignatureConnectUserEventListener.class);

    public void postSave(User user, boolean isNew) throws Exception 
    {
        String userName = user.getUserName();
        try 
        {
            String currentUserName = Util.getPortalRequestContext().getRemoteUser();
            if (isNew) 
            { // user added
                LOGGER.info("[User Management] New user " + userName + " created by " + currentUserName);
            } 
            else 
            {// user updated
                LOGGER.info("[User Management] " + currentUserName + " modified user " + userName);
            }
			
		} 
        catch (NullPointerException npe) 
        {
		    if (isNew) 
		    { // user added
		        LOGGER.info("[User Management] New user " + userName + " created");
	        } 
		    else 
		    {// user updated
	            LOGGER.info("[User Management] " + userName + " modified");
	        }
		}
        
    }
}
