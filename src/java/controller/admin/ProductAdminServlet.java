package controller.admin;

import dal.implement.CategoryDAO;
import dal.implement.ProductDAO;
import entity.Product;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig
public class ProductAdminServlet extends HttpServlet {
    
    ProductDAO productDAO = new ProductDAO();
    CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //set endcoding UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        //get session
        HttpSession session = request.getSession();
        
        //get action
        String action = request.getParameter("action") == null
                ? ""
                : request.getParameter("action");
        switch (action) {
            case "add":
                addProduct(request);
                break;
            case "delete":
                deleteProduct(request);
                break;
            default:
                
        }
        response.sendRedirect("dashboard");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void addProduct(HttpServletRequest request) {
        try {
            //get name
            String name = request.getParameter("name");
            //get price
            int price = Integer.parseInt(request.getParameter("price"));
            //get quantity
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            //get descript
            String description = request.getParameter("description");
            //get cateId
            int categoryId = Integer.parseInt(request.getParameter("category"));
            
            //image
            Part part = request.getPart("image");
            
            //duong dan luu anh
            String path = request.getServletContext().getRealPath("/images");
            File dir = new File(path);
            
            //xem duong dan nay da ton tai chua
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            File image = new File(dir, part.getSubmittedFileName());
            
            //ghi file vao trong duong dan
            part.write(image.getAbsolutePath());
            
            //lay ra cai context path cua project
            String pathOfFile = request.getContextPath() + "/images/" + image.getName();
            
            Product newProduct = Product.builder()
                                     .name(name)
                                     .price(price)
                                     .quantity(quantity)
                                     .categoryId(categoryId)
                                     .description(description)
                                     .image(pathOfFile)
                                     .build();
            
            
            productDAO.insert(newProduct);
        } catch (NumberFormatException | IOException | ServletException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteProduct(HttpServletRequest request) {
        //get id
        int id = Integer.parseInt(request.getParameter("id"));
        
        productDAO.deleteById(id);
    }
}
