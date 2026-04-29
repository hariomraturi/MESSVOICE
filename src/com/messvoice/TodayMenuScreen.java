package com.messvoice;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.messvoice.data.DataHandler;
import com.messvoice.model.MenuItem;

public class TodayMenuScreen extends JDialog {
    private DataHandler dh;
    private List<MenuItem> allMenu;
    private List<JCheckBox> checkboxes;
    private String selectedMealType;
    
    public TodayMenuScreen(JFrame parent) {
        super(parent, "Set Today's Menu", true);
        dh = new DataHandler();
        
        // First ask which meal to set
        String[] mealTypes = {"Breakfast", "Lunch", "Dinner"};
        selectedMealType = (String) JOptionPane.showInputDialog(this, 
            "Which meal are you setting?", "Select Meal Type",
            JOptionPane.QUESTION_MESSAGE, null, mealTypes, mealTypes[0]);
        
        if (selectedMealType == null) {
            dispose();
            return;
        }
        
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Title
        JLabel title = new JLabel("Set " + selectedMealType + " Menu for Today", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(41, 128, 185));
        add(title, BorderLayout.NORTH);
        
        // Get menu items for selected meal type only
        allMenu = dh.getDishesByMealType(selectedMealType);
        checkboxes = new ArrayList<>();
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        if (allMenu.isEmpty()) {
            panel.add(new JLabel("No dishes available for " + selectedMealType));
        } else {
            for (MenuItem item : allMenu) {
                JCheckBox cb = new JCheckBox(item.getDishName() + " (" + item.getCategory() + ")");
                cb.setFont(new Font("Arial", Font.PLAIN, 12));
                panel.add(cb);
                checkboxes.add(cb);
                panel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel bottomPanel = new JPanel();
        
        JButton saveBtn = new JButton("Save Today's Menu");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveMenu());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
 // Modified saveMenu method
    private void saveMenu() {
        List<Integer> selectedDishIds = new ArrayList<>();
        
        for (int i = 0; i < checkboxes.size(); i++) {
            if (checkboxes.get(i).isSelected()) {
                selectedDishIds.add(allMenu.get(i).getId());
            }
        }
        
        if (selectedDishIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one dish");
            return;
        }
        
        dh.saveTodayMenu(selectedDishIds, selectedMealType);
        JOptionPane.showMessageDialog(this, selectedMealType + " menu saved for today!");
        dispose();
    }
}