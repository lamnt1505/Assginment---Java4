package edu.poly.site.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.poly.common.EmailUtils;
import edu.poly.common.PageInfo;
import edu.poly.common.PageType;
import edu.poly.common.SessionUtils;
import edu.poly.dao.ShareDao;
import edu.poly.dao.UserDao;
import edu.poly.domain.Email;
import edu.poly.model.Share;
import edu.poly.model.User;
import edu.poly.model.Video;

/**
 * Servlet implementation class SharaVideoServlet
 */
@WebServlet("/ShareVideo")
public class ShareVideoServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!SessionUtils.isLogin(request)) {
			response.sendRedirect("Login");
			return;
		}
		String videoId = request.getParameter("videoId");//khai bao bien
		 
		 if(videoId == null) {
			 response.sendRedirect("/Homepage");
			 return;
		 }
		 request.setAttribute("videoId", videoId);
		 PageInfo.prepareAndForwardSite(request, response, PageType.SITE_SHARE_PAGE);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String emailAddress = request.getParameter("email");//lay 2 tt tu form share
			String videoId = request.getParameter("videoId");//lay 2 tt tu form share
			
			
			
			if(videoId == null) {
				request.setAttribute("error", "Video Id is null!");//neu = null xuat hien tb loi
			}else {//tao ra dt email de gui doi tuong nhap vao
				Email email = new Email();
				email.setFrom("lamntpd05350@fpt.edu.vn");
				email.setFromPassword("1236@Lam1236");
				email.setTo(emailAddress);
				email.setSubject("Share Favorite Video");
				StringBuilder sb = new StringBuilder();
				sb.append("Dear Ms/Mr. <br>");
				sb.append("Please click the link").append(String.format("<a href=''>View Video</a><br>", videoId));	
				sb.append("Regards<br>");
				sb.append("Administrator");
				
				email.setContent(sb.toString());
				EmailUtils.send(email);//goi thuc hien pt send de gui mail di 
				
				ShareDao dao = new ShareDao();//tao ra dt shardao luu tt
				Share share = new Share();	//tao entity dao
				share.setEmails(emailAddress);//tl tt email 
				share.setShareDate(new Date());//tl tt sharedate
				
				String username = SessionUtils.getLoginedUsername(request);//tao ra dt user
				User user = new User();
				user.setUsername(username);//tl tt users cua uestname
				
				share.setUser(user);
				Video video = new Video();//tao ra dt video 
				video.setVideoId(videoId);
				share.setVideo(video);
				
				dao.insert(share);//goi thuc hien pt insert
				request.setAttribute("message", "Video link has been sent");//hien thi tb video da dc gui 
			}
		}catch(Exception e) {
			e.printStackTrace();//in ra tb loi 
			
			request.setAttribute("error", e.getMessage());
		}
		PageInfo.prepareAndForwardSite(request, response, PageType.SITE_SHARE_PAGE);
	}

}
