import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginUI extends JFrame {
    private JPanel mainPanel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginUI() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setSize(500, 350);
        
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(102, 204, 255));
        mainPanel.setLayout(null);
        
        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        usernameLabel.setBounds(120, 110, 100, 26);
        
        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        passwordLabel.setBounds(120, 180, 100, 26);
        
        usernameField = new JTextField();
        usernameField.setBounds(230, 110, 150, 26);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(230, 180, 150, 26);
        
        loginButton = new JButton("Login");
        loginButton.setBounds(200, 240, 100, 30);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        
        mainPanel.add(usernameLabel);
        mainPanel.add(passwordLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);
        mainPanel.add(loginButton);
        
        getContentPane().add(mainPanel);
    }
    
    private void loginButtonActionPerformed(ActionEvent evt) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username", "Username is empty", JOptionPane.WARNING_MESSAGE);
        } else if (password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter password", "Password is empty", JOptionPane.WARNING_MESSAGE);
        } else {
            boolean isValid;
            
            // Try JDBC first, fall back to mock database
            if (JdbcDatabaseConnection.isUsingJdbc()) {
                isValid = JdbcDatabaseConnection.validateLogin(username, password);
            } else {
                isValid = DatabaseConnection.validateLogin(username, password);
            }
            
            if (isValid) {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                mainMenu.setLocationRelativeTo(null);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class MainMenu extends JFrame {
        private JPanel leftPanel;
        private JPanel rightPanel;
        private JButton patientDataButton;
        private JButton medicineButton;
        
        public MainMenu() {
            initComponents();
        }
        
        private void initComponents() {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setTitle("Medical DB - Main Menu");
            setSize(850, 720);
            setLayout(new BorderLayout());
            
            leftPanel = new JPanel();
            leftPanel.setBackground(new Color(0, 51, 153));
            leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 580));
            
            rightPanel = new JPanel();
            rightPanel.setBackground(new Color(0, 102, 51));
            rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 580));
            
            patientDataButton = new JButton("Patient Data");
            patientDataButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
            patientDataButton.setPreferredSize(new Dimension(360, 90));
            patientDataButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    PatientUI patientUI = new PatientUI();
                    patientUI.setVisible(true);
                    patientUI.setLocationRelativeTo(null);
                    patientUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
            });
            
            medicineButton = new JButton("Medicine");
            medicineButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
            medicineButton.setPreferredSize(new Dimension(360, 90));
            medicineButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    MedicineUI medicineUI = new MedicineUI();
                    medicineUI.setVisible(true);
                    medicineUI.setLocationRelativeTo(null);
                    medicineUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
            });
            
            leftPanel.add(patientDataButton);
            rightPanel.add(medicineButton);
            
            add(leftPanel, BorderLayout.WEST);
            add(rightPanel, BorderLayout.EAST);
        }
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginUI().setVisible(true);
            }
        });
        
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                JdbcDatabaseConnection.closeConnection();
            }
        });
    }
}
