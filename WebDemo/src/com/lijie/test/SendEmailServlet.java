package com.lijie.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class SendEmailServlet
 */
@WebServlet("/SendEmailServlet")
public class SendEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendEmailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public class Auth extends Authenticator {  
    	  
        private String username = "";  
        private String password = "";  
      
        public Auth(String username, String password) {  
            this.username = username;  
            this.password = password;  
        }  
        public PasswordAuthentication getPasswordAuthentication() {  
            return new PasswordAuthentication(username, password);  
        }   
    }  
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//收件人的邮箱
		String to = "994482177@qq.com";
		
		//发件人的邮箱
		String from = "jie.li@ce-link.com";
		
		//从本地主机发送
		String  host = "mail.ce-link.com";
		
		//获取系统属性
		Properties properties = System.getProperties();
		
		//设置邮件服务器
		properties.setProperty("mail.smtp.host", host);// 设置SMTP主机  
		properties.setProperty("mail.smtp.port", "25");// 设置服务端口号  
		properties.setProperty("mail.smtp.auth", "true");// 同时通过验证  
		
		//获取默认的session
		Session session = Session.getDefaultInstance(properties, new Auth(from, "oa.rd20jl"));
		
		
		//设置响应的内容类型
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		try {
			//创建一个默认的MimeMessage对象
			MimeMessage message = new MimeMessage(session);
			//设置发信人  
			message.setFrom(new InternetAddress(from));
			//设置收信人  
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			//设置 subject
			message.setSubject("这个是主题哦!");
			//设置实际消息
			message.setText("消息主体，想要发送的消息就写在这里就好了");
			//发送消息
			Transport.send(message);
		
			//回调
			String title = "发送邮件";
			String res = "成功发送消息";
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", res);
			jsonObject.put("title", title);
			
			out.println(jsonObject);
			
		} catch (MessagingException exception) {
			// TODO: handle exception
			exception.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
