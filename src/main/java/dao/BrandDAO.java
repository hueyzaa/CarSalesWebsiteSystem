package dao;

import model.Brand;
import util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {

    // Lấy tất cả thương hiệu
    public List<Brand> getAllBrands() {
        List<Brand> brands = new ArrayList<>();
        String sql = "SELECT brand_id, brand_name FROM Brand ORDER BY brand_name ASC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Brand brand = new Brand();
                brand.setBrandId(rs.getInt("brand_id"));
                brand.setBrandName(rs.getString("brand_name"));
                brands.add(brand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    // Xóa brand thật, đồng thời xóa tất cả xe liên quan
    public boolean deleteBrand(int brandId) {
        String deleteCarsSql = "DELETE FROM Car WHERE brand_id = ?";
        String deleteBrandSql = "DELETE FROM Brand WHERE brand_id = ?";

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Xóa tất cả xe liên quan
            try (PreparedStatement stmt = conn.prepareStatement(deleteCarsSql)) {
                stmt.setInt(1, brandId);
                stmt.executeUpdate();
            }

            // Xóa brand
            try (PreparedStatement stmt = conn.prepareStatement(deleteBrandSql)) {
                stmt.setInt(1, brandId);
                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    return false; // Brand không tồn tại
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm brand mới
    public boolean addBrand(String brandName) {
        String sql = "INSERT INTO Brand (brand_name) VALUES (?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, brandName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra brand tồn tại
    public boolean brandExists(String brandName) {
        String sql = "SELECT COUNT(*) FROM Brand WHERE brand_name = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, brandName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật brand
    public boolean updateBrand(int brandId, String newName) {
        String sql = "UPDATE Brand SET brand_name = ? WHERE brand_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, brandId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy brand theo ID
    public Brand getBrandById(int brandId) {
        String sql = "SELECT brand_id, brand_name FROM Brand WHERE brand_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Brand brand = new Brand();
                brand.setBrandId(rs.getInt("brand_id"));
                brand.setBrandName(rs.getString("brand_name"));
                return brand;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
