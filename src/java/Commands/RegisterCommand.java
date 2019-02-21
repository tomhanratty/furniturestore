/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Business.User;
import DAO.UserDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tom
 */
public class RegisterCommand implements Command{

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String forwardToJsp = null;
        
        String username = request.getParameter("email");
        String password = request.getParameter("password");
        String first = request.getParameter("firstName");
        String last = request.getParameter("lastName");
        
        boolean adminLevel = false;
        
        if(username != null && !username.equals("") && password != null && !password.equals("") && first != null && !first.equals("")){
            UserDao uDao = new UserDao("furniturestore");
            User toAdd = new User (username,password,first,last,adminLevel);
            boolean added = uDao.addUser(toAdd);
            
            if(added != false){
                // Registration was successful, log the user in!
                User u = uDao.findUserByUsernamePassword(username, password);
                
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", u);
                
                forwardToJsp = "index.jsp";
            }else{
                // The user couldn't be added to the database
                // Send the user to the error page and inform them of this
                String errorMessage = "User couldn't be added to the database at this time"
                        + "Please <a href='register.jsp'>go back</a> and try again.<br/>Try a different username!";
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", errorMessage);
                forwardToJsp = "error.jsp";
            }
        }else{
            // One or more fields were missing
            // Send the user to the error page and inform them of this
            String errorMessage = "One or more fields were missing. Please <a href='register.jsp'>go back</a> and try again.";
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", errorMessage);
            forwardToJsp = "error.jsp";
        }
        
        return forwardToJsp;
    }
    
}