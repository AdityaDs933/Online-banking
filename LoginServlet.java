/******************************************************************************
*	Program Author: Dr. Yongming Tang for CSCI 6810 Java and the Internet	  *
*	Date: September, 2012													  *
*******************************************************************************/

import java.io.*;
import javax.servlet.*;  //package for GenericServlet
import javax.servlet.http.*;  //package for HttpServlet
import javax.servlet.annotation.WebServlet;
import java.util.*;
import com.aditya.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/OnlineLoginServlet")
public class LoginServlet extends HttpServlet {
   private String Username, Password;
   private PrintWriter output;
   private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

   //a method called automatically to initialize the servlet
   public void init( ServletConfig config ) throws ServletException
   {
      super.init( config );
      Username = new String("");
      Password = new String("");
   }

   //a method included in class HttpServlet to respond
   //to post requests from a client.
   public void doGet ( HttpServletRequest req, HttpServletResponse res )
      throws ServletException, IOException
   {	doPost(req, res);
   }

   //a method included in class HttpServlet to respond
   //to post requests from a client.
   public void doPost ( HttpServletRequest req, HttpServletResponse res )
      throws ServletException, IOException
   {
      //obtains a character-based output stream that enables
      //text data to be sent to the client
      output = res.getWriter();

      //specifies the MIME type of the response to the browser
      res.setContentType( "text/html" );

      //returns the value associated with a parameter sent to
      //the servlet as part of a post request
      Username = req.getParameter( "UserName" );
      Password = req.getParameter( "PassWord" );
      Account Acct = new Account(Username, Password);
      String CustomerName = Acct.signIn();
        if (!CustomerName.equals("")){
           logger.info("login username={}", Username);
           // Prevent session fixation: invalidate existing session and create a new one
           HttpSession oldSession = req.getSession(false);
           if (oldSession != null) {
              try { oldSession.invalidate(); } catch (IllegalStateException e) { }
           }
           HttpSession session = req.getSession(true);
           // Set a reasonable session timeout (15 minutes)
           session.setMaxInactiveInterval(15 * 60);

           // Sanitize values before storing/forwarding
           String safeUsername = com.aditya.InputValidator.escapeHtml(Username);
           String safeCustomerName = com.aditya.InputValidator.escapeHtml(CustomerName);

           // Store both request and session attributes (JSPs may read either)
           req.setAttribute("Username", safeUsername);
           req.setAttribute("CustomerName", safeCustomerName);
           session.setAttribute("Username", safeUsername);
           session.setAttribute("CustomerName", safeCustomerName);

           // Set HttpOnly cookie for session (add Secure when using HTTPS)
           javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("JSESSIONID", session.getId());
           cookie.setHttpOnly(true);
           cookie.setPath(req.getContextPath() == null ? "/" : req.getContextPath());
           if (req.isSecure()) cookie.setSecure(true);
           res.addCookie(cookie);

           req.getRequestDispatcher("/CSCI6810/afterlogin.jsp").forward(req, res);
        } else {
           output.println("login failed");
        }
   }


   //this "cleanup" method is called when a servlet is terminated by the server
   public void destroy() {
       output.close();
   }
}