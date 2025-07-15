package controller.authen;

import constant.CommonConst;
import dal.implement.AccountDAO;
import entity.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationController extends HttpServlet {

    AccountDAO accDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get action
        String action = request.getParameter("action") != null
                ? request.getParameter("action")
                : "";
        //dua theo action set URL trang can chuyen den
        String url;
        switch (action) {
            case "login":
                url = "view/authen/login.jsp";
                break;
            case "logout":
                url = logOut(request, response);
                break;
            default:
                url = "home";
        }
        //chuyen trang
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get action
        String action = request.getParameter("action") != null
                ? request.getParameter("action")
                : "";
        
        //dua theo action de xu li request
        String url;
        switch (action) {
            case "login":
                url = loginDoPost(request, response);
                break;
            default:
                url = "home";
        }
        
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String loginDoPost(HttpServletRequest request, HttpServletResponse response) {
        String url = null;
        //get ve cac thong tin user nhap
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //kiem tra info trong db
        Account acc = Account.builder()
                             .username(username)
                             .password(password)
                             .build();
        Account accFoundByUsernamePass = accDAO.findByUsernameAndPass(acc);
        //true => trang home (set account vao trong session)
        if (accFoundByUsernamePass != null) {
            HttpSession session = request.getSession();
            session.setAttribute(CommonConst.SESSION_ACCOUNT, accFoundByUsernamePass);
            url = "home";
        //false => quay tro lai login + thong bao loi
        } else {
            request.setAttribute("error", "Username or password is incorrect!!!");
            url = "view/authen/login.jsp";
        }
        
        return url;
    }

    private String logOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(CommonConst.SESSION_ACCOUNT);
        
        return "home";
    }

}
