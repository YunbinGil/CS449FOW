package main.java.com.YunbinGil.sos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SosGUI extends JFrame {
    public SosGUI() {
        setTitle("SOS Game GUI");  // Window Title
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Top panel: Title Label
        JLabel titleLabel = new JLabel("Welcome to SOS Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // ✅ Center panel: Custom Panel with Lines
        JPanel drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawLine(50, 20, 150, 20);  // Draw a simple line
            }
        };
        drawingPanel.setPreferredSize(new Dimension(300, 100));
        add(drawingPanel, BorderLayout.CENTER);

        // ✅ Bottom panel: Checkbox & Radio Buttons
        JPanel bottomPanel = new JPanel();
        JCheckBox checkBox = new JCheckBox("Enable SOS Mode");
        JRadioButton option1 = new JRadioButton("Option 1");
        JRadioButton option2 = new JRadioButton("Option 2");

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(option1);
        radioGroup.add(option2);

        bottomPanel.add(checkBox);
        bottomPanel.add(option1);
        bottomPanel.add(option2);

        add(bottomPanel, BorderLayout.SOUTH);

        // ✅ Show GUI
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SosGUI::new);
    }
}
