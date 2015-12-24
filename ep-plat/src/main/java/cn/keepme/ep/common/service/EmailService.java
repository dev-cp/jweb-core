package cn.keepme.ep.common.service;

import java.io.File;

/**
 * This interface define methods that are used to send email.
 */
public interface EmailService {

    public void sendTextMail(String[] receviers, String replyTo, String subject, String content);

    public void sendMimeMail(String[] receviers, String replyTo, String subject, String content, File[] attachments);
}
