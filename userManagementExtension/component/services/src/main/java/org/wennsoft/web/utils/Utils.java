package org.wennsoft.web.utils;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.mail.MailService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.exoplatform.services.mail.Message;
import java.util.Random;

/**
 * @author MedAmine Krout
 */
public class Utils 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    
    public static String changePassword(String emailAccount)
    {
    	String password = new String();
        try 
        {
            OrganizationService organizationService = (OrganizationService)PortalContainer.getInstance().getComponentInstanceOfType(OrganizationService.class);
            RequestLifeCycle.begin((ComponentRequestLifecycle)organizationService);
            Query query = new Query();
            query.setEmail(emailAccount);
            PageList<User> users = null;
            try 
            {
                users = organizationService.getUserHandler().findUsers(query);
                if (users.getAll().size() > 0)
                {
                    User user = users.getAll().get(0);
                    password = generatePassword(8);
                    user.setPassword(password);
                    organizationService.getUserHandler().saveUser(user, true);
                }
            } 
            catch (Exception exception) 
            {
            	LOGGER.error("Cannot change password");
            }
	        } 
            finally
            {
	            RequestLifeCycle.end();
	        }
        return password;
    }

    public static String generatePassword(int length) 
    {
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random(System.currentTimeMillis());
        StringBuilder password = new StringBuilder();
        for (int count = 0; count < length; count++) 
        {
            int position = random.nextInt(charset.length());
            password.append(charset.charAt(position));
        }
        return password.toString();
    }

    public  static void sendMAil(String to, String  subject, String  mailText) 
    {
        MailService mailService = (MailService)PortalContainer.getInstance().getComponentInstanceOfType(MailService.class);
        Message message = new Message() ;
        message.setTo(to);
        message.setSubject(subject) ;
        message.setBody(mailText) ;
        message.setMimeType("text/html") ;
        try 
        {
        	mailService.sendMessage(message);
        }
        catch (Exception exception) 
        {
            LOGGER.error("Mail not sent");
        }
    }
}