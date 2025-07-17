package dal.implement;

import dal.GenericDAO;
import entity.OrderDetails;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderDetailsDAO extends GenericDAO<OrderDetails> {

    @Override
    public List<OrderDetails> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int insert(OrderDetails t) {
        String sql = "INSERT INTO [dbo].[OrderDetails]\n"
                + "           ([quantity]\n"
                + "           ,[productId]\n"
                + "           ,[orderId])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?)";
        
        parameterMap = new LinkedHashMap<>();
        parameterMap.put("1", t.getQuantity());
        parameterMap.put("2", t.getProductId());
        parameterMap.put("3", t.getOrderId());
        
        return insertGenericDAO(sql, parameterMap);
    }

}
