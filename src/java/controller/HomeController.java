package controller;

import constant.CommonConst;
import dal.implement.CategoryDAO;
import dal.implement.ProductDAO;
import entity.Category;
import entity.PageControl;
import entity.Product;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public class HomeController extends HttpServlet {

    ProductDAO productDAO = new ProductDAO();
    CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PageControl pageControl = new PageControl();
        
        List<Product> listProd = findProductDoGet(request, pageControl);

        //get list categoryDAO
        List<Category> listCate = categoryDAO.findAll();

        //set listProd, listCate to session
        HttpSession session = request.getSession();
        session.setAttribute("listProd", listProd);
        session.setAttribute("listCate", listCate);
        request.setAttribute("pageControl", pageControl);

        request.getRequestDispatcher("view/homepage/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private List<Product> findProductDoGet(HttpServletRequest request, PageControl pageControl) {
        //get ve page
        String pageRaw = request.getParameter("page");
        //validate page
        int page;
        try {
            page = Integer.parseInt(pageRaw);
            if(page <= 0) {
                page = 1;
            }
        } catch (NumberFormatException ignored) {
            page = 1;
        }
        //get ve search
        String actionSearch = request.getParameter("search") == null 
                ? "default" 
                : request.getParameter("search");
        //get list productDAO
        List<Product> listProd;
        //get request URL
        String requestURL = request.getRequestURL().toString();
        
        //total record
        int totalRecord;
        switch (actionSearch) {
            case "category":
                String categoryId = request.getParameter("categoryId");
                totalRecord = productDAO.findTotalRecordByCategory(categoryId);
                listProd = productDAO.findByCategory(categoryId, page);
                pageControl.setUrlPattern(requestURL + "?search=category&categoryId=" + categoryId + "&");
                break;
            case "searchByName":
                String keyword = request.getParameter("keyword");
                totalRecord = productDAO.findTotalRecordByName(keyword);
                listProd = productDAO.findByName(keyword, page);
                pageControl.setUrlPattern(requestURL + "?search=searchByName&keyword=" + keyword + "&");
                break;
            default:
                listProd = productDAO.findAll();
                totalRecord = productDAO.findTotalRecord();
                listProd = productDAO.findByPage(page);
                pageControl.setUrlPattern(requestURL + "?");
        }
        
        
        //total page
        int totalPage = (totalRecord % CommonConst.recordPerPage) == 0 
                ? (totalRecord / CommonConst.recordPerPage)
                : (totalRecord / CommonConst.recordPerPage) + 1;
        //set total record, total page, page vao pageControl
        pageControl.setPage(page);
        pageControl.setTotalRecord(totalRecord);
        pageControl.setTotalPage(totalPage);
        return listProd;
    }

}
