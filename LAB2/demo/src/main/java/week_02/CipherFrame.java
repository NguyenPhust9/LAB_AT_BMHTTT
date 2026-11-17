package week_02;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

/** Các trường và sự kiện chung cho sáu bài mã hóa. */
class CipherFrame extends LabFrame {
    protected final JTextArea txt_plaintext = area();
    protected final JTextArea txt_ciphertext = area();
    protected final JTextField txt_key = new JTextField(42);
    protected final JTextField txt_iv = new JTextField(42);
    protected final JTextField txt_username = new JTextField("sinhvien01", 20);
    protected final JPasswordField txt_password = new JPasswordField(20);
    protected final JTextField txt_ticket = new JTextField(32);
    private final String algorithm;
    private String currentUser;

    CipherFrame(String algorithm, String title, String defaultKey, String hint) {
        super(title);
        this.algorithm = algorithm;
        if (algorithm.equals("AES")) {
            field("Username:", txt_username);
            field("Password (ít nhất 8 ký tự):", txt_password);
            row(button("Kết nối SQL Server", () -> new frm_KetNoiSQL().setVisible(true)),
                button("Register", this::register), button("Login", this::login),
                button("Logout", this::logout));
            txt_ticket.setEditable(false);
            field("Ticket:", txt_ticket);
        }
        field("Plaintext:", new JScrollPane(txt_plaintext));
        txt_key.setText(defaultKey);
        field("Key:", txt_key);
        row(new JLabel(hint));
        if (algorithm.equals("Twofish")) {
            field("IV (HEX):", txt_iv);
            row(button("Generate IV", () -> txt_iv.setText(TwofishCipher.newIv())));
        } else if (algorithm.equals("AES-CTR")) {
            field("Nonce (Base64):", txt_iv);
            row(button("Generate Key", () -> txt_key.setText(AESCTRCipher.newKey())),
                button("Generate Nonce", () -> txt_iv.setText(AESCTRCipher.newNonce())));
        }
        field("Ciphertext" + (algorithm.equals("RC4") ? " (HEX):" : " (Base64):"), new JScrollPane(txt_ciphertext));
        row(button("Encrypt", () -> crypt(true)), button("Decrypt", () -> crypt(false)),
            button("Save File", this::save), button("Open File", this::open));
        finish();
    }

    private static JTextArea area() {
        JTextArea area = new JTextArea(4, 58);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return area;
    }

    private void register() throws Exception {
        char[] password = txt_password.getPassword();
        try {
            UserDao.register(txt_username.getText().trim(), password);
            lbl_status.setText("Register thành công. Bấm Login để nhận Ticket.");
        } finally { Arrays.fill(password, '\0'); }
    }

    private void logout() {
        currentUser = null;
        txt_ticket.setText("");
        lbl_status.setText("Đã đăng xuất.");
    }

    private void login() throws Exception {
        logout();
        char[] password = txt_password.getPassword();
        String username = txt_username.getText().trim();
        try {
            if (!UserDao.login(username, password))
                throw new IllegalArgumentException("Username hoặc mật khẩu không đúng.");
            currentUser = username;
            txt_ticket.setText(UserDao.newTicket());
            lbl_status.setText("Login thành công.");
        } finally { Arrays.fill(password, '\0'); }
    }

    private void crypt(boolean encrypt) throws Exception {
        if (algorithm.equals("AES") && currentUser == null)
            throw new IllegalArgumentException("Hãy Login trước khi dùng AES.");
        String input = (encrypt ? txt_plaintext : txt_ciphertext).getText();
        if (input.isEmpty()) throw new IllegalArgumentException("Hãy nhập " + (encrypt ? "Plaintext" : "Ciphertext") + " trước.");
        String key = txt_key.getText(), iv = txt_iv.getText();
        String output = switch (algorithm) {
            case "DES" -> encrypt ? DESCipher.encrypt(input, key) : DESCipher.decrypt(input, key);
            case "TripleDES" -> encrypt ? TripleDESCipher.encrypt(input, key) : TripleDESCipher.decrypt(input, key);
            case "AES" -> encrypt ? AESCipher.encrypt(input, key) : AESCipher.decrypt(input, key);
            case "RC4" -> encrypt ? RC4Cipher.encrypt(input, key) : RC4Cipher.decrypt(input, key);
            case "Twofish" -> encrypt ? TwofishCipher.encrypt(input, key, iv) : TwofishCipher.decrypt(input, key, iv);
            case "AES-CTR" -> encrypt ? AESCTRCipher.encrypt(input, key, iv) : AESCTRCipher.decrypt(input, key, iv);
            default -> throw new IllegalStateException("Thuật toán không hợp lệ.");
        };
        (encrypt ? txt_ciphertext : txt_plaintext).setText(output);
        lbl_status.setText("<html>Đã " + (encrypt ? "mã hóa " : "giải mã ") + algorithm + ". "
            + HistoryDao.tryInsert(algorithm, encrypt ? "ENCRYPT" : "DECRYPT", currentUser, input.length(), output.length()) + "</html>");
    }

    private void save() {
        if (txt_ciphertext.getText().isBlank()) throw new IllegalArgumentException("Chưa có bản mã để lưu.");
        String content = switch (algorithm) {
            case "Twofish" -> TwofishCipher.toFile(txt_iv.getText(), txt_ciphertext.getText());
            case "AES-CTR" -> AESCTRCipher.toFile(txt_iv.getText(), txt_ciphertext.getText());
            default -> txt_ciphertext.getText();
        };
        String path = FileTextIO.saveString(this, content, algorithm.toLowerCase() + "_ciphertext.txt");
        if (path != null) lbl_status.setText("Đã lưu: " + path);
    }

    private void open() {
        String content = FileTextIO.openString(this);
        if (content == null) return;
        if (algorithm.equals("Twofish") || algorithm.equals("AES-CTR")) {
            String[] parts = algorithm.equals("Twofish") ? TwofishCipher.fromFile(content) : AESCTRCipher.fromFile(content);
            txt_iv.setText(parts[0]);
            txt_ciphertext.setText(parts[1]);
        } else txt_ciphertext.setText(content.trim());
        lbl_status.setText("Đã mở tệp. Nhập đúng Key rồi bấm Decrypt.");
    }
}
