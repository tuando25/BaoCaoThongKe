package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Ban;
import model.ChiTietGiaoDich;
import model.DichVu;
import model.GiaoDich;
import model.KhachHang;
import model.MonAn;
import model.TableStat;
import model.UsedService;
import model.User;

public class ReportDAO extends DAO {

    public ReportDAO() {
        super();
    }

    public ArrayList<TableStat> getRevenueByTable(Date start, Date end) {
        ArrayList<TableStat> result = new ArrayList<>();
        String sql = "SELECT b.id, b.tenBan, b.kieu, b.khuVuc, "
                   + "COUNT(g.id) AS tongLuotKhach, "
                   + "SUM(g.tongTien) AS doanhThu "
                   + "FROM tblBan b "
                   + "INNER JOIN tblGiaoDich g ON b.id = g.idBan "
                   + "WHERE g.ngayGiaoDich >= ? AND g.ngayGiaoDich <= ? "
                   + "GROUP BY b.id, b.tenBan, b.kieu, b.khuVuc "
                   + "ORDER BY doanhThu DESC, tongLuotKhach DESC";
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            if (con == null) {
                new DAO();
            }
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, sdf.format(start));
                ps.setString(2, sdf.format(end));
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Ban ban = new Ban();
                    ban.setId(rs.getInt("id"));
                    ban.setTenBan(rs.getString("tenBan"));
                    ban.setKieu(rs.getString("kieu"));
                    ban.setKhuVuc(rs.getString("khuVuc"));

                    TableStat stat = new TableStat();
                    stat.setBan(ban);
                    stat.setTongLuotKhach(rs.getInt("tongLuotKhach"));
                    stat.setDoanhThu(rs.getFloat("doanhThu"));

                    result.add(stat);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<GiaoDich> getDetailRevenueByTable(int tableId, Date start, Date end) {
        ArrayList<GiaoDich> result = new ArrayList<>();
        String sqlGiaoDich = "SELECT g.id, g.ngayGiaoDich, g.tongTien, "
                           + "kh.id AS kh_id, kh.tenKH, kh.soDT, "
                           + "u.id AS u_id, u.tenNV, u.chucVu, "
                           + "b.id AS b_id, b.tenBan, b.kieu, b.khuVuc "
                           + "FROM tblGiaoDich g "
                           + "LEFT JOIN tblKhachHang kh ON g.idKhachHang = kh.id "
                           + "LEFT JOIN tblUser u ON g.idUser = u.id "
                           + "LEFT JOIN tblBan b ON g.idBan = b.id "
                           + "WHERE g.idBan = ? AND g.ngayGiaoDich >= ? AND g.ngayGiaoDich <= ? "
                           + "ORDER BY g.ngayGiaoDich DESC";

        String sqlChiTietGiaoDich = "SELECT ctgd.id, ctgd.soLuong, ctgd.giaBan, "
                                  + "m.id AS m_id, m.tenMon, m.danhMuc, m.donGia "
                                  + "FROM tblChiTietGiaoDich ctgd "
                                  + "INNER JOIN tblMonAn m ON ctgd.idMonAn = m.id "
                                  + "WHERE ctgd.idGiaoDich = ?";

        String sqlUsedService = "SELECT us.id, us.soLuong, us.thanhTien, "
                              + "d.id AS d_id, d.tenDichVu, d.donGia "
                              + "FROM tblUsedService us "
                              + "INNER JOIN tblDichVu d ON us.idDichVu = d.id "
                              + "WHERE us.idGiaoDich = ?";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            if (con == null) {
                new DAO();
            }
            if (con != null) {
                PreparedStatement psGD = con.prepareStatement(sqlGiaoDich);
                psGD.setInt(1, tableId);
                psGD.setString(2, sdf.format(start));
                psGD.setString(3, sdf.format(end));
                ResultSet rsGD = psGD.executeQuery();

                while (rsGD.next()) {
                    GiaoDich gd = new GiaoDich();
                    gd.setId(rsGD.getInt("id"));
                    gd.setNgayGiaoDich(rsGD.getTimestamp("ngayGiaoDich"));
                    gd.setTongTien(rsGD.getFloat("tongTien"));

                    Ban ban = new Ban();
                    ban.setId(rsGD.getInt("b_id"));
                    ban.setTenBan(rsGD.getString("tenBan"));
                    ban.setKieu(rsGD.getString("kieu"));
                    ban.setKhuVuc(rsGD.getString("khuVuc"));
                    gd.setBan(ban);

                    KhachHang kh = new KhachHang();
                    kh.setId(rsGD.getInt("kh_id"));
                    kh.setTenKH(rsGD.getString("tenKH"));
                    kh.setSoDT(rsGD.getString("soDT"));
                    gd.setKhachHang(kh);

                    User u = new User();
                    u.setId(rsGD.getInt("u_id"));
                    u.setTenNV(rsGD.getString("tenNV"));
                    u.setChucVu(rsGD.getString("chucVu"));
                    gd.setUser(u);

                    PreparedStatement psCT = con.prepareStatement(sqlChiTietGiaoDich);
                    psCT.setInt(1, gd.getId());
                    ResultSet rsCT = psCT.executeQuery();
                    List<ChiTietGiaoDich> listCT = new ArrayList<>();
                    while (rsCT.next()) {
                        MonAn ma = new MonAn();
                        ma.setId(rsCT.getInt("m_id"));
                        ma.setTenMon(rsCT.getString("tenMon"));
                        ma.setDanhMuc(rsCT.getString("danhMuc"));
                        ma.setDonGia(rsCT.getFloat("donGia"));

                        ChiTietGiaoDich ct = new ChiTietGiaoDich();
                        ct.setId(rsCT.getInt("id"));
                        ct.setSoLuong(rsCT.getInt("soLuong"));
                        ct.setGiaBan(rsCT.getFloat("giaBan"));
                        ct.setMonAn(ma);
                        listCT.add(ct);
                    }
                    gd.setListChiTietGiaoDich(listCT);

                    PreparedStatement psUS = con.prepareStatement(sqlUsedService);
                    psUS.setInt(1, gd.getId());
                    ResultSet rsUS = psUS.executeQuery();
                    List<UsedService> listUS = new ArrayList<>();
                    while (rsUS.next()) {
                        DichVu dv = new DichVu();
                        dv.setId(rsUS.getInt("d_id"));
                        dv.setTenDichVu(rsUS.getString("tenDichVu"));
                        dv.setDonGia(rsUS.getFloat("donGia"));

                        UsedService us = new UsedService();
                        us.setId(rsUS.getInt("id"));
                        us.setSoLuong(rsUS.getInt("soLuong"));
                        us.setThanhTien(rsUS.getFloat("thanhTien"));
                        us.setService(dv);
                        listUS.add(us);
                    }
                    gd.setListUsedService(listUS);

                    result.add(gd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
