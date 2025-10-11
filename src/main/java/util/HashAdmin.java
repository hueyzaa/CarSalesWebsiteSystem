package util;

import org.mindrot.jbcrypt.BCrypt;

public class HashAdmin {
    public static void main(String[] args) {
        String password = "admin123"; // mật khẩu bạn muốn dùng
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        System.out.println(hashed);
    }
}


