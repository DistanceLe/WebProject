package com.lijie.test;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * 
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/LoginFilter")
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		//获取请求信息
		String name = request.getParameter("name");
		System.out.println("==================================================过滤器 获取的参数 name ==  " + name);
		chain.doFilter(request, response);
		
		if ("lijie".equals(name)) {
			// pass the request along the filter chain
			// 把请求传回过滤链
			chain.doFilter(request, response);
		}else {
			//设置返回内容类型
//			response.setContentType("text/html;charset=GBK");
			
			//输出返回响应信息
//			PrintWriter out = response.getWriter();
//			out.println("name 不正确， 不能访问");
//			System.out.println("name 不正确，被拦截了");
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		
		//获取初始化参数
		String site = fConfig.getInitParameter("Site");
		
		//输出 参数
		System.out.println("网站。。 = " + site);
		
	}

}
