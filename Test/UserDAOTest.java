package test.unit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import dao.DAO;
import dao.UserDAO;
import model.User;

public class UserDAOTest {
    private static UserDAO userDAO;

    @BeforeClass
    public static void setUp() {
        new DAO();
        userDAO = new UserDAO();
    }

    public void testCheckLoginSuccess() {
        User user = new User();
        user.setTaiKhoan("admin");
        user.setMatKhau("admin123");

        boolean result = userDAO.checkLogin(user);

        Assert.assertTrue("Đăng nhập phải thành công", result);
        Assert.assertEquals("Tên nhân viên phải là 'Nguyen Van A'", "Nguyen Van A", user.getTenNV());
        Assert.assertEquals("Chức vụ phải là 'manager'", "manager", user.getChucVu());
        Assert.assertTrue("User ID phải được cập nhật và lớn hơn 0", user.getId() > 0);
    }

    @Test
    public void testCheckLoginWrongPassword() {
        User user = new User();
        user.setTaiKhoan("admin");
        user.setMatKhau("wrong_password");

        boolean result = userDAO.checkLogin(user);

        Assert.assertFalse("Đăng nhập phải thất bại do sai mật khẩu", result);
        Assert.assertNull("Tên nhân viên phải giữ nguyên là null", user.getTenNV());
        Assert.assertNull("Chức vụ phải giữ nguyên là null", user.getChucVu());
    }

    @Test
    public void testCheckLoginNonExistentUser() {
        User user = new User();
        user.setTaiKhoan("non_existent");
        user.setMatKhau("some_password");

        boolean result = userDAO.checkLogin(user);

        Assert.assertFalse("Đăng nhập phải thất bại với tài khoản không tồn tại", result);
    }
}