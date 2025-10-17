package util;
import java.sql.Connection;
import java.sql.DriverManager;
public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://DESKTOP-8M1I98V\\HUYNG:1433;databaseName=CarSalesWebsite;encrypt=true;trustServerCertificate=true";
        String username = "sa";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Kết nối database thành công!");
        } catch (Exception e) {
            System.out.println("Kết nối thất bại:");
            e.printStackTrace();
        }
    }
}
