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


public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static String gneratePassword (int lenth) {
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lenth; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }

    public static String changePassword (String emailAccount){
        String sb=null;
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
                    sb = Utils.gneratePassword(8);
                    user.setPassword(sb.toString());
                    orgService.getUserHandler().saveUser(user, true);
                 }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
	        } finally {
	            RequestLifeCycle.end();
	        }
        return sb;
    }

    public  static void  sendMAil (String to, String  subject, String  mailText) {
        MailService mailSrc = (MailService) PortalContainer.getInstance().getComponentInstanceOfType(MailService.class);
        Message message = new Message() ;
        message.setTo(to);
        message.setSubject(subject) ;
        message.setBody(mailText) ;
        message.setMimeType("text/html") ;
        try {
            mailSrc.sendMessage(message);
        }
        catch(Exception e) {
             log.error("Mail not sent");
        }
    }
}
