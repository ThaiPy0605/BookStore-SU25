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
        String sql = "INSERT INTO [dbo].[Account]\n"
                + "           ([username]\n"
                + "           ,[password]\n"
                + "           ,[email]\n"
                + "           ,[address]\n"
                + "           ,[roleId])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,2)";
        parameterMap = new LinkedHashMap<>();
        parameterMap.put("username", t.getUsername());
        parameterMap.put("password", t.getPassword());
        parameterMap.put("email", t.getEmail());
        parameterMap.put("address", t.getAddress());
        
        return insertGenericDAO(sql, parameterMap);
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

    public boolean checkUsernameExist(Account newAccount) {
        String url = "SELECT *\n"
                + "  FROM [dbo].[Account]\n"
                + "  WHERE [username] = ?";
        parameterMap = new LinkedHashMap<>();
        parameterMap.put("username", newAccount.getUsername());

        return !queryGenericDAO(Account.class, url, parameterMap).isEmpty();
    }

}
