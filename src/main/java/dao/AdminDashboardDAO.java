package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DBContext;

public class AdminDashboardDAO {

    // Tổng số xe
    public int getCarCount() {
        return getCount("SELECT COUNT(*) FROM car");
    }

    // Tổng số hãng xe
    public int getBrandCount() {
        return getCount("SELECT COUNT(*) FROM brand");
    }

    public int getStaffCount() {
        return getCount("SELECT COUNT(*) FROM staff"); // thêm []
    }

    public int getOrderCount() {
        return getCount("SELECT COUNT(*) FROM Orders"); // thêm []
    }


    // Hàm chung để lấy count
    private int getCount(String sql) {
        int count = 0;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
