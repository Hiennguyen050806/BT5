package vn.iotstar.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Mã hóa mật khẩu bằng thuật toán SHA-256 (tương thích dữ liệu từ D:\web\BaiTap2_JPA)
     */
    public static String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi mã hóa SHA-256: " + e.getMessage(), e);
        }
    }

    /**
     * So khớp mật khẩu người dùng nhập với mật khẩu đã lưu trong CSDL
     */
    public static boolean checkPassword(String plainPassword, String storedPassword) {
        if (plainPassword == null || storedPassword == null) {
            return false;
        }
        String hashedPlain = hashPassword(plainPassword);
        return hashedPlain.equalsIgnoreCase(storedPassword);
    }
}
