package controller.user;

import constant.CommonConst;
import dal.implement.OrderDAO;
import dal.implement.OrderDetailsDAO;
import entity.Account;
import entity.Order;
import entity.OrderDetails;
import entity.Product;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import javafx.util.converter.LocalDateTimeStringConverter;

public class PaymentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/user/payment/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null
                ? ""
                : request.getParameter("action");
        switch (action) {
            case "addProductToCart":
                addProductToCart(request, response);
                break;
            case "changeQuantity":
                changeQuantity(request, response);
                break;
            case "deleteProductFromCart":
                deleteProductFromCart(request, response);
                break;
            case "checkout":
                checkout(request, response);
                break;
            default:
                throw new AssertionError();
        }
        response.sendRedirect("payment");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //get ve sesion
        HttpSession session = request.getSession();
        //get ve productId
        int id = Integer.parseInt(request.getParameter("id"));
        //get ve quantity
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        //lay ve cart tren session
        Order cart = (Order) session.getAttribute("cart");

        if (cart == null) {
            cart = new Order();
        }
        OrderDetails od = new OrderDetails();
        od.setProductId(id);
        od.setQuantity(quantity);

        //them orderdetails vao trong cart
        addOrderDetailsToOrder(od, cart);
        //set cart moi len tren session
        session.setAttribute("cart", cart);
    }

    private void addOrderDetailsToOrder(OrderDetails od, Order cart) {
        boolean isAdded = false;
        for (OrderDetails obj : cart.getListOrderDetails()) {
            if (obj.getProductId() == od.getProductId()) {
                obj.setQuantity(obj.getQuantity() + od.getQuantity());
                isAdded = true;
            }
        }
        if (isAdded == false) {
            cart.getListOrderDetails().add(od);
        }
    }

    private void changeQuantity(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {
            //get ve productId can thay doi quantity
            int productId = Integer.parseInt(request.getParameter("id"));
            //get ve quantity
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            //lay ve cart
            Order cart = (Order) session.getAttribute("cart");
            //thay doi quantity
            for (OrderDetails obj : cart.getListOrderDetails()) {
                if (obj.getProductId() == productId) {
                    obj.setQuantity(quantity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteProductFromCart(HttpServletRequest request, HttpServletResponse response) {
        //get ve productId
        int productId = Integer.parseInt(request.getParameter("id"));
        
        HttpSession session = request.getSession();
        Order cart = (Order) session.getAttribute("cart");
        OrderDetails od = null;
        
        for (OrderDetails obj : cart.getListOrderDetails()) {
            if (obj.getProductId() == productId) {
                od = obj;
            }
        }
        
        cart.getListOrderDetails().remove(od);
        session.setAttribute("cart", cart);
    }

    private void checkout(HttpServletRequest request, HttpServletResponse response) {
        //lay ve cart
        HttpSession session = request.getSession();
        Order cart = (Order) session.getAttribute("cart");
        //lay ve accountId
        int accountId = ((Account) session.getAttribute(CommonConst.SESSION_ACCOUNT)).getId();
        
        List<Product> list = (List<Product>) session.getAttribute(CommonConst.SESSION_PRODUCT);
        //amount
        int amount = caculateAmount(cart, list);
        //set info
        cart.setAccountId(accountId);
        cart.setAmount(amount);
        cart.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        //insert order
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailsDAO odDAO = new OrderDetailsDAO();
        int orderId = orderDAO.insert(cart);
        for (OrderDetails obj : cart.getListOrderDetails()) {
            obj.setOrderId(orderId);
            odDAO.insert(obj);
        }
        //tru di so luong san pham trong DB
        
        session.removeAttribute("cart");
    }

    private int caculateAmount(Order cart, List<Product> list) {
        int amount = 0;
        for (OrderDetails od : cart.getListOrderDetails()) {
            amount += (od.getQuantity() * findPriceById(list, od.getProductId()));
        }
        
        return amount;
    }

    private float findPriceById(List<Product> list, int productId) {
        for (Product obj : list) {
            if (obj.getId() == productId) {
                return obj.getPrice();
            }
        }
        
        return 0;
    }



}
