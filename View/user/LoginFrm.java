package view.user;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.UserDAO;
import model.User;

public class LoginFrm extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrm() {
        super("Đăng nhập hệ thống");
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        txtPassword.setEchoChar('*');
        btnLogin = new JButton("Đăng nhập");

        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BoxLayout(pnMain, BoxLayout.PAGE_AXIS));
        pnMain.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(lblTitle.getFont().deriveFont(20.0f));
        pnMain.add(lblTitle);
        pnMain.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel pnUsername = new JPanel();
        pnUsername.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnUsername.add(new JLabel("Tài khoản:"));
        pnUsername.add(txtUsername);
        pnMain.add(pnUsername);

        JPanel pnPass = new JPanel();
        pnPass.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnPass.add(new JLabel("Mật khẩu: "));
        pnPass.add(txtPassword);
        pnMain.add(pnPass);

        JPanel pnBtn = new JPanel();
        pnBtn.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnBtn.add(btnLogin);
        pnMain.add(pnBtn);
        pnMain.add(Box.createRigidArea(new Dimension(0, 15)));

        btnLogin.addActionListener(this);

        this.setSize(400, 250);
        this.setLocationRelativeTo(null); // Center on screen
        this.setContentPane(pnMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnLogin)) {
            User user = new User();
            user.setTaiKhoan(txtUsername.getText().trim());
            user.setMatKhau(new String(txtPassword.getPassword()));

            UserDAO ud = new UserDAO();
            if (ud.checkLogin(user)) {
                if (user.getChucVu().equalsIgnoreCase("manager")) {
                    (new ManagerHomeFrm(user)).setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Chức năng dành cho vai trò " + user.getChucVu() + " đang được phát triển!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
            }
        }
    }

    public static void main(String[] args) {
        LoginFrm myFrame = new LoginFrm();
        myFrame.setVisible(true);
    }
}
