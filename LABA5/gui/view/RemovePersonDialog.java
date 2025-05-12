package gui.view;

import javax.swing.*;
import java.awt.*;

import gui.controller.ClinicController;

public class RemovePersonDialog extends JDialog {
    public RemovePersonDialog(JFrame parent, ClinicController controller) {
        super(parent, "Удаление персоны", true);
        setSize(300, 150);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Введите ID удаляемой персоны:");
        JTextField idField = new JTextField(10);
        panel.add(label);
        panel.add(idField);
        add(panel, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Удалить");
        JButton cancelButton = new JButton("Отмена");
        buttonsPanel.add(removeButton);
        buttonsPanel.add(cancelButton);
        add(buttonsPanel, BorderLayout.SOUTH);
        
        removeButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                controller.removePerson(id);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Неверный ID.");
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
}
