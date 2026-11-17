package week_02;

import java.util.Arrays;
import javax.swing.*;

public class frm_KetNoiSQL extends LabFrame {
    private final JTextField txt_server = new JTextField("127.0.0.1", 30);
    private final JTextField txt_port = new JTextField("1433", 8);
    private final JTextField txt_database = new JTextField("LAB2_MaHoaHienDai", 30);
    private final JTextField txt_user = new JTextField("lab2_user", 30);
    private final JPasswordField txt_password = new JPasswordField(30);

    public frm_KetNoiSQL() {
        super("KẾT NỐI MICROSOFT SQL SERVER");
        field("Server:", txt_server); field("Port:", txt_port);
        field("Database:", txt_database); field("User:", txt_user); field("Password:", txt_password);
        row(button("Test Connection", this::test), button("Xem lịch sử", () -> new frm_LichSu().setVisible(true)));
        lbl_status.setText("Nhập mật khẩu SQL rồi bấm Test Connection.");
        finish();
    }

    private void test() throws Exception {
        char[] password = txt_password.getPassword();
        try {
            DbConfig.set(txt_server.getText().trim(), Integer.parseInt(txt_port.getText().trim()),
                txt_database.getText().trim(), txt_user.getText().trim(), password);
            lbl_status.setText("<html>" + DbConfig.test() + "</html>");
        } catch (Exception ex) {
            DbConfig.clear();
            throw ex;
        } finally { Arrays.fill(password, '\0'); }
    }
}
