package com.lijie.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	
	//上传文件存储目录
	private static final String Upload_Directory = "uploadFile";
	
	//上传配置
	private static final int Memory_Threshold = 1024 * 1024 * 3; //3MB
	private static final int Max_File_Size = 1024 * 1024 * 40;//40MB
	private static final int Max_Request_Size = 1024 * 1024 * 50;//50MB
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

  //构造类  
    class Position {  
  
        int begin;  
        int end;  
  
        public Position(int begin, int end) {  
            this.begin = begin;  
            this.end = end;  
        }  
    }  
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding("UTF-8");
		//设置返回格式
		response.setContentType("text/json; charset=utf-8");
		
		//检测是否为多媒体 上传
		if (!ServletFileUpload.isMultipartContent(request)) {
			//如果不是 则停止
			System.out.println("文件格式有问题");
//			PrintWriter writer = response.getWriter();
//			writer.println("Error: 表单 必须包含 enctype = multipart/form-data");
//			writer.flush();
//			return;
		}
		
		//配置上传参数
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//设置内存临界值 超过后将产生临时文件并存储于临时目录中
		factory.setSizeThreshold(Memory_Threshold);
		//设置临时存储目录
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//设置最大文件上传值
		upload.setFileSizeMax(Max_File_Size);
		//设置最大请求值
		upload.setSizeMax(Max_Request_Size);
		//中文处理
		upload.setHeaderEncoding("UTF-8");
		
				
		//构造临时路径来存储上传的文件
		//这个路径相对应当应用的目录
		String uploadPath = request.getServletContext().getRealPath("./")  + Upload_Directory;
		System.out.println("uploadPath : " + uploadPath);
		
		//如果目录不存在 则创建
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
			System.out.println("目录不存在，新建了一个目录");
		}
		
		try {
			//解析 请求的内容提取文件数据
			java.util.List<FileItem> items = upload.parseRequest(request);
			
			for (FileItem fileItem : items) {
				if (fileItem.isFormField()) {// 如果是一个普通的表单元素(type不是file的表单元素)
				      System.out.println(fileItem.getFieldName()); // 得到对应表单元素的名字
				      System.out.println(fileItem.getString()); // 得到表单元素的值
				     } else {
				    	 	System.out.println("一个非普通的表单");
				    	 	
				    	 // 获取文件的后缀名
				    	      String fileFullName = fileItem.getName();// 得到文件的名字加后缀
				    	      //文件后缀 
				    	      String fileExt = fileFullName.substring(
				    	    		  fileFullName.lastIndexOf(".") ,
				    	    		  fileFullName.length());
				    	      String  fileName = fileFullName.substring(0, fileFullName.lastIndexOf("."));
				    	      
				    	      String timestamp = System.currentTimeMillis() + "";
				    	      System.out.println("fileName:  " + fileName + "     fileExt:  " + fileExt);
				    	      
				    	      //保存的文件路径
				    	      String path = uploadPath + "/"+ fileName + timestamp + fileExt;
				    	      
				    	      //图片再服务器的URL路径
				    	      String url = "http://" + request.getServerName() + ":"
				    	        + request.getServerPort() 
				    	        + request.getContextPath()
				    	        + "/" + Upload_Directory 
				    	        + "/" + fileName+ timestamp + fileExt;
				    	      
//				    	      String extension = path.substring(
//				    	        path.lastIndexOf(".") + 1, path.length());
//				    	      Pattern pattern = Pattern.compile("bmp|gif|gepg|png|");
//				    	      Matcher matcher = pattern.matcher(extension);
//				    	      if (matcher.find()) {
				    	    	  		fileItem.write(new File(path));
				    	    	  		System.out.println("上传成功...imageUrl = " + url);
				    	    	  		
				    	    			JSONObject jsonobj = new JSONObject(); 
				    	    			jsonobj.put("imageUrl", url);
				    	    			jsonobj.put("message", "成功啦。。。");
				    	    			//输出数据
				    	    			PrintWriter out = response.getWriter();
				    	    			out.println(jsonobj);
//				    	      }
				     }
			}
		}catch(Exception exception) {
			System.out.println("...出异常啦" + exception.getMessage());
			request.setAttribute("message", "错误信息: " + exception.getMessage());
			//跳转到 message.jsp
			request.getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
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
