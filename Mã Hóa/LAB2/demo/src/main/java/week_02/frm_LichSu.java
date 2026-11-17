package week_02;

import java.awt.Dimension;
import javax.swing.*;

public class frm_LichSu extends LabFrame {
    private final JTable tbl_history = new JTable(HistoryDao.emptyModel());
    public frm_LichSu() {
        super("LỊCH SỬ THAO TÁC - DBO.CRYPTO_HISTORY");
        JScrollPane pane = new JScrollPane(tbl_history);
        pane.setPreferredSize(new Dimension(860, 280));
        row(pane);
        row(button("Tải lại", this::loadData));
        finish();
        loadData();
    }
    private void loadData() {
        try {
            tbl_history.setModel(HistoryDao.loadLatest(50));
            lbl_status.setText("Đã tải " + tbl_history.getRowCount() + " dòng mới nhất (thời gian UTC).");
        } catch (java.sql.SQLException ex) { lbl_status.setText("Chưa tải được: " + ex.getMessage()); }
    }
}
