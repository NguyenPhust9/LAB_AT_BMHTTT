package week_02;

import java.awt.*;
import javax.swing.*;

public class frm_Menu extends LabFrame {
    public frm_Menu() {
        super("LAB 2 - MÃ HÓA ĐỐI XỨNG HIỆN ĐẠI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        row(new JLabel("Chọn một bài để mở form tương ứng:"));
        row(button("2.1 DES", () -> new frm_DES().setVisible(true)),
            button("2.2 TripleDES", () -> new frm_TripleDES().setVisible(true)),
            button("2.3 AES + Login", () -> new frm_AES().setVisible(true)));
        row(button("2.4 RC4", () -> new frm_RC4().setVisible(true)),
            button("2.5 Twofish", () -> new frm_Twofish().setVisible(true)),
            button("2.6 Nâng cao - AES-CTR", () -> new frm_AES_CTR().setVisible(true)));
        row(button("Kết nối SQL Server", () -> new frm_KetNoiSQL().setVisible(true)),
            button("Xem lịch sử", () -> new frm_LichSu().setVisible(true)));
        pack();
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) { }
            new frm_Menu().setVisible(true);
        });
    }
}
