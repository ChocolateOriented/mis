package com.mo9.risk.util;

import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {
	private static String DEFAULT_PROTOCOL = "smtp";

	private String protocol;//协议

	private String host;//主机

	private String username;//用户名

	private String password;//密码

	private String from;//发件人

	private String to;//收件人

	private String cc;//抄送人

	private boolean bccFlag = false;//是否密送，默认否

	private String subject;//邮件主题

	private String content;//邮件正文

	private List<File> attachFiles = new ArrayList<File>();//附件文件集合

	private Map<String,DataSource> attachSource ; //附件数据集合

	private Map<String,DataSource> imgs ; //图片集合

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public boolean isBccFlag() {
		return bccFlag;
	}

	public void setBccFlag(boolean bccFlag) {
		this.bccFlag = bccFlag;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MailSender() {
		super();
	}

	/**
	 * @Description 使用系统默认邮箱
	 * @param to
	 */
	public MailSender(String to) {
		//使用数据库字典
		this.host = DictUtils.getDictValue("email_server","sys_email","");
		String userAdress = DictUtils.getDictValue("email_username","sys_email","");
		this.username = userAdress ;
		this.from = userAdress ;
		this.password = DictUtils.getDictValue("email_password","sys_email","");

		this.to = to;
	}

	/**
	 * 添加附件
	 * @param filenam
	 */
	public void addAttachFile(String filenam) {
		File file = new File(filenam);
		attachFiles.add(file);

	}

	/**
	 * 添加附件
	 * @param file
	 */
	public void addAttachFile(File file) {
		attachFiles.add(file);
	}

	/**
	 * 清空附件
	 */
	public void clearAttachFile() {
		attachFiles.clear();
	}
	/**
	 * @Description 添加附件
	 * @return
	 */
	public void addAttachSource(DataSource dataSource ,String fileName) {
		if (attachSource == null){
			attachSource = new HashMap<String, DataSource>();
		}
		attachSource.put(fileName,dataSource);
	}

	/**
	 * @Description 添加图片
	 * @return
	 */
	public void addImage(DataSource dataSource ,String contentId) {
		if (imgs == null){
			imgs = new HashMap<String, DataSource>();
		}
		imgs.put(contentId, dataSource);
	}

	/**
	 * 发送邮件
	 * @throws MessagingException 
	 */
	public void sendMail() throws MessagingException {
		if (host == null || "".equals(host)) {
			throw new IllegalArgumentException("host is empty");
		}

		if (username == null || "".equals(username)) {
			throw new IllegalArgumentException("username is empty");
		}

		if (password == null || "".equals(password)) {
			throw new IllegalArgumentException("password is empty");
		}

		if (from == null || "".equals(from)) {
			throw new IllegalArgumentException("from is empty");
		}

		if (to == null || "".equals(to)) {
			throw new IllegalArgumentException("to is empty");
		}

		//构造mail session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		Session session = Session.getInstance(props,
			new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

		Transport transport = null;

		try {
			MimeMessage msg = new MimeMessage(session);

			//设置发件人
			msg.setFrom(new InternetAddress(from));

			//设置收件人和抄送
			if (bccFlag) {
				msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to));
			} else {
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				if (cc != null && !"".equals(cc)) {
					msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				}
			}

			//设置主题
			msg.setSubject(subject);

			Multipart mp = new MimeMultipart();

			//添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setContent(content == null ? "" : content, "text/html;charset=utf-8");
			mp.addBodyPart(mbpContent);

			//添加附件
			if (attachFiles != null) {
				for (File file : attachFiles) {
					MimeBodyPart mbpFile = new MimeBodyPart();
					mbpFile.attachFile(file);
					mbpFile.getInputStream();
					mp.addBodyPart(mbpFile);
				}
			}
			if(attachSource != null){
				for (Entry<String, DataSource> dataSource:attachSource.entrySet()) {
					MimeBodyPart mbpFile = new MimeBodyPart();
					mbpFile.setDataHandler(new DataHandler(dataSource.getValue()));
					mbpFile.setFileName(dataSource.getKey());
					mp.addBodyPart(mbpFile);
				}
			}
			if(imgs != null){
				for (Entry<String, DataSource> dataSource: imgs.entrySet()) {
					MimeBodyPart mbpFile = new MimeBodyPart();
					mbpFile.setDataHandler(new DataHandler(dataSource.getValue()));
					mbpFile.setHeader("Content-ID", dataSource.getKey());
					mp.addBodyPart(mbpFile);
				}
			}

			msg.setContent(mp);
			msg.setSentDate(new Date());
			msg.saveChanges();

			//发送邮件
			String protocol = this.protocol;
			if (protocol == null || "".equals(protocol)) {
				protocol = DEFAULT_PROTOCOL;
			}

			transport = session.getTransport(protocol);
			transport.connect(host, username, password);
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (AddressException e) {
			throw new MessagingException(e.getMessage());
		} catch (NoSuchProviderException e) {
			throw new MessagingException(e.getMessage());
		} catch (IOException e) {
			throw new MessagingException(e.getMessage());
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}

	public static void main(String[] args) {
		MailSender sendmail = new MailSender();

		sendmail.setHost("smtp.exmail.qq.com");
		sendmail.setUsername("aaa@mo9.com");
		sendmail.setPassword("****");
		sendmail.setFrom("aaa@mo9.com");
		sendmail.setTo("bbb@mo9.com,ccc@mo9.com");
		sendmail.setCc("ddd@mo9.com");
		sendmail.setSubject("主题");
		//sendmail.setContent("正文");
		sendmail.setContent("<p style='font-size:20px;'>标题</p><p style='color:red;'>正文1</p><p>正文2</p>");
		//sendmail.addAttachFile("C:/develop/memo.txt");

		try {
			sendmail.sendMail();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
