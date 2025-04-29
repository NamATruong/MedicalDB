import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class MedicineUI extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel listPanel;
    private JPanel detailsPanel;
    
    private JTable medicineTable;
    private JScrollPane medicineScrollPane;
    private JTextField searchField;
    private JButton viewButton;
    
    private JTextField medIdField;
    private JTextField medNameField;
    private JTextField drugClassField;
    private JTextField indicationField;
    private JTextPane indicationDescPane;
    private JTextPane dosagePane;
    private JTextPane adminDescPane;
    private JTextPane contraindicationPane;
    private JTextPane sideEffectPane;
    private JTextPane pregnancyLactationPane;
    private JTextPane precautionsPane;
    private JTextPane durationPane;
    private JTextPane storagePane;
    private JTextField linkField;
    private JButton addPrescriptionButton;
    
    private MedicineManager medicineManager;
    
    public MedicineUI() {
        medicineManager = new MedicineManager();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Medical DB - Medicine Data");
        setSize(1520, 670);
        getContentPane().setBackground(new Color(51, 51, 51));
        setLayout(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        
        createListPanel();
        
        createDetailsPanel();
        
        tabbedPane.addTab("Medicine List", listPanel);
        tabbedPane.addTab("Medicine Information", detailsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void createListPanel() {
        listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout(10, 10));
        

        String[] columns = {"ID", "Name", "Drug Class", "Indication", "Indication Description", 
                           "Dosage", "Admin Description", "Contraindication", "Side Effect", 
                           "Pregnancy/Lactation", "Precautions", "Treatment Duration", 
                           "Storage Condition", "Link"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        medicineTable = new JTable(model);
        medicineScrollPane = new JScrollPane(medicineTable);
        

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Medicine Name Search:");
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                medicineManager.searchMedicineByName(medicineTable, searchField.getText());
            }
        });
        
        viewButton = new JButton("See Full Medicine Information");
        viewButton.addActionListener(e -> {
            viewMedicineDetails();
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(viewButton);
        
        listPanel.add(searchPanel, BorderLayout.NORTH);
        listPanel.add(medicineScrollPane, BorderLayout.CENTER);
    }
    
    private void createDetailsPanel() {
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        
        JLabel idLabel = new JLabel("Medicine ID:");
        medIdField = new JTextField(10);
        medIdField.setEditable(false);
        topPanel.add(idLabel);
        topPanel.add(medIdField);
    
        JLabel nameLabel = new JLabel("Medicine Name:");
        medNameField = new JTextField(20);
        medNameField.setEditable(false);
        topPanel.add(nameLabel);
        topPanel.add(medNameField);
        
        JLabel classLabel = new JLabel("Drug Class:");
        drugClassField = new JTextField(20);
        drugClassField.setEditable(false);
        topPanel.add(classLabel);
        topPanel.add(drugClassField);
        
        JLabel indicationLabel = new JLabel("Indication:");
        indicationField = new JTextField(20);
        indicationField.setEditable(false);
        topPanel.add(indicationLabel);
        topPanel.add(indicationField);
        
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel linkLabel = new JLabel("For more information visit:");
        linkField = new JTextField(40);
        linkField.setEditable(false);
        
        addPrescriptionButton = new JButton("Add this medicine to Patient Prescription");
        addPrescriptionButton.addActionListener(e -> {
            addPrescription();
        });
        
        linkPanel.add(linkLabel);
        linkPanel.add(linkField);
        linkPanel.add(addPrescriptionButton);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(topPanel, gbc);
        
        gbc.gridy = 1;
        formPanel.add(linkPanel, gbc);
        
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        JPanel indicationDescPanel = new JPanel(new BorderLayout());
        indicationDescPanel.setBorder(BorderFactory.createTitledBorder("Indication Description"));
        indicationDescPane = new JTextPane();
        indicationDescPane.setEditable(false);
        JScrollPane indicationDescScrollPane = new JScrollPane(indicationDescPane);
        indicationDescPanel.add(indicationDescScrollPane, BorderLayout.CENTER);
        formPanel.add(indicationDescPanel, gbc);
        
        gbc.gridx = 1;
        JPanel contraindicationPanel = new JPanel(new BorderLayout());
        contraindicationPanel.setBorder(BorderFactory.createTitledBorder("Contraindication"));
        contraindicationPane = new JTextPane();
        contraindicationPane.setEditable(false);
        JScrollPane contraindicationScrollPane = new JScrollPane(contraindicationPane);
        contraindicationPanel.add(contraindicationScrollPane, BorderLayout.CENTER);
        formPanel.add(contraindicationPanel, gbc);
        
        gbc.gridx = 2;
        JPanel precautionsPanel = new JPanel(new BorderLayout());
        precautionsPanel.setBorder(BorderFactory.createTitledBorder("Precautions"));
        precautionsPane = new JTextPane();
        precautionsPane.setEditable(false);
        JScrollPane precautionsScrollPane = new JScrollPane(precautionsPane);
        precautionsPanel.add(precautionsScrollPane, BorderLayout.CENTER);
        formPanel.add(precautionsPanel, gbc);
        
        gbc.gridx = 3;
        JPanel storagePanel = new JPanel(new BorderLayout());
        storagePanel.setBorder(BorderFactory.createTitledBorder("Storage Condition"));
        storagePane = new JTextPane();
        storagePane.setEditable(false);
        JScrollPane storageScrollPane = new JScrollPane(storagePane);
        storagePanel.add(storageScrollPane, BorderLayout.CENTER);
        formPanel.add(storagePanel, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        
        JPanel dosagePanel = new JPanel(new BorderLayout());
        dosagePanel.setBorder(BorderFactory.createTitledBorder("Dosage"));
        dosagePane = new JTextPane();
        dosagePane.setEditable(false);
        JScrollPane dosageScrollPane = new JScrollPane(dosagePane);
        dosagePanel.add(dosageScrollPane, BorderLayout.CENTER);
        formPanel.add(dosagePanel, gbc);
        
        gbc.gridx = 1;
        JPanel adminDescPanel = new JPanel(new BorderLayout());
        adminDescPanel.setBorder(BorderFactory.createTitledBorder("Administration Description"));
        adminDescPane = new JTextPane();
        adminDescPane.setEditable(false);
        JScrollPane adminDescScrollPane = new JScrollPane(adminDescPane);
        adminDescPanel.add(adminDescScrollPane, BorderLayout.CENTER);
        formPanel.add(adminDescPanel, gbc);
        
        gbc.gridx = 2;
        JPanel durationPanel = new JPanel(new BorderLayout());
        durationPanel.setBorder(BorderFactory.createTitledBorder("Treatment Duration"));
        durationPane = new JTextPane();
        durationPane.setEditable(false);
        JScrollPane durationScrollPane = new JScrollPane(durationPane);
        durationPanel.add(durationScrollPane, BorderLayout.CENTER);
        formPanel.add(durationPanel, gbc);
        
        gbc.gridx = 3;
        JPanel sideEffectPanel = new JPanel(new BorderLayout());
        sideEffectPanel.setBorder(BorderFactory.createTitledBorder("Side Effects"));
        sideEffectPane = new JTextPane();
        sideEffectPane.setEditable(false);
        JScrollPane sideEffectScrollPane = new JScrollPane(sideEffectPane);
        sideEffectPanel.add(sideEffectScrollPane, BorderLayout.CENTER);
        formPanel.add(sideEffectPanel, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JPanel pregnancyPanel = new JPanel(new BorderLayout());
        pregnancyPanel.setBorder(BorderFactory.createTitledBorder("Pregnancy & Lactation"));
        pregnancyLactationPane = new JTextPane();
        pregnancyLactationPane.setEditable(false);
        JScrollPane pregnancyScrollPane = new JScrollPane(pregnancyLactationPane);
        pregnancyPanel.add(pregnancyScrollPane, BorderLayout.CENTER);
        formPanel.add(pregnancyPanel, gbc);
        
        detailsPanel.add(formPanel, BorderLayout.CENTER);
    }
    
    private void loadData() {
        medicineManager.fillMedicineTable(medicineTable);
    }
    
    private void viewMedicineDetails() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a medicine first", 
                "No Medicine Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        tabbedPane.setSelectedIndex(1);
        
        DefaultTableModel model = (DefaultTableModel) medicineTable.getModel();
        
        medIdField.setText(model.getValueAt(selectedRow, 0).toString());
        medNameField.setText(model.getValueAt(selectedRow, 1).toString());
        drugClassField.setText(model.getValueAt(selectedRow, 2).toString());
        indicationField.setText(model.getValueAt(selectedRow, 3).toString());
        indicationDescPane.setText(model.getValueAt(selectedRow, 4).toString());
        dosagePane.setText(model.getValueAt(selectedRow, 5).toString());
        adminDescPane.setText(model.getValueAt(selectedRow, 6).toString());
        contraindicationPane.setText(model.getValueAt(selectedRow, 7).toString());
        sideEffectPane.setText(model.getValueAt(selectedRow, 8).toString());
        pregnancyLactationPane.setText(model.getValueAt(selectedRow, 9).toString());
        precautionsPane.setText(model.getValueAt(selectedRow, 10).toString());
        durationPane.setText(model.getValueAt(selectedRow, 11).toString());
        storagePane.setText(model.getValueAt(selectedRow, 12).toString());
        linkField.setText(model.getValueAt(selectedRow, 13).toString());
    }
    
    private void addPrescription() {
        if (medIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a medicine first", 
                "No Medicine Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String patientId = JOptionPane.showInputDialog(this, "Enter patient ID:");
        if (patientId == null || patientId.trim().isEmpty()) {
            return;
        }
        
        int medicineId = Integer.parseInt(medIdField.getText());
        
        if (medicineManager.addPrescriptionForPatient(patientId, medicineId)) {
            JOptionPane.showMessageDialog(this, 
                "Prescription was added successfully", 
                "Add Prescription", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add prescription. The patient may already have a prescription for this medicine.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MedicineUI().setVisible(true);
        });
    }
}
