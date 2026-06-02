package view.user;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.User;
import view.stat.StatOptionFrm;

public class ManagerHomeFrm extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton btnStat, btnBack;
    private User user;

    public ManagerHomeFrm(User user) {
        super("Trang chủ quản lý");
        this.user = user;

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

        JPanel lblPane = new JPanel();
        lblPane.setLayout(new BoxLayout(lblPane, BoxLayout.LINE_AXIS));
        lblPane.add(Box.createRigidArea(new Dimension(350, 0)));
        JLabel lblUser = new JLabel("Đăng nhập với: " + user.getTenNV() + " (" + user.getChucVu() + ")");
        lblUser.setAlignmentX(Component.RIGHT_ALIGNMENT);
        lblPane.add(lblUser);
        listPane.add(lblPane);
        listPane.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblHome = new JLabel("TRANG CHỦ QUẢN LÝ");
        lblHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHome.setFont(lblHome.getFont().deriveFont(24.0f));
        listPane.add(lblHome);
        listPane.add(Box.createRigidArea(new Dimension(0, 30)));

        btnStat = new JButton("Xem báo cáo thống kê");
        btnStat.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStat.setPreferredSize(new Dimension(200, 40));
        btnStat.addActionListener(this);
        listPane.add(btnStat);
        listPane.add(Box.createRigidArea(new Dimension(0, 15)));

        btnBack = new JButton("Đăng xuất");
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(this);
        listPane.add(btnBack);

        this.setSize(600, 350);
        this.setLocationRelativeTo(null); // Center on screen
        this.add(listPane, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnStat)) {
            (new StatOptionFrm(user)).setVisible(true);
            this.dispose();
        } else if (e.getSource().equals(btnBack)) {
            (new LoginFrm()).setVisible(true);
            this.dispose();
        }
    }
}
