package m3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Dashboard extends JFrame {
    
    // --- Colors & Fonts ---
    private final Color PRIMARY_COLOR = new Color(36, 123, 160); 
    private final Color SUCCESS_COLOR = new Color(40, 167, 69);   
    private final Color WARNING_COLOR = new Color(255, 193, 7);   
    private final Color DANGER_COLOR = new Color(220, 53, 69);    
    private final Color SECONDARY_COLOR = new Color(23, 162, 184); 
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // --- Components ---
    private JTextField medicineNameField, supplierField, quantityField, costField, mfgDateField, expDateField;
    private JTable medicineTable;
    private DefaultTableModel tableModel;

    public Dashboard() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Medical Shop Inventory Dashboard");
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 248, 248)); 
        setContentPane(contentPanel);

        contentPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 20)); 
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        centerPanel.add(createInputFormAndButtonsPanel(), BorderLayout.NORTH); 
        centerPanel.add(createTablePanel(), BorderLayout.CENTER); 

        contentPanel.add(centerPanel, BorderLayout.CENTER);
        
        loadMedicineData();
    }
    
    // =================================================================================
    //                                  LAYOUT & STYLE METHODS 
    // =================================================================================

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("MEDICAL SHOP MANAGEMENT DASHBOARD", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("🚪 Logout");
        styleButton(logoutButton, DANGER_COLOR, DANGER_COLOR.darker(), BOLD_FONT.deriveFont(16f));
        logoutButton.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createInputFormAndButtonsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE); 
        
        JPanel inputFieldsPanel = new JPanel(new GridLayout(3, 4, 40, 15)); 
        inputFieldsPanel.setBackground(Color.WHITE);
        
        medicineNameField = createStyledTextField();
        supplierField = createStyledTextField();
        quantityField = createStyledTextField();
        costField = createStyledTextField();
        mfgDateField = createStyledTextField();
        expDateField = createStyledTextField();
        
        inputFieldsPanel.add(createLabel("Medicine Name:"));
        inputFieldsPanel.add(medicineNameField);
        inputFieldsPanel.add(createLabel("Supplier:"));
        inputFieldsPanel.add(supplierField);

        inputFieldsPanel.add(createLabel("Quantity:"));
        inputFieldsPanel.add(quantityField);
        inputFieldsPanel.add(createLabel("Cost:"));
        inputFieldsPanel.add(costField);
        
        inputFieldsPanel.add(createLabel("Mfg Date (YYYY-MM-DD):"));
        inputFieldsPanel.add(mfgDateField);
        inputFieldsPanel.add(createLabel("Exp Date (YYYY-MM-DD):"));
        inputFieldsPanel.add(expDateField);
        
        mainPanel.add(inputFieldsPanel, BorderLayout.NORTH);

        mainPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)), 
            "Medicine Details & Actions", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 16), 
            PRIMARY_COLOR
        ));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 10));
        buttonPanel.setOpaque(false);

        JButton addButton = new JButton("Add");
        styleButton(addButton, SUCCESS_COLOR, SUCCESS_COLOR.darker());
        addButton.addActionListener(e -> addMedicine());
        
        JButton updateButton = new JButton(" Update");
        styleButton(updateButton, WARNING_COLOR.darker(), WARNING_COLOR.darker().darker()); 
        updateButton.addActionListener(e -> updateMedicine());

        JButton deleteButton = new JButton(" Delete");
        styleButton(deleteButton, DANGER_COLOR, DANGER_COLOR.darker());
        deleteButton.addActionListener(e -> deleteMedicine());
        
        JButton viewAllButton = new JButton(" View All");
        styleButton(viewAllButton, SECONDARY_COLOR, SECONDARY_COLOR.darker()); 
        viewAllButton.addActionListener(e -> loadMedicineData());

        JButton searchButton = new JButton("Search");
        styleButton(searchButton, PRIMARY_COLOR, PRIMARY_COLOR.darker());
        searchButton.addActionListener(e -> searchMedicine());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(searchButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }

    private JPanel createTablePanel() {
        // Table Header names displayed in the GUI
        String[] columnNames = {"ID", "Name", "Supplier", "Quantity", "Cost", "Mfg Date", "Exp Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        medicineTable = new JTable(tableModel);

        medicineTable.setFont(MAIN_FONT);
        medicineTable.setRowHeight(30);
        medicineTable.setGridColor(new Color(240, 240, 240)); 
        medicineTable.setSelectionBackground(PRIMARY_COLOR.brighter().brighter().brighter()); 
        
        medicineTable.getTableHeader().setFont(BOLD_FONT.deriveFont(15f));
        medicineTable.getTableHeader().setBackground(new Color(60, 140, 180)); 
        medicineTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); 

        medicineTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = medicineTable.getSelectedRow();
                if (selectedRow != -1) {
                    medicineNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    supplierField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    quantityField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    costField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    mfgDateField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    expDateField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                }
            }
        });
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    // --- Component Styling Helpers (Omitted for brevity) ---
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(MAIN_FONT.deriveFont(14f));
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setFont(MAIN_FONT.deriveFont(15f));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));
        return field;
    }
    
    private void styleButton(JButton button, Color bg, Color hoverBg, Font font) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBg);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });
    }
    
    private void styleButton(JButton button, Color bg, Color hoverBg) {
        styleButton(button, bg, hoverBg, BOLD_FONT.deriveFont(16f));
    }


    // =================================================================================
    //                                  DATABASE & CRUD METHODS (CRITICAL FIXES APPLIED)
    // =================================================================================

    private void clearFields() {
        medicineNameField.setText("");
        supplierField.setText("");
        quantityField.setText("");
        costField.setText("");
        mfgDateField.setText("");
        expDateField.setText("");
        medicineTable.clearSelection();
    }

    private void loadMedicineData() {
        tableModel.setRowCount(0); 

        // *** FIX: Changed table name from 'medicine' to 'medicines' ***
        String sql = "SELECT * FROM medicines";
        try (Connection con = ConnectionDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    // *** FIX: Changed column name from "id" to "med_id" ***
                    rs.getInt("med_id"), 
                    // *** FIX: Changed column name from "name" to "med_name" ***
                    rs.getString("med_name"),
                    rs.getString("supplier"),
                    rs.getInt("quantity"),
                    rs.getDouble("cost"),
                    rs.getString("mfg_date"),
                    rs.getString("exp_date")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading data: Check ConnectionDB and 'medicines' table schema.\nDetails: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addMedicine() {
        if (medicineNameField.getText().isEmpty() || quantityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields (Name, Quantity).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // *** FIX: Changed table name and column name 'name' to 'med_name' ***
        String sql = "INSERT INTO medicines (med_name, supplier, quantity, cost, mfg_date, exp_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, medicineNameField.getText());
            pst.setString(2, supplierField.getText());
            pst.setInt(3, Integer.parseInt(quantityField.getText()));
            pst.setDouble(4, Double.parseDouble(costField.getText()));
            pst.setString(5, mfgDateField.getText());
            pst.setString(6, expDateField.getText());

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "✅ Medicine Added Successfully!");
                loadMedicineData();
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error adding medicine: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMedicine() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a row to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // ID is column 0, which corresponds to med_id
        String id = tableModel.getValueAt(selectedRow, 0).toString();
        
        // *** FIX: Changed table name, column 'name' to 'med_name', and WHERE clause ID to med_id ***
        String sql = "UPDATE medicines SET med_name=?, supplier=?, quantity=?, cost=?, mfg_date=?, exp_date=? WHERE med_id=?";

        try (Connection con = ConnectionDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, medicineNameField.getText());
            pst.setString(2, supplierField.getText());
            pst.setInt(3, Integer.parseInt(quantityField.getText()));
            pst.setDouble(4, Double.parseDouble(costField.getText()));
            pst.setString(5, mfgDateField.getText());
            pst.setString(6, expDateField.getText());
            pst.setString(7, id);

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "✅ Medicine Updated Successfully!");
                loadMedicineData();
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error updating medicine: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMedicine() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a row to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this item?", "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String id = tableModel.getValueAt(selectedRow, 0).toString();
            
            // *** FIX: Changed table name and WHERE clause ID to med_id ***
            String sql = "DELETE FROM medicines WHERE med_id=?";

            try (Connection con = ConnectionDB.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, id);
                if (pst.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Medicine Deleted Successfully!");
                    loadMedicineData();
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error deleting medicine: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchMedicine() {
        String searchText = JOptionPane.showInputDialog(this, "Enter Medicine Name or Supplier to Search:", "Search medicine", JOptionPane.QUESTION_MESSAGE);
        if (searchText != null && !searchText.trim().isEmpty()) {
            tableModel.setRowCount(0);
            
            // *** FIX: Changed table name and column 'name' to 'med_name' ***
            String sql = "SELECT * FROM medicines WHERE med_name LIKE ? OR supplier LIKE ?";
            
            try (Connection con = ConnectionDB.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {
                
                pst.setString(1, "%" + searchText + "%");
                pst.setString(2, "%" + searchText + "%");
                
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                            // *** FIX: Changed column names for display ***
                            rs.getInt("med_id"),
                            rs.getString("med_name"),
                            rs.getString("supplier"),
                            rs.getInt("quantity"),
                            rs.getDouble("cost"),
                            rs.getString("mfg_date"),
                            rs.getString("exp_date")
                        });
                    }
                     if (tableModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(this, "No results found for '" + searchText + "'.");
                    } 
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error during search: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
