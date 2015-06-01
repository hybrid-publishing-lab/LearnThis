package util;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Play;


public class MailUtil {
    private static final String SENDER_EMAIL = Play.application().configuration().getString("f3.mail.senderEmail"); 
    private static final String SMTPHOST = Play.application().configuration().getString("f3.mail.smtpHost");
    private static final String SMTPPASS = Play.application().configuration().getString("f3.mail.smtpPass");
    private static final String SMTPUSER = Play.application().configuration().getString("f3.mail.smtpUser");

    public static void sendMail(String content, String mailSubject, String recipient){
        Mailer mailer = new Mailer(content, mailSubject, recipient, null, "text/html", SENDER_EMAIL, SMTPHOST, SMTPPASS, SMTPUSER);
        
        if (mailer != null) {
            mailer.send();
            if (StringUtils.isBlank(mailer.getError())){
                Logger.info("Sent message with subject '" + mailSubject + "' to " + recipient);
            }
        }
    }
}
