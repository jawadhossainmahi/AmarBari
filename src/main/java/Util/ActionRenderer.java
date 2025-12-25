/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 *
 * @author Mahi
 */
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ActionRenderer extends JPanel implements TableCellRenderer {

    private JButton editButton;
    private JButton deleteButton;

    public ActionRenderer(Boolean sendMail) {
        if (sendMail) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton("Send Mail");
            add(editButton);
        }

    }

    public ActionRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        add(editButton);
        add(deleteButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
