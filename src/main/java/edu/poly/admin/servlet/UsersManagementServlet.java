package edu.poly.admin.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import edu.poly.common.PageInfo;
import edu.poly.common.PageType;
import edu.poly.dao.UserDao;
import edu.poly.model.User;


@WebServlet({"/UsersManagementServlet","/UsersManagementServlet/create","/UsersManagementServlet/update"
	,"/UsersManagementServlet/edit","/UsersManagementServlet/delete","/UsersManagementServlet/reset"})
public class UsersManagementServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getRequestURL().toString();
		User user = null;
		if (url.contains("delete")) {
			delete(request,response);
			user = new User();
			request.setAttribute("user", user);
		} else if (url.contains("edit")) {
			edit(request,response);
		} else if (url.contains("reset")) {
			user = new User();
			request.setAttribute("user", user);
		}
		findAll(request,response);
		PageInfo.prepareAndForward(request, response, PageType.USER_MANAGEMENT_PAGE);
	}

	
	private void findAll(HttpServletRequest request, HttpServletResponse response) {
		try {
			UserDao dao = new UserDao();
			

			List<User> list = dao.findAll();
			
			request.setAttribute("users", list);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Error:" + e.getMessage());
		}
	}


	private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("username"); //lay id do nguoi dung gui toi
		
		if(id == null) { //neu id = null thi bao cho nguoi dung biet va nhap vao id
			request.setAttribute("error", "Usernameid is required !!!!");
			PageInfo.prepareAndForward(request, response, PageType.USER_MANAGEMENT_PAGE);
			return;
		}
		try {
			UserDao dao = new UserDao();
			
			User user = dao.findById(id);
			request.setAttribute("user", user);
			findAll(request,response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Error:" + e.getMessage());
		}
		PageInfo.prepareAndForward(request, response, PageType.USER_MANAGEMENT_PAGE);
	}


	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("username");
		try {
			UserDao dao = new UserDao();
			User user = dao.findById(id);
			
			if(user == null) { //neu video khong tim thay trong csdl thi thong bao cho ng dung biet
				request.setAttribute("error", "User id is not found");
					PageInfo.prepareAndForward(request, response, PageType.USER_MANAGEMENT_PAGE);
				return;
			}
			//nguoc lai goi phuong thuc delete theo id
			dao.delete(id);
			request.setAttribute("message", "User delete!!!");
			findAll(request,response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Error:" + e.getMessage());
		}
		PageInfo.prepareAndForward(request, response, PageType.USER_MANAGEMENT_PAGE);//ht lai nd view 
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURL().toString();
		if (url.contains("create")) {
			insert(request, response);
		} else if (url.contains("delete")) {
			delete(request,response);
			
			request.setAttribute("user", new User());
		} else if (url.contains("update")) {
			update(request,response);
		} else if (url.contains("reset")) {
			request.setAttribute("user", new User());
		}
		findAll(request,response);
		PageInfo.prepareAndForward(request, response, PageType.USER_MANAGEMENT_PAGE);//ht lai nd view 
	}


	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = new User();
			BeanUtils.populate(user, request.getParameterMap());

			UserDao dao = new UserDao();
			dao.update(user);
			request.setAttribute("user",user);
			request.setAttribute("message", "User update!!!");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Error:" + e.getMessage());
		}
		PageInfo.prepareAndForward(request, response, PageType.USER_MANAGEMENT_PAGE);//ht lai nd view 

	}


	private void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		try {
			BeanUtils.populate(user, request.getParameterMap());

			UserDao dao = new UserDao();
			dao.insert(user);
			request.setAttribute("user", user);
			request.setAttribute("message", "User inserted!!!");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Error:" + e.getMessage());
		}
		findAll(request,response);
		PageInfo.prepareAndForward(request, response, PageType.USER_MANAGEMENT_PAGE);//ht lai nd view 
	}

}
