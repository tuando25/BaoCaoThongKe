package test.unit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import dao.DAO;
import dao.ReportDAO;
import model.GiaoDich;
import model.TableStat;
public class ReportDAOTest {
    private static ReportDAO reportDAO;
    private static SimpleDateFormat sdf;
    @BeforeClass
    public static void setUp() {
        new DAO();
        reportDAO = new ReportDAO();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    @Test
    public void testGetRevenueByTableSuccess() throws Exception {
        Date start = sdf.parse("01/05/2026 00:00:00");
        Date end = sdf.parse("31/05/2026 23:59:59");
        ArrayList<TableStat> result = reportDAO.getRevenueByTable(start, end);
        Assert.assertNotNull("Result list should not be null", result);
        Assert.assertEquals("There should be statistics for 2 tables", 2, result.size());
        // TableStat is sorted by revenue DESC, so Ban 01 should be first (456,000 VND > 103,000 VND)
        TableStat firstStat = result.get(0);
        Assert.assertEquals("First table should be 'Ban 01'", "Ban 01", firstStat.getBan().getTenBan());
        Assert.assertEquals("Ban 01 should have 2 visits", 2, firstStat.getTongLuotKhach());
        Assert.assertEquals("Ban 01 revenue should be 456000", 456000f, firstStat.getDoanhThu(), 0.01);
        TableStat secondStat = result.get(1);
        Assert.assertEquals("Second table should be 'Ban 02'", "Ban 02", secondStat.getBan().getTenBan());
        Assert.assertEquals("Ban 02 should have 1 visit", 1, secondStat.getTongLuotKhach());
        Assert.assertEquals("Ban 02 revenue should be 103000", 103000f, secondStat.getDoanhThu(), 0.01);
    }
    @Test
    public void testGetRevenueByTableNoData() throws Exception {
        Date start = sdf.parse("01/01/2025 00:00:00");
        Date end = sdf.parse("31/12/2025 23:59:59");
        ArrayList<TableStat> result = reportDAO.getRevenueByTable(start, end);
        Assert.assertNotNull("Result list should not be null", result);
        Assert.assertTrue("Result list should be empty", result.isEmpty());
    }
    @Test
    public void testGetDetailRevenueByTableSuccess() throws Exception {
        Date start = sdf.parse("01/05/2026 00:00:00");
        Date end = sdf.parse("31/05/2026 23:59:59");
        int tableId = 1; // Ban 01
        ArrayList<GiaoDich> result = reportDAO.getDetailRevenueByTable(tableId, start, end);
        Assert.assertNotNull("Transaction list should not be null", result);
        Assert.assertEquals("Ban 01 should have 2 transactions in May 2026", 2, result.size());
        // Transactions are sorted by date DESC
        GiaoDich gd1 = result.get(0); // Latest transaction: 2026-05-15
        Assert.assertEquals("Transaction total should be 313000", 313000f, gd1.getTongTien(), 0.01);
        Assert.assertNotNull("Client details should be loaded", gd1.getKhachHang());
        Assert.assertEquals("Client name should be 'Pham Khach VIP'", "Pham Khach VIP", gd1.getKhachHang().getTenKH());
        Assert.assertFalse("Dishes list should not be empty", gd1.getListChiTietGiaoDich().isEmpty());
        Assert.assertFalse("Services list should not be empty", gd1.getListUsedService().isEmpty());
        GiaoDich gd2 = result.get(1); // Earlier transaction: 2026-05-10
        Assert.assertEquals("Transaction total should be 143000", 143000f, gd2.getTongTien(), 0.01);
        Assert.assertEquals("Client name should be 'Nguyen Khach Le'", "Nguyen Khach Le", gd2.getKhachHang().getTenKH());
    }
    @Test
    public void testGetDetailRevenueByTableNoData() throws Exception {
        Date start = sdf.parse("01/01/2025 00:00:00");
        Date end = sdf.parse("31/12/2025 23:59:59");
        int tableId = 1;
        ArrayList<GiaoDich> result = reportDAO.getDetailRevenueByTable(tableId, start, end);
        Assert.assertNotNull("Result should not be null", result);
        Assert.assertTrue("Result should be empty", result.isEmpty());
    }
}
