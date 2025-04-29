import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class PatientUI extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel patientPanel;
    private JPanel transcriptionPanel;
    private JPanel prescriptionPanel;
    
    private JTable patientTable;
    private JScrollPane patientScrollPane;
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton clearButton;
    private JButton refreshButton;
    private JButton createTransButton;
    
    private JTable transcriptionTable;
    private JScrollPane transcriptionScrollPane;
    private JTextField transIdField;
    private JTextField transPatientIdField;
    private JTextField medSpecField;
    private JTextField sampleNameField;
    private JTextPane descriptionPane;
    private JTextPane transcriptionPane;
    private JTextPane keywordsPane;
    private JTextField transSearchField;
    private JButton addTransButton;
    private JButton editTransButton;
    private JButton removeTransButton;
    private JButton clearTransButton;
    private JButton refreshTransButton;
    
    private JTable prescriptionTable;
    private JScrollPane prescriptionScrollPane;
    private JTextField presIdField;
    private JTextPane presPatientIdPane;
    private JTextPane presFirstNamePane;
    private JTextPane presMedNamePane;
    private JTextPane presMedIdPane;
    private JTextPane presDosagePane;
    private JTextPane presStoragePane;
    private JTextField presSearchField;
    private JButton removePresButton;
    private JButton refreshPresButton;
    
    private PatientManager patientManager;
    
    public PatientUI() {
        patientManager = new PatientManager();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Medical DB - Patient Data");
        setSize(1450, 670);
        getContentPane().setBackground(new Color(51, 51, 51));
        setLayout(new BorderLayout());
        

        tabbedPane = new JTabbedPane();
        

        createPatientPanel();
        

        createTranscriptionPanel();
        

        createPrescriptionPanel();
        

        tabbedPane.addTab("Patient Information", patientPanel);
        tabbedPane.addTab("Transcriptions", transcriptionPanel);
        tabbedPane.addTab("Prescriptions", prescriptionPanel);
        

        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void createPatientPanel() {
        patientPanel = new JPanel();
        patientPanel.setLayout(new BorderLayout(10, 10));
        

        String[] columns = {"ID", "First Name", "Last Name", "Phone", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        patientTable = new JTable(model);
        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && patientTable.getSelectedRow() != -1) {
                int row = patientTable.getSelectedRow();
                idField.setText(patientTable.getValueAt(row, 0).toString());
                firstNameField.setText(patientTable.getValueAt(row, 1).toString());
                lastNameField.setText(patientTable.getValueAt(row, 2).toString());
                phoneField.setText(patientTable.getValueAt(row, 3).toString());
                emailField.setText(patientTable.getValueAt(row, 4).toString());
                createTransButton.setEnabled(true);
            }
        });
        
        patientScrollPane = new JScrollPane(patientTable);
        

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("First Name Search:");
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                patientManager.searchPatientsByName(patientTable, searchField.getText());
            }
        });
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            patientManager.fillPatientTable(patientTable);
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(refreshButton);
        

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Patient Information"));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        idField = new JTextField(20);
        idField.setEditable(false);
        formPanel.add(idField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("First Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        firstNameField = new JTextField(20);
        formPanel.add(firstNameField, gbc);
        

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Last Name:"), gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        lastNameField = new JTextField(20);
        formPanel.add(lastNameField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);
        

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            addPatient();
        });
        
        editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            editPatient();
        });
        
        removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            removePatient();
        });
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            clearPatientFields();
        });
        
        createTransButton = new JButton("Create/Edit Transcription");
        createTransButton.setEnabled(false);
        createTransButton.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
            transPatientIdField.setText(idField.getText());
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(createTransButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(buttonPanel, gbc);
        

        patientPanel.add(searchPanel, BorderLayout.NORTH);
        patientPanel.add(patientScrollPane, BorderLayout.CENTER);
        patientPanel.add(formPanel, BorderLayout.SOUTH);
    }
    
    private void createTranscriptionPanel() {
        transcriptionPanel = new JPanel();
        transcriptionPanel.setLayout(new BorderLayout(10, 10));
        

        String[] columns = {"Transcription ID", "Patient ID", "Description", "Medical Specialty", 
                           "Sample Name", "Transcription", "Keywords"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transcriptionTable = new JTable(model);
        transcriptionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && transcriptionTable.getSelectedRow() != -1) {
                int row = transcriptionTable.getSelectedRow();
                transIdField.setText(transcriptionTable.getValueAt(row, 0).toString());
                transPatientIdField.setText(transcriptionTable.getValueAt(row, 1).toString());
                descriptionPane.setText(transcriptionTable.getValueAt(row, 2).toString());
                medSpecField.setText(transcriptionTable.getValueAt(row, 3).toString());
                sampleNameField.setText(transcriptionTable.getValueAt(row, 4).toString());
                transcriptionPane.setText(transcriptionTable.getValueAt(row, 5).toString());
                keywordsPane.setText(transcriptionTable.getValueAt(row, 6).toString());
            }
        });
        
        transcriptionScrollPane = new JScrollPane(transcriptionTable);
        

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Patient ID Search:");
        transSearchField = new JTextField(20);
        transSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                patientManager.searchTranscriptionsByPatientId(transcriptionTable, transSearchField.getText());
            }
        });
        
        refreshTransButton = new JButton("Refresh");
        refreshTransButton.addActionListener(e -> {
            patientManager.fillTranscriptionTable(transcriptionTable);
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(transSearchField);
        searchPanel.add(refreshTransButton);
        

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Transcription Information"));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Transcription ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        transIdField = new JTextField(10);
        transIdField.setEditable(false);
        formPanel.add(transIdField, gbc);
        

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Patient ID:"), gbc);
        
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        transPatientIdField = new JTextField(20);
        transPatientIdField.setEditable(false);
        formPanel.add(transPatientIdField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Medical Specialty:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        medSpecField = new JTextField(20);
        formPanel.add(medSpecField, gbc);
        

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Sample Name:"), gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        sampleNameField = new JTextField(20);
        formPanel.add(sampleNameField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        descriptionPane = new JTextPane();
        JScrollPane descScrollPane = new JScrollPane(descriptionPane);
        descScrollPane.setPreferredSize(new Dimension(400, 100));
        formPanel.add(descScrollPane, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Transcription:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        transcriptionPane = new JTextPane();
        JScrollPane transScrollPane = new JScrollPane(transcriptionPane);
        transScrollPane.setPreferredSize(new Dimension(400, 100));
        formPanel.add(transScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Keywords:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        keywordsPane = new JTextPane();
        JScrollPane keywordsScrollPane = new JScrollPane(keywordsPane);
        keywordsScrollPane.setPreferredSize(new Dimension(400, 60));
        formPanel.add(keywordsScrollPane, gbc);
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addTransButton = new JButton("Add");
        addTransButton.addActionListener(e -> {
            addTranscription();
        });
        
        editTransButton = new JButton("Edit");
        editTransButton.addActionListener(e -> {
            editTranscription();
        });
        
        removeTransButton = new JButton("Remove");
        removeTransButton.addActionListener(e -> {
            removeTranscription();
        });
        
        clearTransButton = new JButton("Clear");
        clearTransButton.addActionListener(e -> {
            clearTranscriptionFields();
        });
        
        buttonPanel.add(addTransButton);
        buttonPanel.add(editTransButton);
        buttonPanel.add(removeTransButton);
        buttonPanel.add(clearTransButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(buttonPanel, gbc);
        

        transcriptionPanel.add(searchPanel, BorderLayout.NORTH);
        transcriptionPanel.add(transcriptionScrollPane, BorderLayout.CENTER);
        transcriptionPanel.add(formPanel, BorderLayout.SOUTH);
    }
    
    private void createPrescriptionPanel() {
        prescriptionPanel = new JPanel();
        prescriptionPanel.setLayout(new BorderLayout(10, 10));
        

        String[] columns = {"Prescription ID", "Patient ID", "First Name", "Medicine Name", 
                           "Dosage", "Storage Condition", "Medicine ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        prescriptionTable = new JTable(model);
        prescriptionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && prescriptionTable.getSelectedRow() != -1) {
                int row = prescriptionTable.getSelectedRow();
                presIdField.setText(prescriptionTable.getValueAt(row, 0).toString());
                presPatientIdPane.setText(prescriptionTable.getValueAt(row, 1).toString());
                presFirstNamePane.setText(prescriptionTable.getValueAt(row, 2).toString());
                presMedNamePane.setText(prescriptionTable.getValueAt(row, 3).toString());
                presDosagePane.setText(prescriptionTable.getValueAt(row, 4).toString());
                presStoragePane.setText(prescriptionTable.getValueAt(row, 5).toString());
                presMedIdPane.setText(prescriptionTable.getValueAt(row, 6).toString());
            }
        });
        
        prescriptionScrollPane = new JScrollPane(prescriptionTable);
        

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Patient ID Search:");
        presSearchField = new JTextField(20);
        presSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                patientManager.searchPrescriptionsByPatientId(prescriptionTable, presSearchField.getText());
            }
        });
        
        refreshPresButton = new JButton("Refresh");
        refreshPresButton.addActionListener(e -> {
            patientManager.fillPrescriptionTable(prescriptionTable);
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(presSearchField);
        searchPanel.add(refreshPresButton);
        

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Basic Information"));
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5, 5, 5, 5);
        leftGbc.anchor = GridBagConstraints.WEST;
        

        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftPanel.add(new JLabel("Prescription ID:"), leftGbc);
        
        leftGbc.gridx = 1;
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
        leftGbc.weightx = 1.0;
        presIdField = new JTextField(15);
        presIdField.setEditable(false);
        leftPanel.add(presIdField, leftGbc);
        

        leftGbc.gridx = 0;
        leftGbc.gridy = 1;
        leftGbc.weightx = 0.0;
        leftPanel.add(new JLabel("Patient ID:"), leftGbc);
        
        leftGbc.gridx = 1;
        leftGbc.weightx = 1.0;
        presPatientIdPane = new JTextPane();
        presPatientIdPane.setEditable(false);
        JScrollPane patientIdScrollPane = new JScrollPane(presPatientIdPane);
        patientIdScrollPane.setPreferredSize(new Dimension(200, 30));
        leftPanel.add(patientIdScrollPane, leftGbc);
        

        leftGbc.gridx = 0;
        leftGbc.gridy = 2;
        leftGbc.weightx = 0.0;
        leftPanel.add(new JLabel("First Name:"), leftGbc);
        
        leftGbc.gridx = 1;
        leftGbc.weightx = 1.0;
        presFirstNamePane = new JTextPane();
        presFirstNamePane.setEditable(false);
        JScrollPane firstNameScrollPane = new JScrollPane(presFirstNamePane);
        firstNameScrollPane.setPreferredSize(new Dimension(200, 30));
        leftPanel.add(firstNameScrollPane, leftGbc);
        

        leftGbc.gridx = 0;
        leftGbc.gridy = 3;
        leftGbc.weightx = 0.0;
        leftPanel.add(new JLabel("Medicine Name:"), leftGbc);
        
        leftGbc.gridx = 1;
        leftGbc.weightx = 1.0;
        presMedNamePane = new JTextPane();
        presMedNamePane.setEditable(false);
        JScrollPane medNameScrollPane = new JScrollPane(presMedNamePane);
        medNameScrollPane.setPreferredSize(new Dimension(200, 30));
        leftPanel.add(medNameScrollPane, leftGbc);
        

        leftGbc.gridx = 0;
        leftGbc.gridy = 4;
        leftGbc.weightx = 0.0;
        leftPanel.add(new JLabel("Medicine ID:"), leftGbc);
        
        leftGbc.gridx = 1;
        leftGbc.weightx = 1.0;
        presMedIdPane = new JTextPane();
        presMedIdPane.setEditable(false);
        JScrollPane medIdScrollPane = new JScrollPane(presMedIdPane);
        medIdScrollPane.setPreferredSize(new Dimension(200, 30));
        leftPanel.add(medIdScrollPane, leftGbc);
        

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBorder(BorderFactory.createTitledBorder("Dosage"));
        presDosagePane = new JTextPane();
        presDosagePane.setEditable(false);
        JScrollPane dosageScrollPane = new JScrollPane(presDosagePane);
        middlePanel.add(dosageScrollPane, BorderLayout.CENTER);
        

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Storage Condition"));
        presStoragePane = new JTextPane();
        presStoragePane.setEditable(false);
        JScrollPane storageScrollPane = new JScrollPane(presStoragePane);
        rightPanel.add(storageScrollPane, BorderLayout.CENTER);
        

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        detailsPanel.add(leftPanel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        detailsPanel.add(middlePanel, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0.35;
        detailsPanel.add(rightPanel, gbc);
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        removePresButton = new JButton("Delete this Prescription");
        removePresButton.addActionListener(e -> {
            removePrescription();
        });
        
        buttonPanel.add(removePresButton);
        

        prescriptionPanel.add(searchPanel, BorderLayout.NORTH);
        prescriptionPanel.add(prescriptionScrollPane, BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(detailsPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        prescriptionPanel.add(southPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        patientManager.fillPatientTable(patientTable);
        patientManager.fillTranscriptionTable(transcriptionTable);
        patientManager.fillPrescriptionTable(prescriptionTable);
    }
    
    private void addPatient() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty() || phone.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter required fields (First Name, Last Name, Phone)", 
                "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (patientManager.addPatient(firstName, lastName, phone, email)) {
            JOptionPane.showMessageDialog(this, 
                "New Patient was added", 
                "Add Patient", JOptionPane.INFORMATION_MESSAGE);
            patientManager.fillPatientTable(patientTable);
            clearPatientFields();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Patient was not added", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editPatient() {
        String id = idField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        
        if (id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a patient first", 
                "No Patient Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty() || phone.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter required fields (First Name, Last Name, Phone)", 
                "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (patientManager.editPatient(id, firstName, lastName, phone, email)) {
            JOptionPane.showMessageDialog(this, 
                "Patient Information was edited", 
                "Edit Patient", JOptionPane.INFORMATION_MESSAGE);
            patientManager.fillPatientTable(patientTable);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Patient was not updated", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removePatient() {
        String id = idField.getText();
        
        if (id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a patient first", 
                "No Patient Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this patient?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (patientManager.removePatient(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Patient Information was deleted", 
                    "Delete Patient", JOptionPane.INFORMATION_MESSAGE);
                patientManager.fillPatientTable(patientTable);
                clearPatientFields();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Patient was not deleted, please delete their transcription/treatment plan first", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearPatientFields() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        createTransButton.setEnabled(false);
    }
    
    private void addTranscription() {
        String patientId = transPatientIdField.getText();
        String description = descriptionPane.getText();
        String medSpec = medSpecField.getText();
        String sampleName = sampleNameField.getText();
        String transcription = transcriptionPane.getText();
        String keywords = keywordsPane.getText();
        
        if (patientId.trim().isEmpty() || description.trim().isEmpty() || medSpec.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter required fields (Patient ID, Description, Medical Specialty)", 
                "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (patientManager.addTranscription(patientId, description, medSpec, sampleName, transcription, keywords)) {
            JOptionPane.showMessageDialog(this, 
                "New Transcription was added", 
                "Add Transcription", JOptionPane.INFORMATION_MESSAGE);
            patientManager.fillTranscriptionTable(transcriptionTable);
            clearTranscriptionFields();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Transcription for this patient already existed", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editTranscription() {
        if (transIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a transcription first", 
                "No Transcription Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int transId = Integer.parseInt(transIdField.getText());
        String patientId = transPatientIdField.getText();
        String description = descriptionPane.getText();
        String medSpec = medSpecField.getText();
        String sampleName = sampleNameField.getText();
        String transcription = transcriptionPane.getText();
        String keywords = keywordsPane.getText();
        
        if (patientId.trim().isEmpty() || description.trim().isEmpty() || medSpec.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter required fields (Patient ID, Description, Medical Specialty)", 
                "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (patientManager.editTranscription(transId, patientId, description, medSpec, sampleName, transcription, keywords)) {
            JOptionPane.showMessageDialog(this, 
                "Transcription was edited", 
                "Edit Transcription", JOptionPane.INFORMATION_MESSAGE);
            patientManager.fillTranscriptionTable(transcriptionTable);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Transcription was not updated", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeTranscription() {
        if (transIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a transcription first", 
                "No Transcription Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int transId = Integer.parseInt(transIdField.getText());
        
        if (patientManager.removeTranscription(transId)) {
            JOptionPane.showMessageDialog(this, 
                "Patient Transcription was deleted", 
                "Delete Transcription", JOptionPane.INFORMATION_MESSAGE);
            patientManager.fillTranscriptionTable(transcriptionTable);
            clearTranscriptionFields();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Transcription was not deleted", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearTranscriptionFields() {
        transIdField.setText("");
        transPatientIdField.setText("");
        descriptionPane.setText("");
        medSpecField.setText("");
        sampleNameField.setText("");
        transcriptionPane.setText("");
        keywordsPane.setText("");
    }
    
    private void removePrescription() {
        if (presIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a prescription first", 
                "No Prescription Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int presId = Integer.parseInt(presIdField.getText());
        
        if (patientManager.removePrescription(presId)) {
            JOptionPane.showMessageDialog(this, 
                "Patient Prescription was deleted", 
                "Delete Prescription", JOptionPane.INFORMATION_MESSAGE);
            patientManager.fillPrescriptionTable(prescriptionTable);
            clearPrescriptionFields();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Prescription was not deleted", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearPrescriptionFields() {
        presIdField.setText("");
        presPatientIdPane.setText("");
        presFirstNamePane.setText("");
        presMedNamePane.setText("");
        presMedIdPane.setText("");
        presDosagePane.setText("");
        presStoragePane.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PatientUI().setVisible(true);
        });
    }
}
