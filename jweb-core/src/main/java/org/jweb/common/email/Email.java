package org.jweb.common.email;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
/**
 * -----------------------------------------
 * @文件: Email.java
 * @作者: fancy
 * @邮箱: fancydeepin@yeah.net
 * @时间: 2012-6-12
 * @描述: 发送Email工具类
 * -----------------------------------------
 */
public class Email {
    
    private JavaMailSender javaMailSender;
    private SimpleMailMessage simpleMailMessage;
    
  /**
     * @方法名: sendMail 
     * @参数名：@param subject 邮件主题
     * @参数名：@param content 邮件主题内容
     * @参数名：@param to        收件人Email地址
     * @描述语:  发送邮件
     */
    public void sendMail(String subject, String content, String to) {
        
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
          /**
             * new MimeMessageHelper(mimeMessage,true)之true个人见解：
             * 关于true参数,官方文档是这样解释的：
             * use the true flag to indicate you need a multipart message
             * 翻译过来就是：使用true,以表明你需要多个消息
             * 再去翻一下MimeMessageHelper的API,找到这样一句话：
             * supporting alternative texts, inline elements and attachments
             * 也就是说,如果要支持内联元素和附件就必须给定第二个参数为true
             * 否则抛出 java.lang.IllegalStateException 异常
             */
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
            messageHelper.setFrom(simpleMailMessage.getFrom()); //设置发件人Email
            messageHelper.setSubject(subject); //设置邮件主题
            messageHelper.setText(content);   //设置邮件主题内容
            messageHelper.setTo(to);          //设定收件人Email
          /**
             * ClassPathResource：很明显就是类路径资源,我这里的附件是在项目里的,所以需要用ClassPathResource
             * 如果是系统文件资源就不能用ClassPathResource,而要用FileSystemResource,例：
             * FileSystemResource file = new FileSystemResource(new File("D:/Readme.txt"));
             */
//            ClassPathResource file = new ClassPathResource("attachment/Readme.txt");
          /**
             * MimeMessageHelper的addAttachment方法：
             * addAttachment(String attachmentFilename, InputStreamSource inputStreamSource)
             * InputStreamSource是一个接口,ClassPathResource和FileSystemResource都实现了这个接口
             */
//            messageHelper.addAttachment(file.getFilename(), file); //添加附件
            javaMailSender.send(mimeMessage);    //发送附件邮件
            
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new RuntimeException(e);
        }
    }
      //Spring 依赖注入
    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }
      //Spring 依赖注入
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
