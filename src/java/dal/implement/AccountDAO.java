package dal.implement;

import dal.GenericDAO;
import entity.Account;
import java.util.LinkedHashMap;
import java.util.List;

public class AccountDAO extends GenericDAO<Account> {

    @Override
    public List<Account> findAll() {
        return queryGenericDAO(Account.class);
    }

    @Override
    public int insert(Account t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Account findByUsernameAndPass(Account acc) {
        String sql = "SELECT *\n"
                + "  FROM [dbo].[Account]\n"
                + "  WHERE username = ? and password = ?";
        
        parameterMap = new LinkedHashMap<>();
        parameterMap.put("username", acc.getUsername());
        parameterMap.put("password", acc.getPassword());
        
        List<Account> list = queryGenericDAO(Account.class, sql, parameterMap);
        
        return list.isEmpty() ? null : list.get(0);
    }

}
