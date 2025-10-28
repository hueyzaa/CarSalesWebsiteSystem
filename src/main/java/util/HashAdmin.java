package util;

import org.mindrot.jbcrypt.BCrypt;

public class HashAdmin {
    public static void main(String[] args) {
        String password = "admin@123";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        System.out.println(hashed);
    }
}