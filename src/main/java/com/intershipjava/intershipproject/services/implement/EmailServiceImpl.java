package com.intershipjava.intershipproject.services.implement;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.services.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import freemarker.template.Configuration;
@Service
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LogManager.getLogger(getClass());
    private static final String SUPPORT_EMAIL = "support.email";
    public static final String LINE_BREAK = "<br>";
    public final static String BASE_URL = "baseUrl";

    @Autowired
    private MessageService messageService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    Configuration freemarkerConfiguration;

    @Async
    @Override
    public void sendVerificationToken(String token, Customer customer) {
         final String confirmationUrl = "http://localhost:4200/verify?token=" + token;
         final String message = "Thank you for creating an account. Please click the link below to activate your account. This link will expire in 24 hours.";
         sendHtmlEmail("Registration Confirmation", message + LINE_BREAK + confirmationUrl, customer);
    }

    private String geFreeMarkerTemplateContent(Map<String, Object> model, String templateName) {
        StringBuffer content = new StringBuffer();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), model));

            return content.toString();
        } catch (Exception e) {
            System.out.println("Exception occured while processing fmtemplate:" + e.getMessage());
        }
        return "";
    }

    private void sendHtmlEmail(String subject, String msg, Customer user) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", user.getName());
        model.put("msg", msg);
        model.put("title", subject);
        model.put(BASE_URL, "http://localhost:8082/");
        try {
            sendHtmlMail(env.getProperty(SUPPORT_EMAIL), user.getEmail(), subject, geFreeMarkerTemplateContent(model, "mail/verification.ftl"));
        } catch (MessagingException e) {
            logger.error("Failed to send mail", e);
        }
    }

    public void sendHtmlMail(String from, String to, String subject, String body) throws MessagingException {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
        helper.setFrom(from);
        if (to.contains(",")) {
            helper.setTo(to.split(","));
        } else {
            helper.setTo(to);
        }
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(mail);
        logger.info("Sent mail: {0}", subject);
    }

}
