package gui.view;

import javax.swing.*;

import gui.controller.ClinicController;

import java.awt.*;

public class FindPersonDialog extends JDialog {
    public FindPersonDialog(JFrame parent, ClinicController controller) {
        super(parent, "Поиск персоны", true);
        setSize(300, 150);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Введите ID персоны:");
        JTextField idField = new JTextField(10);
        panel.add(label);
        panel.add(idField);
        add(panel, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton findButton = new JButton("Найти");
        JButton cancelButton = new JButton("Отмена");
        buttonsPanel.add(findButton);
        buttonsPanel.add(cancelButton);
        add(buttonsPanel, BorderLayout.SOUTH);
        
        findButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                controller.findPerson(id);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Неверный ID.");
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
}
