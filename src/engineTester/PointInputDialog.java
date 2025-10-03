package engineTester;

import javax.swing.*;
import java.awt.*;

public class PointInputDialog extends JDialog {
    private JTextField xField;
    private JTextField yField;
    private float x, y;
    private boolean confirmed = false;

    public PointInputDialog(Frame owner) {
        super(owner, "Enter Point Coordinates", true);
        setLayout(new GridLayout(3, 2, 5, 5));

        // Добавляем метки и поля для ввода
        add(new JLabel("X Coordinate:"));
        xField = new JTextField();
        add(xField);

        add(new JLabel("Y Coordinate:"));
        yField = new JTextField();
        add(yField);

        // Кнопки OK и Cancel
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> onOK());
        add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> onCancel());
        add(cancelButton);
        
        // Настраиваем окно
        pack(); // Автоматически подбирает размер
        setLocationRelativeTo(owner); // По центру родительского окна
        setAlwaysOnTop(true);
    }

    private void onOK() {
        try {
            // Считываем и парсим значения
            x = Float.parseFloat(xField.getText());
            y = Float.parseFloat(yField.getText());
            confirmed = true;
            dispose(); // Закрываем окно
        } catch (NumberFormatException ex) {
            // Если ввели не число
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for coordinates.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        confirmed = false;
        dispose(); // Просто закрываем окно
    }

    // Методы для получения введенных данных
    public boolean isConfirmed() {
        return confirmed;
    }

    public float getXCoordinate() {
        return x;
    }

    public float getYCoordinate() {
        return y;
    }
}