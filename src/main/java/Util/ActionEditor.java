/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 *
 * @author Mahi
 */
import Models.Buildings;
import Models.RoomDetails;
import Models.Rooms;
import static Util.SendEmail.sendMail;
import View.Dashboard.Building.EditBuilding;
import View.Dashboard.BuildingRooms.EditRoom;
import View.Dashboard.Dashboard;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.AbstractCellEditor;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

public class ActionEditor extends AbstractCellEditor implements TableCellEditor {

    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private Object currentObject;
    private Dashboard parent; // keep a reference
    private ReloadTable reloadFn; // callback

    public ActionEditor(JTable table, Dashboard parent, ReloadTable reloadFn, boolean sendMail) {
        this.parent = parent;
        this.reloadFn = reloadFn;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        if (sendMail) {
            editButton = new JButton("Send Mail");
            panel.add(editButton);

            editButton.addActionListener(e -> {
                fireEditingStopped();
                // 👉 here you can call your SendEmail class
                if (currentObject instanceof RoomDetails) {
                    RoomDetails rd = (RoomDetails) currentObject;
                    SendEmail mailer = new SendEmail();

                    Response res = mailer.sendMail(
                            rd.getUser().getEmail(),
                            "Room Booking Confirmation",
                            "Dear " + rd.getUser().getFirstName() + ",\n\n"
                            + "We are pleased to inform you that your booking has been successfully confirmed.\n\n"
                            + "Room Details:\n"
                            + "Room Name: " + rd.getRoom().getRoomName() + "\n\n"
                            + "Please ensure that the rental payment is completed according to the building policy.\n\n"
                            + "If you have any questions, feel free to contact us.\n\n"
                            + "Thank you for choosing our service.\n\n"
                            + "Best regards,\n"
                            + "Hotel Management Team"
                    );

                    if (res.isSuccess()) {
                        JOptionPane.showMessageDialog(parent,
                                "Mail sent successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                }
            });

        }
    }

    public ActionEditor(JTable table, Dashboard parent, ReloadTable reloadFn) {
        this.parent = parent;
        this.reloadFn = reloadFn;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        panel.add(editButton);
        panel.add(deleteButton);

        editButton.addActionListener(e -> {
            fireEditingStopped();
            if (currentObject instanceof Buildings) {
                Buildings building = (Buildings) currentObject;
                EditBuilding eb = new EditBuilding(parent, building);
                parent.setJContainerContent(eb, "Edit Building");
            } else if (currentObject instanceof Rooms) {
                Rooms r = (Rooms) currentObject;
                EditRoom er = new EditRoom(parent, r);
                parent.setJContainerContent(er, "Edit Rooms");
            }
        });

        deleteButton.addActionListener(e -> {
            fireEditingStopped();
            if (currentObject instanceof Buildings) {
                Buildings b = (Buildings) currentObject;

                boolean exist = Buildings.RoomExist(b.getId());

                System.out.println(exist);
                if (!exist) {
                    Response response = b.deleteRelatedData();
                    JOptionPane.showMessageDialog(parent,
                            "Building deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    // 🔄 Trigger reload of the table
                    if (reloadFn != null) {
                        reloadFn.reload();
                    }

                } else {
                    JOptionPane.showMessageDialog(parent,
                            "Room already booked",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } else if (currentObject instanceof Rooms) {
                Rooms r = (Rooms) currentObject;
                System.out.println(r.getBooked());
                if (r.getBooked() == 0) {
                    Response res = r.delete();
                    if (res.isSuccess()) {
                        JOptionPane.showMessageDialog(parent,
                                "Room deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(parent,
                                res.getMsg(),
                                "Error",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                    // 🔄 Trigger reload of the table
                    if (reloadFn != null) {
                        reloadFn.reload();
                    }

                }

            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        currentObject = value;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return currentObject;
    }
}
