package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;

public class UserDAO extends DAO {
    
    public UserDAO() {
        super();
    }

    public boolean checkLogin(User user) {
        boolean result = false;
        String sql = "SELECT id, tenNV, chucVu FROM tblUser WHERE taiKhoan = ? AND matKhau = ?";
        try {
            if (con == null) {
                new DAO();
            }
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, user.getTaiKhoan());
                ps.setString(2, user.getMatKhau());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setTenNV(rs.getString("tenNV"));
                    user.setChucVu(rs.getString("chucVu"));
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
