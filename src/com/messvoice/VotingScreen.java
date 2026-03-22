package com.messvoice;
import com.messvoice.model.MenuItem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import com.messvoice.data.DataHandler;
import com.messvoice.model.MenuItem;

public class VotingScreen extends JDialog {
    private DataHandler dh = new DataHandler();
    private String studentName;
    private JCheckBox[] checkboxes;
    private List<MenuItem> menuItems;

    public VotingScreen(JFrame parent, String studentName) {
        super(parent, "Weekly Voting", true);
        this.studentName = studentName;
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Vote for Next Week's Menu", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        menuItems = dh.getAllMenuItems();
        JPanel panel = new JPanel(new GridLayout(menuItems.size(), 1, 5, 5));
        checkboxes = new JCheckBox[menuItems.size()];

     // Checkboxes greyed out
        List<String> prefs = dh.getStudentPreferences(studentName);
        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            JCheckBox cb = new JCheckBox(item.getDishName() + " (" + item.getDayMeal() + ")");
            boolean disabled = false;

            if (prefs.contains("vegetarian_only") && item.getType().equals("non-veg")) disabled = true;
            if (prefs.contains("no_onion") && item.getDishName().toLowerCase().contains("onion")) disabled = true;
            if (prefs.contains("no_garlic") && item.getDishName().toLowerCase().contains("garlic")) disabled = true;
            if (prefs.contains("no_dairy") && item.getDishName().toLowerCase().contains("paneer")) disabled = true;

            if (disabled) {
                cb.setEnabled(false);
                cb.setForeground(Color.GRAY);
            }
            panel.add(cb);
            checkboxes[i] = cb;
        }

        JButton submitBtn = new JButton("Submit Vote");
        submitBtn.addActionListener(e -> submitVote());

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(submitBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void submitVote() {
        List<String> selected = new ArrayList<>();
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                selected.add(cb.getText().split(" \\(")[0]); // extract dish name
            }
        }

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one dish");
            return;
        }

        dh.saveVote(studentName, selected);
        JOptionPane.showMessageDialog(this, "Vote submitted successfully!");
        dispose();
    }
}