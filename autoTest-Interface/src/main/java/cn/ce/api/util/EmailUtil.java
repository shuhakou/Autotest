package cn.ce.api.util;

import cn.ce.api.enums.EmailTypeEnum;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class EmailUtil {
    //读取emailConfig文件
    private static Map<String, String> emailConfigMap = new ParseXmlUtil().getEmaiConfig();

    /**
     * 发送邮件，指定发件人，收件人，抄送和密送使用api-config.xml的配置
     * @param from 发件人
     * @param to  收件人
     * @param subject 邮件标题
     * @param contents 邮件内容
     * @param type  邮件类型：纯文本/附件/图片
     */
    public void send(String from, String to, String subject, String contents, EmailTypeEnum type) {
        try {
            Session session = getSession(emailConfigMap.get("type"));
            Message message = getMessage(session, from, to, subject, contents, type);
            Transport.send(message);
            System.out.println("send message success!");
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送邮件，指定收件人，发件人使用api-config.xml中配置的发件人，抄送和密送使用api-config.xml的配置
     * @param to 收件人
     * @param subject 邮件标题
     * @param contents 邮件内容
     * @param type 邮件类型：纯文本/附件/图片
     */
    public void send(String to, String subject, String contents, EmailTypeEnum type) {
        send(emailConfigMap.get("from"), to, subject, contents, type);
    }

    /**
     * 发送邮件，收件人，发件人，抄送和密送使用api-config.xml中配置
     * @param subject 邮件标题
     * @param contents 邮件内容
     * @param type 邮件类型：纯文本/附件/图片
     */
    public void send(String subject, String contents, EmailTypeEnum type) {
        send(emailConfigMap.get("from"), emailConfigMap.get("to"), subject, contents, type);
    }

    /**
     * 将地址根据“;”拆分为多个地址并封装到List中
     * @param address 地址
     * @return 地址集合
     */
    private List<String> getAddress(String address) {
        List<String> addressList = new ArrayList<>();

        if (address.isEmpty())
            return addressList;

        if (address.indexOf(";") > 0) {
            String[] addresses = address.split(";");
            addressList.addAll(Arrays.asList(addresses));
        } else {
            addressList.add(address);
        }

        return addressList;
    }

    /**
     * 根据Type得到不同类型的session
     * Outgoing Mail (SMTP) Server
     * requires TLS or SSL: smtp.gmail.com (use authentication)
     * Use Authentication: Yes
     * Port for SSL: 465
     * Port for TLS/STARTTLS: 587
     *
     * @param type 协议类型（SSL/TLS）
     * @return Session
     */
    private Session getSession(String type) {
        boolean isAuthor = true;//判断是否需要授权
        Properties props = System.getProperties();
        props.put("mail.smtp.host", emailConfigMap.get("host"));
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        if (type.toLowerCase().equals("tls")) {
            props.put("mail.smtp.port", "587"); //TLS Port
        } else if (type.toLowerCase().equals("ssl")) {
            props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
            props.put("mail.smtp.port", "465"); //SMTP Port
        } else {
            isAuthor = false;
        }
        Authenticator auth = null;
        if (isAuthor) {
            auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailConfigMap.get("AuthorUsername"), emailConfigMap.get("AuthorCode"));
                }
            };
        }

        return Session.getInstance(props, auth);
    }

    /**
     * 根据不同类型的邮件得到不同的message
     *
     * @param session session
     * @param from 发件人
     * @param to 收件人
     * @param subject 邮件标题
     * @param contents 邮件内容
     * @param type 邮件类型：纯文本/附件/图片
     * @return Message
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    private Message getMessage(Session session, String from, String to, String subject, String contents, EmailTypeEnum type) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        Multipart multipart;
        switch (type) {
            case TEXT:
                message.setText(contents, "UTF-8");
                break;
            case ATTACHMENT:
                multipart = getMultPart(message, contents, emailConfigMap.get("AttachPath"), emailConfigMap.get("AttachName"), EmailTypeEnum.ATTACHMENT);
                // Send the complete message parts
                message.setContent(multipart);
                break;
            case IMAGE:
                multipart = getMultPart(message, contents, emailConfigMap.get("imagePath"), emailConfigMap.get("imageTitle"), EmailTypeEnum.IMAGE);
                //Set the multipart message to the email message
                message.setContent(multipart);
            default:
        }
        message.setFrom(new InternetAddress(from, emailConfigMap.get("senderName")));
        message.setSubject(subject, "UTF-8");
        message.setSentDate(new Date());
        List<String> toList = getAddress(to);
        for (String address : toList) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
        }

        List<String> ccList = getAddress(emailConfigMap.get("cc"));
        for (String address : ccList) {
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(address));
        }

        List<String> bccList = getAddress(emailConfigMap.get("bcc"));
        for (String address : bccList) {
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(address));
        }
        return message;
    }

    /**
     * 根据image/attachment得到对应的Multipart
     * @param message
     * @param contents 邮件内容
     * @param filePath image/attachment的文件路径,
     * @param filename attachment的展示名称，如果是image,则代表image上方显示的文字描述
     * @param type 邮件类型：纯文本/附件/图片
     * @return Multipart
     * @throws MessagingException
     */
    private Multipart getMultPart(Message message, String contents, String filePath, String filename, EmailTypeEnum type) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        message.addHeader("Content-type", "text/HTML; charset=UTF-8");
        message.addHeader("format", "flowed");
        message.addHeader("Content-Transfer-Encoding", "8bit");

        // first part text
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(contents);
        multipart.addBodyPart(messageBodyPart);
        //Second part file
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filePath);
        messageBodyPart.setDataHandler(new DataHandler(source));


         /*
                也可以使用第二种方式
            try {
                    messageBodyPart2.attachFile(emailConfigMap.get("reportFilePath"));
                    messageBodyPart2.setFileName(MimeUtility.encodeWord(emailConfigMap.get("reportFileName")));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
        if (Objects.equals(type, EmailTypeEnum.ATTACHMENT)) {
            messageBodyPart.setFileName(filename);//设置报告名称
            multipart.addBodyPart(messageBodyPart);
        } else if (Objects.equals(type, EmailTypeEnum.IMAGE)) {
            messageBodyPart.setFileName(filePath);//设置图片路径,不要使用fileName，否则引用不到该图片
            messageBodyPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(messageBodyPart);
            //third part for displaying image in the email body
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("<h1>" + filename + "</h1>" +
                    "<img src='cid:image_id'>", "text/html");
            multipart.addBodyPart(messageBodyPart);
        }
        return multipart;
    }

    public static void main(String[] args) throws MessagingException {
        ParseXmlUtil parseXmlUtil = new ParseXmlUtil("K:\\Autotest\\autoTest-Interface\\src\\main\\resources\\api-config.xml");
        // new EmailUtil().send("不正确的协议", "testContents2");
        EmailUtil emailUtil = new EmailUtil();
         emailUtil.send("文本邮件","发送文本信息啊",EmailTypeEnum.TEXT);
        emailUtil.send("带有附件的邮件标题", "带有附件的邮件", EmailTypeEnum.ATTACHMENT);
        emailUtil.send("带有图片的邮件标题", "带有图片的邮件", EmailTypeEnum.IMAGE);

    }

}
