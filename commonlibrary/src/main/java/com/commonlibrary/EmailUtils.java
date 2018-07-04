package com.commonlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SimpleAdapter;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Store;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailUtils {

    private static String tos[];

    /**
     * 邮件发送程序
     *
     * @param
     * @param subject 邮件主题
     * @param content 邮件内容
     * @throws Exception
     * @throws
     */
    public static void sendEmail(final Context context, final String subject, final String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
//        String host = "mail.zqlwl.cn";
//        String address = "itserver@zqlwl.cn";
//        String from = "itserver@zqlwl.cn";
//        String password = "Admin@123";// 密码
        final String host = context.getResources().getString(R.string.host).toString();
        final String address = context.getResources().getString(R.string.address).toString();
        final String from = context.getResources().getString(R.string.from).toString();
        final String password = context.getResources().getString(R.string.password).toString();

        String to1 = context.getResources().getString(R.string.to).toString(); //郝九凤
//        String to2 = context.getResources().getString(R.string.to2).toString(); //
//        String to3 = context.getResources().getString(R.string.to3).toString();//
        String to4 = context.getResources().getString(R.string.to4).toString(); //齐建周
        String to6 = context.getResources().getString(R.string.to6).toString(); //马金刚
//        String to7 = context.getResources().getString(R.string.to7).toString(); //程玉龙

        tos = new String[]{to1, to4, to6};
//        tos = new String[] {to1,to2,to3,to4,to6};
        if ("".equals(tos) || tos == null) {
//            tos = new String[] {to1,to2,to3,to4,to6};
            tos = new String[]{to1, to4, to6};
        }
        if (subject.contains("消息页面加载数据异常")) {
            tos = new String[]{to1};
        }
        final String port = context.getResources().getString(R.string.port).toString();

        new Thread() {
            public void run() {
                try {
                    SendEmail(host, address, from, password, port, DeviceInfoUtil.getVersionCode(context) + subject, content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }


    /**
     * 邮件服发送
     *
     * @param host     邮件服务器
     * @param address  发送邮件的地址
     * @param from
     * @param password
     * @param \
     * @param port     端口
     * @param subject  邮件主题
     * @param content  邮件内容
     */
    private static void SendEmail(String host, String address, String from,
                                  String password, String port, String subject,
                                  String content) throws Exception {

        Multipart multiPart;
        String finalString = "";
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", address);
            props.put("mail.smtp.password", password);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            Log.i("Check", "done pops");
            Session session = Session.getDefaultInstance(props, null);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(), "text/plain"));
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setDataHandler(handler);
//			Log.i("Check", "done sessions");

            multiPart = new MimeMultipart();
            String to = getMailList(tos);
            InternetAddress[] toAddress;
            toAddress = new InternetAddress().parse(to);
            //message.addRecipient(Message.RecipientType.TO, toAddress);
            message.setRecipients(Message.RecipientType.TO, toAddress);
//			Log.i("Check", "added recipient");
            message.setSubject(subject);
            message.setContent(multiPart);
            message.setText(TextUtils.isEmpty(content) ? "" : content);

//			Log.i("check", "transport");
            Transport transport = session.getTransport("smtp");
//			Log.i("check", "connecting");
            transport.connect(host, address, password);
//			Log.i("check", "wana send");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
//			Log.i("check", "sent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //拼接邮件接收人字符串
    public static String getMailList(String[] mailArray) {
        StringBuffer buffer = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            buffer.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                buffer.append(mailArray[i]);
                if (i != (length - 1)) {
                    buffer.append(",");
                }
            }
        }
        return buffer.toString();
    }
}
