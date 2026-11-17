package week_02;

import java.awt.*;
import javax.swing.*;

/** Bố cục Swing viết bằng Java, chỉnh sửa trực tiếp trong VS Code. */
class LabFrame extends JFrame {
    protected final JPanel body = new JPanel();
    protected final JLabel lbl_status = new JLabel("Sẵn sàng.");

    LabFrame(String title) {
        super(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        row(heading);
        setContentPane(body);
    }

    protected void row(Component... items) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        for (Component item : items) panel.add(item);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        body.add(panel);
    }

    protected void field(String label, JComponent input) {
        row(new JLabel(label), input);
    }

    protected JButton button(String text, Action action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> {
            try { action.run(); }
            catch (Exception ex) { error(ex); }
        });
        return button;
    }

    protected void error(Exception ex) {
        lbl_status.setText("Thao tác thất bại.");
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    protected void finish() {
        lbl_status.setPreferredSize(new Dimension(Math.max(400, body.getPreferredSize().width - 36), 42));
        row(lbl_status);
        pack();
        setLocationRelativeTo(null);
    }

    @FunctionalInterface protected interface Action { void run() throws Exception; }
}
