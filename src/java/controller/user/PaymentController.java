package controller.user;

import entity.Order;
import entity.OrderDetails;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
            default:
                throw new AssertionError();
        }
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
        response.sendRedirect("payment");
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

}
