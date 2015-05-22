package util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import play.Logger;

import com.sun.mail.smtp.SMTPMessage;

public class Mailer {
	


	private String content = null;
	private String subject = null;
	private String recipient = null;
	private String bcc_recipient = null;
	private String mimetype =null;
	private String fromAddress = null;
	
	private String smtpHost = null;
	private String smtpPass = null;
	private String smtpUser = null;
	
	private String error = null;
	private String localhost = null;
	
	

	public Mailer(String content, String subject, String recipient, String mimetype, String fromAddress) {
		super();
		this.content = content;
		this.subject = subject;
		this.recipient = recipient;
		this.mimetype = mimetype;
		this.fromAddress = fromAddress;
	}

	
	public Mailer(String content, String subject, String recipient, String mimetype, String fromAddress, String smtpHost, String smtpPass, String smtpUser) {
		this(content, subject, recipient, mimetype, fromAddress);
		this.smtpHost = smtpHost;
		this.smtpPass = smtpPass;
		this.smtpUser = smtpUser;
	}
	
	public Mailer(String content, String subject, String recipient, String bcc_recipient, String mimetype, String fromAddress,String smtpHost, String smtpPass, String smtpUser){
        this(content, subject, recipient, mimetype, fromAddress);
        this.smtpHost = smtpHost;
        this.smtpPass = smtpPass;
        this.smtpUser = smtpUser;
        this.bcc_recipient= bcc_recipient;
    }
    

	public Mailer(String content, String subject, String recipient, String bcc_receipient, String mimetype, String fromAddress) {
		this(content, subject, recipient, mimetype, fromAddress);
		this.bcc_recipient = bcc_receipient;
	}
	

	
	public void send( ) {

		System.setProperty("mail.mime.charset", "ISO-8859-1");
		SMTPMessage message;
		InternetAddress defaultFrom;

		try {
			defaultFrom = new InternetAddress(this.fromAddress);
			
			Session s = getMailSession();
			message = new SMTPMessage(s);
			message.setFrom(defaultFrom);
			message.setReplyTo(new Address[] { defaultFrom });
			message.setEnvelopeFrom(this.fromAddress);
			message.setSubject(this.subject);
			message.setAllow8bitMIME(true);
			message.setContent(this.content, this.mimetype);			
		} catch (AddressException e) {
			Logger.warn("Error while sending mail: Address invalid?", e);
			error = e.getMessage();
			return;
		} catch (MessagingException e) {
			Logger.error("Error while sending mail", e);
			error = e.getMessage();
			return;
		}

		try {
			if( this.bcc_recipient != null  ){
				if (this.bcc_recipient.indexOf(",") > -1) {
					String[] bccAdresses = this.bcc_recipient.split(",");
					for (String bccAdress : bccAdresses) {
						InternetAddress bcc = new InternetAddress(bccAdress);
						message.addRecipient(Message.RecipientType.BCC, bcc);
					}
				} else {
					InternetAddress bcc = new InternetAddress(this.bcc_recipient);
					message.addRecipient(Message.RecipientType.BCC, bcc);
				}
			}
			if( this.recipient != null  ){
			if (this.recipient.indexOf(",") > -1) {
				String[] toAdresses = this.recipient.split(",");
				for (String toAdress : toAdresses) {
					InternetAddress to = new InternetAddress(toAdress);
					message.addRecipient(Message.RecipientType.TO, to);
				}
			} else {
				InternetAddress to = new InternetAddress(this.recipient);
				message.addRecipient(Message.RecipientType.TO, to);
			}
			}
			Transport.send(message);
		} catch (AddressException e) {
			String failureCause = "While sending mail to " + this.recipient
					+ " Exception : " + e;
			Logger.warn(failureCause, e);
			error = e.getMessage();
		} catch (MessagingException e) {
			String failureCause = "While sending mail to " + this.recipient
					+ " Exception : " + e;
			Logger.warn(failureCause, e);
			error = e.getMessage();
		}
		catch( Exception e){
			String failureCause = "While sending mail to " + this.recipient
			+ " Exception : " + e;
			Logger.error(failureCause, e);
			error = e.getMessage();
		}
	}

	
	/**
	 * return session object with either smtp authentication turned on or direct
	 * mail sending without authentication. when property Mail.SmtpUser is set
	 * in the configuration file, authentication is used.
	 * 
	 * @return Session object for mail transport
	 */
	private Session getMailSession() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", this.smtpHost);
		String username = this.smtpUser;
		
		if (username != null && !"".equals(username)) {
			props.setProperty("mail.smtp.auth", "true");
			
			String password = null;
			password = this.smtpPass;
			
			return Session.getInstance(props, new CustomSmtpAuthenticator(
					username, password));
		}
		return Session.getInstance(props, null);
	}


	public String getError() {
		return error;
	}

    /**
     * Custom Authentication Object - needed for SMTP transport with authentication
     * enabled.
     *
     * @author sn
     *
     * @since che7
     *
     */
    public class CustomSmtpAuthenticator extends Authenticator {

        private String username;
        private String password;

        public CustomSmtpAuthenticator(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
	
	public boolean hasErrors() {
		return error != null;
	}


	public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

	
}
