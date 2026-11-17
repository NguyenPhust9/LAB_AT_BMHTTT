package week_02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.*;

/** Chỉ chạy khi cung cấp mật khẩu SQL qua biến môi trường. Dọn dữ liệu thử sau khi chạy. */
@EnabledIfEnvironmentVariable(named = "LAB2_DB_PASSWORD", matches = ".+")
class SqlIntegrationTest {
    @Test void registerLoginAndHistory() throws Exception {
        String username = "test_" + CipherUtil.hex(CipherUtil.randomBytes(8));
        DbConfig.set("127.0.0.1", Integer.parseInt(System.getenv().getOrDefault("LAB2_DB_PORT", "1433")),
            "LAB2_MaHoaHienDai", "lab2_user", System.getenv("LAB2_DB_PASSWORD").toCharArray());
        char[] password = "Lab2_Test_Password".toCharArray();
        try {
            assertTrue(DbConfig.test().contains("LAB2_MaHoaHienDai"));
            UserDao.register(username, password);
            assertThrows(IllegalArgumentException.class, () -> UserDao.register(username, password));
            assertTrue(UserDao.login(username, password));
            assertFalse(UserDao.login(username, "wrong-password".toCharArray()));
            assertFalse(UserDao.login("missing_" + username, password));
            try (Connection con = DbConfig.open(); PreparedStatement ps = con.prepareStatement(
                    "SELECT DATALENGTH(salt), DATALENGTH(password_hash) FROM dbo.app_users WHERE username=?")) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next()); assertEquals(16, rs.getInt(1)); assertEquals(32, rs.getInt(2));
                }
            }
            String plaintext = "Xin chào Lab 2 - Mã hóa hiện đại!";
            String ciphertext = AESCipher.encrypt(plaintext, "AES_Test_Key");
            assertEquals(plaintext, AESCipher.decrypt(ciphertext, "AES_Test_Key"));
            HistoryDao.insert("AES", "ENCRYPT", username, plaintext.length(), ciphertext.length());
            HistoryDao.insert("AES", "DECRYPT", username, ciphertext.length(), plaintext.length());
            assertTrue(HistoryDao.loadLatest(50).getRowCount() >= 2);
            try (Connection con = DbConfig.open(); PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM dbo.crypto_history WHERE app_username=?")) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) { assertTrue(rs.next()); assertEquals(2, rs.getInt(1)); }
            }
        } finally {
            try (Connection con = DbConfig.open()) {
                for (String query : new String[]{"DELETE FROM dbo.crypto_history WHERE app_username=?",
                        "DELETE FROM dbo.app_users WHERE username=?"}) {
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setString(1, username); ps.executeUpdate();
                    }
                }
            } finally { DbConfig.clear(); java.util.Arrays.fill(password, '\0'); }
        }
    }
}
