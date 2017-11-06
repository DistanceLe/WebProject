package com.lijie.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 使用 UTF-8 设置中文正常显示 优先级高
		
		
		ServletContext context  = this.getServletContext();
		context.log("HelloServlet come in " + "一次Get请求");
		
		response.setCharacterEncoding("UTF-8");
		//设置返回格式
		response.setContentType("text/json; charset=utf-8");
		
		// 设置错误代码和原因    模拟一个404错误，会跳到 ErrorHandler 页面
//	     response.sendError(404, "Need authentication!!!" ); 
		
	     
	     //获取cookie
	     Cookie[] cookies = request.getCookies();
	     Cookie cookie = null;
	     if (cookies != null) {
	    	 for (int i = 0; i < cookies.length; i++){
	             cookie = cookies[i];
	             System.out.print("cookie名称  ==  " + cookie.getName());
	             System.out.println("    cookie值：" +  cookie.getValue());
	          }
		}
	     
	     //Cookie
	     Cookie name = new Cookie("name", "xiaoming");
	     name.setMaxAge(60*60*24);  //24小时后过期   设为0，表示删除这个cookie
	     response.addCookie(name);
	     
	     
	     //Session  记录一个访问的session，类似cookie功能
	     HttpSession session = request.getSession(true);//不存在 则新建一个
	     Date createTime = new Date(session.getCreationTime());//创建时间
	     Date lastAccessTime = new Date(session.getLastAccessedTime());//网页最后一次访问时间
	     //设置日期输出的格式  
	     SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	     Integer visitCount = 0 ;
	     String userID = "";
	     
	     //检测是否有新的 访问者
	     if (session.isNew()) {
	    	 	userID = "id" + visitCount + session.getId();
			session.setAttribute("userID", userID);
		}else {
			userID  = (String)session.getAttribute("userID");
			visitCount = (Integer) session.getAttribute("visitCount") ;
			if (visitCount == null) {
				visitCount = 0;
			}
			visitCount += 1;
		}
	     session.setAttribute("visitCount", visitCount);
	     System.out.println("userID: " +userID  + "  visitCount:" + visitCount + "  createTime:" + df.format(createTime) + "  lastAccessTime:" + df.format(lastAccessTime));
	     
	     
	     
	     
		//获取参数
		Enumeration<String> params = request.getParameterNames();
		while(params.hasMoreElements()) {
			System.out.println("ParameterName == " + params.nextElement());
		}
		
		
		
		//获取key 和 value
		Map<String, ?> maps = request.getParameterMap();
		Set<String> sets = maps.keySet();
		Iterator<String> iterators = sets.iterator(); 
		while (iterators.hasNext()) {
			String keyName = (String) iterators.next();
			System.out.println("ParameterMap == " + keyName);
			
			Object values = maps.get(keyName);
			if (values instanceof String[]) {
				String[] strValues = (String[])values;
				System.out.println(Arrays.toString(strValues));
			}
		}
		
		//请求头
		Enumeration<?> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String paramName = (String) headerNames.nextElement();
			System.out.print("headParamName = "+paramName);
			String paramValue = request.getHeader(paramName);
			System.out.println("   Value = " + paramValue);
		}
		
		//设置自动刷新  间隔5秒
		//response.setIntHeader("Refresh", 5);
		
		PrintWriter out = response.getWriter();
		
		try {
			//Class.forName(String): 加载类，并且执行类初始化；
			//可以通过Class.forName(String, boolean, ClassLoader)第二个参数来仅仅加载类不执行初始化；
			//ClassLoader.loadClass(String): 仅仅加载类，不执行类初始化；
			
			//1.加载驱动，使用反射知识  
			Class.forName("com.mysql.jdbc.Driver");
			//2.使用DriverManager获取数据库连接，其中返回的Connection就代表了Java程序和数据库的连接  
            //不同数据库的URL写法需要查看驱动文档，用户名、密码由DBA分配  
			Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/myDB?useSSL=false", "root", "123456");
			
			//3.使用Connection来创建一个Statement对象  
			Statement stmt = conn.createStatement();
			
			//4.执行SQL    三种方法
			String sql = "select* from users";
			ResultSet rs = stmt.executeQuery(sql);
			JSONArray jsonarray = new JSONArray();  
			JSONObject jsonobj = new JSONObject(); 
			
			//展开结果集数据库
			while(rs.next()) {
				//通过字段检索
				jsonobj.put("名字", rs.getString("name"));
				jsonobj.put("年龄", rs.getString("age"));
				jsonobj.put("密码", rs.getString("password"));
				jsonobj.put("userID", rs.getString("id"));
				jsonarray.add(jsonobj);
			}
			
			//输出数据
			out = response.getWriter();
			out.println(jsonarray);
			
			//完成后关闭
			rs.close();
			stmt.close();
			conn.close();
			
		}catch (Exception e) {
			out.print("get data error!");
			e.printStackTrace();
		}
		
		
//		response.getWriter().append("收到..GET: Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
