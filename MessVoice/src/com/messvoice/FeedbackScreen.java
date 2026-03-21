package com.messvoice;
import com.messvoice.data.DataHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FeedbackScreen extends JDialog {
	private DataHandler dh = new DataHandler();
    private String studentName;

    public FeedbackScreen(JFrame parent, String studentName) {
        super(parent, "Daily Feedback", true);
        this.studentName = studentName;
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("Daily Feedback", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title);

        JComboBox<String> dishBox = new JComboBox<>(new String[]{"Poha", "Dal Makhani", "Rajma"});
        JSlider slider = new JSlider(1, 5, 3);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(1);
        JTextField comment = new JTextField("Comment (optional)");

        JButton submit = new JButton("Submit");
        submit.addActionListener(e -> {
            String dish = (String) dishBox.getSelectedItem();
            int rating = slider.getValue();
            String cmt = comment.getText().trim();

            dh.saveFeedback(studentName, dish, rating, cmt);  // now dh is known

            JOptionPane.showMessageDialog(this, "Feedback submitted!");
            dispose();
        });

        add(new JLabel("Dish:"));
        add(dishBox);
        add(new JLabel("Rating:"));
        add(slider);
        add(comment);
        add(submit);

        setVisible(true);
    }
}