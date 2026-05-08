package db;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GymMemberForm extends JFrame {

    private static final String DB_URL  = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASS = "hari";

    private static final int MIN_AGE    = 14;
    private static final int MAX_AGE    = 50;
    private static final double MIN_WEIGHT = 38.0;
    private static final double MAX_WEIGHT = 125.0;

    private Connection conn;

    private JPanel insertPanel;
    private JPanel updatePanel;
    private JPanel deletePanel;
    private JPanel viewPanel;
    private JPanel currentPanel;

    private JTextField ins_MemberId, ins_Name, ins_Age, ins_Weight, ins_BranchId;
    private JTextField ins_AddrCity, ins_AddrStreet, ins_AddrPincode;
    private JTextField ins_Mail;
    private JLabel ins_MemberIdHint, ins_NameHint, ins_AgeHint, ins_WeightHint;
    private JLabel ins_BranchIdHint, ins_AddrCityHint, ins_AddrStreetHint;
    private JLabel ins_AddrPincodeHint, ins_MailHint;

    private ButtonGroup ins_GenderGroup, ins_BodyTypeGroup;
    private JRadioButton ins_GenderMale, ins_GenderFemale, ins_GenderOther;
    private JRadioButton ins_BodyMeso, ins_BodyEndo, ins_BodyEcto;

    private JTextField upd_MemberId, upd_Weight, upd_AddrCity, upd_AddrStreet, upd_AddrPincode;
    private JTextField upd_Mail;
    private JLabel upd_MemberIdHint, upd_WeightHint, upd_AddrCityHint;
    private JLabel upd_AddrStreetHint, upd_AddrPincodeHint, upd_MailHint;

    private JTextField del_MemberId;
    private JLabel del_MemberIdHint;

    private JTable viewTable;
    private DefaultTableModel viewTableModel;

    private JLabel statusLabel;

    public GymMemberForm() {
        setTitle("Gym Management System – Member Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 920);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        connectDB();
        buildMenuBar();
        buildAllPanels();
        buildStatusBar();

        showPanel(insertPanel);
        setVisible(true);
    }

    private void connectDB() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                "Oracle JDBC driver not found.\nAdd ojdbc8.jar to classpath.",
                "Driver Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            showDbWarning("Could not connect to the database. Please check your connection settings.");
        }
    }

    private void showDbWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private String friendlyOraMessage(int code, String rawMsg) {
        switch (code) {
            case 1:
                return "A member with this ID already exists. Please use a different Member ID.";
            case 1400:
                return "A required field is missing. Please fill in all mandatory fields.";
            case 2291:
                return "No such branch exists. Please enter a valid Branch ID.";
            case 2292:
                return "This member has linked records. Remove associated records before deleting.";
            case 2290:
                if (rawMsg != null) {
                    String r = rawMsg.toUpperCase();
                    if (r.contains("CHK_AGE") || r.contains("CHK_MEMBER_AGE"))
                        return "Age value is not within the allowed range.";
                    if (r.contains("CHK_MEMBER_GENDER"))
                        return "Gender value is not valid.";
                    if (r.contains("CHK_MEMBER_BODYTYPE"))
                        return "Body type value is not valid.";
                }
                return "One or more values failed a constraint check. Please review your input.";
            case 904:
                return "An internal column reference error occurred. Contact the administrator.";
            case 942:
                return "The required database table does not exist. Contact the administrator.";
            case 1017:
                return "Database login failed. Please check the credentials in the source code.";
            case 12541:
                return "Cannot reach the database. Ensure Oracle XE is running.";
            case 17002:
                return "Cannot connect to the database host. Ensure Oracle XE is running on localhost:1521.";
            default:
                return "An unexpected database error occurred. Please try again.";
        }
    }

    // ─── Validation helpers ──────────────────────────────────────────────────

    private String validateMemberId(String raw) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return "Member ID is required.";
        if (!t.matches("\\d+")) return "Member ID must be a whole number.";
        long v;
        try { v = Long.parseLong(t); } catch (NumberFormatException e) { return "Member ID must be a valid number."; }
        if (v <= 0) return "Member ID must be greater than 0.";
        return null;
    }

    private String validateBranchId(String raw) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return "Branch ID is required.";
        if (!t.matches("\\d+")) return "Branch ID must be a whole number.";
        long v;
        try { v = Long.parseLong(t); } catch (NumberFormatException e) { return "Branch ID must be a valid number."; }
        if (v <= 0) return "Branch ID must be greater than 0.";
        return null;
    }

    private String validateAge(String raw) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return "Age is required.";
        if (!t.matches("\\d+")) return "Age must be a whole number.";
        int age;
        try { age = Integer.parseInt(t); } catch (NumberFormatException e) { return "Age must be a whole number."; }
        if (age < MIN_AGE) return "Age is too low. Minimum allowed age is " + MIN_AGE + ".";
        if (age > MAX_AGE) return "Age is too high. Maximum allowed age is " + MAX_AGE + ".";
        return null;
    }

    private String validateWeight(String raw) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return "Weight is required.";
        if (!t.matches("\\d*\\.?\\d*") || t.equals(".")) return "Weight must be a valid number (e.g. 65.5).";
        double v;
        try { v = Double.parseDouble(t); } catch (NumberFormatException e) { return "Weight must be a valid number."; }
        if (v < MIN_WEIGHT || v > MAX_WEIGHT)
            return "Weight must be between " + MIN_WEIGHT + " and " + MAX_WEIGHT + " kg.";
        return null;
    }

    private String validateWeightOptional(String raw) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return null;
        return validateWeight(raw);
    }

    private String validateVarchar(String raw, String fieldName, int maxLen) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return fieldName + " is required.";
        if (t.matches("\\d+")) return fieldName + " cannot be a pure number.";
        if (t.length() > maxLen) return fieldName + " is too long. Maximum " + maxLen + " characters.";
        return null;
    }

    private String validatePincode(String raw) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return "Pincode is required.";
        if (!t.matches("\\d+")) return "Pincode must contain digits only.";
        if (t.length() < 4 || t.length() > 10) return "Pincode must be between 4 and 10 digits.";
        return null;
    }

    private String validatePincodeOptional(String raw) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return null;
        return validatePincode(raw);
    }

    private String validateGender(String raw) {
        if (raw == null) return "Gender is required.";
        String t = raw.trim();
        if (!t.equalsIgnoreCase("Male") && !t.equalsIgnoreCase("Female") && !t.equalsIgnoreCase("Other"))
            return "Gender must be Male, Female, or Other.";
        return null;
    }

    private String validateBodyType(String raw) {
        if (raw == null) return "Body type is required.";
        String t = raw.trim();
        if (!t.equalsIgnoreCase("Mesoderm") && !t.equalsIgnoreCase("Endoderm") && !t.equalsIgnoreCase("Ectoderm"))
            return "Body type must be Mesoderm, Endoderm, or Ectoderm.";
        return null;
    }

    private String validateEmail(String raw) {
        String mail = raw == null ? "" : raw.trim().toLowerCase();
        if (mail.isEmpty()) return "Email is required.";
        if (!mail.contains("@")) return "Missing '@' \u2014 \"" + mail + "\" is not a valid email.";
        String[] parts = mail.split("@", 2);
        String user   = parts[0];
        String domain = parts.length > 1 ? parts[1] : "";
        if (user.isEmpty())  return "Email needs a username before '@'.";
        if (!domain.contains(".")) return "Domain \"" + domain + "\" has no dot \u2014 try @gmail.com etc.";
        String[] dp = domain.split("\\.");
        String ext  = dp[dp.length - 1];
        if (ext.length() < 2) return "Extension '." + ext + "' is too short.";
        if (ext.length() > 6) return "Extension '." + ext + "' looks unusual.";
        if (!user.matches("[a-z0-9._%+\\-]+"))
            return "Username has invalid characters before '@'.";
        if (!domain.matches("[a-z0-9.\\-]+"))
            return "Domain has invalid characters after '@'.";
        if (mail.contains("..")) return "Email contains consecutive dots '..'.";
        if (mail.length() > 20)
            return "Email is too long. Maximum 20 characters allowed.";
        return null;
    }

    private String validateEmailOptional(String raw) {
        String t = raw == null ? "" : raw.trim();
        if (t.isEmpty()) return null;
        return validateEmail(raw);
    }

    // ─── Menu Bar ────────────────────────────────────────────────────────────

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(Box.createHorizontalGlue());

        JMenu manageMenu = new JMenu("Manage Members");
        JMenuItem insertItem = new JMenuItem("Add New Member");
        JMenuItem updateItem = new JMenuItem("Edit Member");
        JMenuItem deleteItem = new JMenuItem("Remove Member");
        JMenuItem viewItem   = new JMenuItem("Display Members");

        insertItem.addActionListener(e -> showPanel(insertPanel));
        updateItem.addActionListener(e -> showPanel(updatePanel));
        deleteItem.addActionListener(e -> showPanel(deletePanel));
        viewItem.addActionListener(e -> { loadViewTable(); showPanel(viewPanel); });

        manageMenu.add(insertItem);
        manageMenu.add(updateItem);
        manageMenu.add(deleteItem);
        manageMenu.addSeparator();
        manageMenu.add(viewItem);

        JButton consoleButton = new JButton("Console");
        consoleButton.setFocusPainted(false);
        consoleButton.addActionListener(e -> showConsoleMenu());

        menuBar.add(manageMenu);
        menuBar.add(Box.createHorizontalStrut(8));
        menuBar.add(consoleButton);
        setJMenuBar(menuBar);
    }

    private void showConsoleMenu() {
        String[] options = {
            "1. Insert Member",
            "2. Delete Member",
            "3. Update Member",
            "4. Display Member",
            "5. Exit"
        };
        JList<String> list = new JList<>(options);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Monospaced", Font.PLAIN, 14));
        list.setVisibleRowCount(5);
        list.setSelectedIndex(0);

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Window w = SwingUtilities.getWindowAncestor(list);
                    if (w instanceof JDialog) ((JDialog) w).dispose();
                    handleConsoleChoice(list.getSelectedIndex() + 1);
                }
            }
        });

        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(BorderFactory.createTitledBorder("Console Operations"));

        int result = JOptionPane.showConfirmDialog(this, sp,
            "Console Menu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int choice = list.getSelectedIndex() + 1;
            handleConsoleChoice(choice);
        }
    }

    private void handleConsoleChoice(int choice) {
        switch (choice) {
            case 1: consoleInsert();  break;
            case 2: consoleDelete();  break;
            case 3: consoleUpdate();  break;
            case 4: consoleDisplay(); break;
            case 5:
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Exit the application?", "Exit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) System.exit(0);
                break;
        }
    }

    private void buildAllPanels() {
        insertPanel = buildInsertPanel();
        updatePanel = buildUpdatePanel();
        deletePanel = buildDeletePanel();
        viewPanel   = buildViewPanel();
    }

    private void showPanel(JPanel panel) {
        if (currentPanel != null) remove(currentPanel);
        currentPanel = panel;
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // ─── Insert Panel ────────────────────────────────────────────────────────

    private JPanel buildInsertPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("  Register New Member", JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 44, 10, 44));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 2, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        ins_MemberId     = new JTextField(22);
        ins_MemberIdHint = makeHintLabel();
        addFieldWithHint(form, gc, "Member ID *", ins_MemberId, ins_MemberIdHint, 0);
        addNumericListener(ins_MemberId, ins_MemberIdHint, false);

        ins_Name     = new JTextField(22);
        ins_NameHint = makeHintLabel();
        addFieldWithHint(form, gc, "Member Name *", ins_Name, ins_NameHint, 2);
        addVarcharListener(ins_Name, ins_NameHint, 50, false);

        addLabel(form, gc, "Gender *", 0, 4);
        ins_GenderGroup  = new ButtonGroup();
        ins_GenderMale   = new JRadioButton("Male");
        ins_GenderFemale = new JRadioButton("Female");
        ins_GenderOther  = new JRadioButton("Other");
        ins_GenderMale.setSelected(true);
        ins_GenderGroup.add(ins_GenderMale);
        ins_GenderGroup.add(ins_GenderFemale);
        ins_GenderGroup.add(ins_GenderOther);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        genderPanel.add(ins_GenderMale);
        genderPanel.add(ins_GenderFemale);
        genderPanel.add(ins_GenderOther);
        gc.gridx = 1; gc.gridy = 4; gc.gridwidth = 3;
        form.add(genderPanel, gc);
        gc.gridx = 1; gc.gridy = 5; gc.gridwidth = 3;
        form.add(new JLabel(" "), gc);

        ins_Age     = new JTextField(22);
        ins_AgeHint = makeHintLabel();
        addFieldWithHint(form, gc, "Age *", ins_Age, ins_AgeHint, 6);
        ins_Age.getDocument().addDocumentListener(new QuickDocListener() {
            public void update() { validateAgeField(ins_Age, ins_AgeHint); }
        });

        addLabel(form, gc, "Body Type *", 0, 8);
        ins_BodyTypeGroup = new ButtonGroup();
        ins_BodyMeso = new JRadioButton("Mesoderm");
        ins_BodyEndo = new JRadioButton("Endoderm");
        ins_BodyEcto = new JRadioButton("Ectoderm");
        ins_BodyMeso.setSelected(true);
        ins_BodyTypeGroup.add(ins_BodyMeso);
        ins_BodyTypeGroup.add(ins_BodyEndo);
        ins_BodyTypeGroup.add(ins_BodyEcto);
        JPanel bodyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        bodyPanel.add(ins_BodyMeso);
        bodyPanel.add(ins_BodyEndo);
        bodyPanel.add(ins_BodyEcto);
        gc.gridx = 1; gc.gridy = 8; gc.gridwidth = 3;
        form.add(bodyPanel, gc);
        gc.gridx = 1; gc.gridy = 9; gc.gridwidth = 3;
        form.add(new JLabel(" "), gc);

        ins_Weight     = new JTextField(22);
        ins_WeightHint = makeHintLabel();
        addFieldWithHint(form, gc, "Weight (kg) *", ins_Weight, ins_WeightHint, 10);
        addWeightListener(ins_Weight, ins_WeightHint, false);

        ins_BranchId     = new JTextField(22);
        ins_BranchIdHint = makeHintLabel();
        addFieldWithHint(form, gc, "Branch ID *", ins_BranchId, ins_BranchIdHint, 12);
        addNumericListener(ins_BranchId, ins_BranchIdHint, false);

        ins_AddrCity     = new JTextField(22);
        ins_AddrCityHint = makeHintLabel();
        addFieldWithHint(form, gc, "Address City *", ins_AddrCity, ins_AddrCityHint, 14);
        addVarcharListener(ins_AddrCity, ins_AddrCityHint, 30, false);

        ins_AddrStreet     = new JTextField(22);
        ins_AddrStreetHint = makeHintLabel();
        addFieldWithHint(form, gc, "Address Street *", ins_AddrStreet, ins_AddrStreetHint, 16);
        addVarcharListener(ins_AddrStreet, ins_AddrStreetHint, 30, false);

        ins_AddrPincode     = new JTextField(22);
        ins_AddrPincodeHint = makeHintLabel();
        addFieldWithHint(form, gc, "Pincode * ", ins_AddrPincode, ins_AddrPincodeHint, 18);
        addPincodeListener(ins_AddrPincode, ins_AddrPincodeHint, false);

        ins_Mail     = new JTextField(22);
        ins_MailHint = makeHintLabel();
        addFieldWithHint(form, gc, "Email *", ins_Mail, ins_MailHint, 20);
        ins_Mail.getDocument().addDocumentListener(new QuickDocListener() {
            public void update() { updateMailHint(ins_Mail, ins_MailHint); }
        });

        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 16));
        JButton btnInsert = new JButton("Insert");
        JButton btnClear  = new JButton("Clear");
        btnInsert.setPreferredSize(new Dimension(110, 36));
        btnClear.setPreferredSize(new Dimension(110, 36));
        btnInsert.addActionListener(e -> doInsert());
        btnClear.addActionListener(e -> clearInsertForm());
        btnPanel.add(btnInsert);
        btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ─── Update Panel ────────────────────────────────────────────────────────

    private JPanel buildUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("  Edit Member Information", JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(22, 44, 22, 44));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 2, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        upd_MemberId     = new JTextField(22);
        upd_MemberIdHint = makeHintLabel();
        addFieldWithHint(form, gc, "Member ID (to update) *", upd_MemberId, upd_MemberIdHint, 0);
        addNumericListener(upd_MemberId, upd_MemberIdHint, false);

        upd_Weight     = new JTextField(22);
        upd_WeightHint = makeHintLabel();
        addFieldWithHint(form, gc, "New Weight (kg)", upd_Weight, upd_WeightHint, 2);
        addWeightListener(upd_Weight, upd_WeightHint, true);

        upd_AddrCity     = new JTextField(22);
        upd_AddrCityHint = makeHintLabel();
        addFieldWithHint(form, gc, "New Address City", upd_AddrCity, upd_AddrCityHint, 4);
        addVarcharListener(upd_AddrCity, upd_AddrCityHint, 30, true);

        upd_AddrStreet     = new JTextField(22);
        upd_AddrStreetHint = makeHintLabel();
        addFieldWithHint(form, gc, "New Address Street", upd_AddrStreet, upd_AddrStreetHint, 6);
        addVarcharListener(upd_AddrStreet, upd_AddrStreetHint, 30, true);

        upd_AddrPincode     = new JTextField(22);
        upd_AddrPincodeHint = makeHintLabel();
        addFieldWithHint(form, gc, "New Pincode ", upd_AddrPincode, upd_AddrPincodeHint, 8);
        addPincodeListener(upd_AddrPincode, upd_AddrPincodeHint, true);

        upd_Mail     = new JTextField(22);
        upd_MailHint = makeHintLabel();
        addFieldWithHint(form, gc, "New Email", upd_Mail, upd_MailHint, 10);
        upd_Mail.getDocument().addDocumentListener(new QuickDocListener() {
            public void update() { updateMailHint(upd_Mail, upd_MailHint); }
        });

        panel.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 16));
        JButton btnUpdate = new JButton("Update");
        JButton btnClear  = new JButton("Clear");
        btnUpdate.setPreferredSize(new Dimension(110, 36));
        btnClear.setPreferredSize(new Dimension(110, 36));
        btnUpdate.addActionListener(e -> doUpdate());
        btnClear.addActionListener(e -> clearUpdateForm());
        btnPanel.add(btnUpdate);
        btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ─── Delete Panel ────────────────────────────────────────────────────────

    private JPanel buildDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("  Remove Member", JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(60, 44, 22, 44));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 2, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        del_MemberId     = new JTextField(22);
        del_MemberIdHint = makeHintLabel();
        addFieldWithHint(form, gc, "Member ID to Delete *", del_MemberId, del_MemberIdHint, 0);
        addNumericListener(del_MemberId, del_MemberIdHint, false);

        JLabel warn = new JLabel("  \u26a0  This permanently deletes the member and all related records.");
        warn.setForeground(Color.RED.darker());
        warn.setFont(new Font("Arial", Font.ITALIC, 11));
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 4;
        form.add(warn, gc);

        JLabel oraWarn = new JLabel("  \u26a0  If linked records exist, remove them first before deleting.");
        oraWarn.setForeground(new Color(160, 80, 0));
        oraWarn.setFont(new Font("Arial", Font.ITALIC, 11));
        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 4;
        form.add(oraWarn, gc);

        panel.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 16));
        JButton btnDelete = new JButton("Delete");
        JButton btnClear  = new JButton("Clear");
        btnDelete.setPreferredSize(new Dimension(110, 36));
        btnClear.setPreferredSize(new Dimension(110, 36));
        btnDelete.addActionListener(e -> doDelete());
        btnClear.addActionListener(e -> {
            del_MemberId.setText("");
            del_MemberIdHint.setText(" ");
        });
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ─── View Panel ──────────────────────────────────────────────────────────

    private JPanel buildViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel titleBar = new JPanel(new BorderLayout());
        JLabel title = new JLabel("  All Registered Members", JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(12, 12, 12, 12));

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBorder(new EmptyBorder(10, 16, 10, 16));
        btnRefresh.addActionListener(e -> loadViewTable());
        titleBar.add(title, BorderLayout.CENTER);
        titleBar.add(btnRefresh, BorderLayout.EAST);
        panel.add(titleBar, BorderLayout.NORTH);

        String[] columns = {
            "Member ID", "Branch ID", "Member Name", "Gender", "Age",
            "Body Type", "Weight", "Date of Join", "City", "Street", "Pincode", "Email"
        };
        viewTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        viewTable = new JTable(viewTableModel);
        viewTable.setRowHeight(26);
        viewTable.setFont(new Font("Arial", Font.PLAIN, 12));
        viewTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        viewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] colWidths = {80, 80, 130, 70, 50, 90, 65, 100, 90, 100, 80, 160};
        for (int i = 0; i < colWidths.length; i++) {
            viewTable.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(viewTable,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton btnPrint = new JButton("Display Selected Member to Console");
        btnPrint.addActionListener(e -> {
            int row = viewTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(panel, "Please select a row first.");
                return;
            }
            int memberId = Integer.parseInt(viewTableModel.getValueAt(row, 0).toString());
            printMemberToConsole(memberId);
        });
        bottomBar.add(btnPrint);
        panel.add(bottomBar, BorderLayout.SOUTH);

        return panel;
    }

    private void buildStatusBar() {
        statusLabel = new JLabel("  Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLabel.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, UIManager.getColor("Separator.foreground")),
            new EmptyBorder(4, 10, 4, 10)));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void setStatus(String msg) { statusLabel.setText("  " + msg); }

    // ─── Hint label helpers ──────────────────────────────────────────────────

    private JLabel makeHintLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(new Font("Arial", Font.ITALIC, 11));
        l.setForeground(Color.GRAY);
        return l;
    }

    private void addFieldWithHint(JPanel panel, GridBagConstraints gc,
                                   String label, JTextField field,
                                   JLabel hint, int baseRow) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        gc.gridx = 0; gc.gridy = baseRow; gc.gridwidth = 1;
        panel.add(lbl, gc);

        gc.gridx = 1; gc.gridy = baseRow; gc.gridwidth = 3;
        panel.add(field, gc);

        gc.gridx = 1; gc.gridy = baseRow + 1; gc.gridwidth = 3;
        panel.add(hint, gc);
    }

    // ─── Document listeners ──────────────────────────────────────────────────

    private void addNumericListener(JTextField field, JLabel hint, boolean optional) {
        field.getDocument().addDocumentListener(new QuickDocListener() {
            public void update() {
                String t = field.getText().trim();
                if (t.isEmpty()) {
                    hint.setText(optional ? " " : "\u26a0 This field is required.");
                    hint.setForeground(optional ? Color.GRAY : Color.RED.darker());
                    return;
                }
                if (!t.matches("\\d+")) {
                    hint.setText("\u26a0 Enter a valid whole number.");
                    hint.setForeground(Color.RED.darker());
                    return;
                }
                try {
                    long v = Long.parseLong(t);
                    if (v <= 0) {
                        hint.setText("\u26a0 Value must be greater than 0.");
                        hint.setForeground(Color.RED.darker());
                    } else {
                        hint.setText("\u2714 Valid.");
                        hint.setForeground(new Color(0, 128, 0));
                    }
                } catch (NumberFormatException ex) {
                    hint.setText("\u26a0 Enter a valid number.");
                    hint.setForeground(Color.RED.darker());
                }
            }
        });
    }

    private void addWeightListener(JTextField field, JLabel hint, boolean optional) {
        field.getDocument().addDocumentListener(new QuickDocListener() {
            public void update() {
                String t = field.getText().trim();
                if (t.isEmpty()) {
                    hint.setText(optional ? " " : "\u26a0 This field is required.");
                    hint.setForeground(optional ? Color.GRAY : Color.RED.darker());
                    return;
                }
                if (!t.matches("\\d*\\.?\\d*") || t.equals(".")) {
                    hint.setText("\u26a0 Enter a valid number.");
                    hint.setForeground(Color.RED.darker());
                    return;
                }
                try {
                    double v = Double.parseDouble(t);
                    if (v < MIN_WEIGHT || v > MAX_WEIGHT) {
                        hint.setText("\u26a0 Weight must be " + MIN_WEIGHT + "\u2013" + MAX_WEIGHT + " kg.");
                        hint.setForeground(Color.RED.darker());
                    } else {
                        hint.setText("\u2714 Valid.");
                        hint.setForeground(new Color(0, 128, 0));
                    }
                } catch (NumberFormatException ex) {
                    hint.setText("\u26a0 Enter a valid number.");
                    hint.setForeground(Color.RED.darker());
                }
            }
        });
    }

    private void addVarcharListener(JTextField field, JLabel hint, int maxLen, boolean optional) {
        field.getDocument().addDocumentListener(new QuickDocListener() {
            public void update() {
                String t = field.getText().trim();
                if (t.isEmpty()) {
                    hint.setText(optional ? " " : "\u26a0 This field is required.");
                    hint.setForeground(optional ? Color.GRAY : Color.RED.darker());
                    return;
                }
                if (t.matches("\\d+")) {
                    hint.setText("\u26a0 This field cannot be a pure number.");
                    hint.setForeground(Color.RED.darker());
                    return;
                }
                if (t.length() > maxLen) {
                    hint.setText("\u26a0 Too long. Maximum " + maxLen + " characters allowed.");
                    hint.setForeground(Color.RED.darker());
                    return;
                }
                hint.setText("\u2714 Valid (" + t.length() + "/" + maxLen + ").");
                hint.setForeground(new Color(0, 128, 0));
            }
        });
    }

    private void addPincodeListener(JTextField field, JLabel hint, boolean optional) {
        field.getDocument().addDocumentListener(new QuickDocListener() {
            public void update() {
                String t = field.getText().trim();
                if (t.isEmpty()) {
                    hint.setText(optional ? " " : "\u26a0 Pincode is required.");
                    hint.setForeground(optional ? Color.GRAY : Color.RED.darker());
                    return;
                }
                if (!t.matches("\\d+")) {
                    hint.setText("\u26a0 Pincode must contain digits only.");
                    hint.setForeground(Color.RED.darker());
                    return;
                }
                if (t.length() < 4 || t.length() > 10) {
                    hint.setText("\u26a0 Pincode must be 4\u201310 digits.");
                    hint.setForeground(Color.RED.darker());
                    return;
                }
                hint.setText("\u2714 Valid (" + t.length() + " digits).");
                hint.setForeground(new Color(0, 128, 0));
            }
        });
    }

    private void validateAgeField(JTextField field, JLabel hint) {
        String t = field.getText().trim();
        if (t.isEmpty()) {
            hint.setText("\u26a0 Age is required.");
            hint.setForeground(Color.RED.darker());
            return;
        }
        if (!t.matches("\\d+")) {
            hint.setText("\u26a0 Age must be a whole number.");
            hint.setForeground(Color.RED.darker());
            return;
        }
        try {
            int age = Integer.parseInt(t);
            if (age < MIN_AGE) {
                hint.setText("\u26a0 Minimum age is " + MIN_AGE + ".");
                hint.setForeground(Color.RED.darker());
            } else if (age > MAX_AGE) {
                hint.setText("\u26a0 Maximum age is " + MAX_AGE + ".");
                hint.setForeground(Color.RED.darker());
            } else {
                hint.setText("\u2714 Age is valid.");
                hint.setForeground(new Color(0, 128, 0));
            }
        } catch (NumberFormatException ex) {
            hint.setText("\u26a0 Age must be a whole number.");
            hint.setForeground(Color.RED.darker());
        }
    }

    private void updateMailHint(JTextField field, JLabel hint) {
        String text = field.getText();
        if (text.trim().isEmpty()) {
            hint.setText(" ");
            hint.setForeground(Color.GRAY);
            return;
        }
        String error = validateEmail(text);
        if (error != null) {
            hint.setText("\u26a0 " + error);
            hint.setForeground(Color.RED.darker());
        } else {
            String[] p = text.trim().toLowerCase().split("@", 2);
            hint.setText("\u2714 " + p[0] + " @ " + p[1] + " \u2014 valid.");
            hint.setForeground(new Color(0, 128, 0));
        }
    }

    private String getSelectedGender() {
        if (ins_GenderMale.isSelected())   return "Male";
        if (ins_GenderFemale.isSelected()) return "Female";
        if (ins_GenderOther.isSelected())  return "Other";
        return null;
    }

    private String getSelectedBodyType() {
        if (ins_BodyMeso.isSelected()) return "Mesoderm";
        if (ins_BodyEndo.isSelected()) return "Endoderm";
        if (ins_BodyEcto.isSelected()) return "Ectoderm";
        return null;
    }

    // ─── GUI CRUD operations ─────────────────────────────────────────────────

    private void doInsert() {
        if (ins_MemberId.getText().trim().isEmpty()
                || ins_Name.getText().trim().isEmpty()
                || ins_Age.getText().trim().isEmpty()
                || ins_Weight.getText().trim().isEmpty()
                || ins_BranchId.getText().trim().isEmpty()
                || ins_AddrCity.getText().trim().isEmpty()
                || ins_AddrStreet.getText().trim().isEmpty()
                || ins_AddrPincode.getText().trim().isEmpty()
                || ins_Mail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "All fields marked * are required.\nPlease fill in every field before inserting.",
                "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String err;

        err = validateMemberId(ins_MemberId.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); ins_MemberId.requestFocus(); return; }

        err = validateBranchId(ins_BranchId.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); ins_BranchId.requestFocus(); return; }

        err = validateAge(ins_Age.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Validation Error", JOptionPane.WARNING_MESSAGE); ins_Age.requestFocus(); return; }

        err = validateWeight(ins_Weight.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Validation Error", JOptionPane.WARNING_MESSAGE); ins_Weight.requestFocus(); return; }

        err = validateVarchar(ins_Name.getText(), "Member Name", 50);
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); ins_Name.requestFocus(); return; }

        err = validateVarchar(ins_AddrCity.getText(), "Address City", 30);
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); ins_AddrCity.requestFocus(); return; }

        err = validateVarchar(ins_AddrStreet.getText(), "Address Street", 30);
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); ins_AddrStreet.requestFocus(); return; }

        err = validatePincode(ins_AddrPincode.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); ins_AddrPincode.requestFocus(); return; }

        err = validateEmail(ins_Mail.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Invalid Email", JOptionPane.WARNING_MESSAGE); ins_Mail.requestFocus(); return; }

        String gender   = getSelectedGender();
        String bodyType = getSelectedBodyType();
        if (gender == null || bodyType == null) {
            JOptionPane.showMessageDialog(this, "Please select Gender and Body Type.");
            return;
        }

        int memberId  = Integer.parseInt(ins_MemberId.getText().trim());
        int branchId  = Integer.parseInt(ins_BranchId.getText().trim());
        int age       = Integer.parseInt(ins_Age.getText().trim());
        double weight = Double.parseDouble(ins_Weight.getText().trim());

        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO MEMBER " +
                "(MEMBER_ID, BRANCH_ID, MEMBER_NAME, GENDER, AGE, BODY_TYPE, WEIGHT, DATE_OF_JOIN, " +
                " ADDRESS_CITY, ADDRESS_STREET, ADDRESS_PINCODE, MAIL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?)");
            ps.setInt   (1,  memberId);
            ps.setInt   (2,  branchId);
            ps.setString(3,  ins_Name.getText().trim());
            ps.setString(4,  gender);
            ps.setInt   (5,  age);
            ps.setString(6,  bodyType);
            ps.setDouble(7,  weight);
            ps.setString(8,  ins_AddrCity.getText().trim());
            ps.setString(9,  ins_AddrStreet.getText().trim());
            ps.setString(10, ins_AddrPincode.getText().trim());
            ps.setString(11, ins_Mail.getText().trim().toLowerCase());
            ps.executeUpdate();
            conn.commit();
            setStatus("Insert successful \u2013 Member #" + memberId);
            JOptionPane.showMessageDialog(this,
                "Member registered successfully!\nMember ID: " + memberId);
            printMemberToConsole(memberId);
            clearInsertForm();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { }
            showDbWarning(friendlyOraMessage(e.getErrorCode(), e.getMessage()));
        }
    }

    private void doUpdate() {
        String err;

        err = validateMemberId(upd_MemberId.getText());
        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Missing Field", JOptionPane.WARNING_MESSAGE);
            upd_MemberId.requestFocus();
            return;
        }

        err = validateWeightOptional(upd_Weight.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Validation Error", JOptionPane.WARNING_MESSAGE); upd_Weight.requestFocus(); return; }

        if (!upd_AddrCity.getText().trim().isEmpty()) {
            err = validateVarchar(upd_AddrCity.getText(), "Address City", 30);
            if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); upd_AddrCity.requestFocus(); return; }
        }
        if (!upd_AddrStreet.getText().trim().isEmpty()) {
            err = validateVarchar(upd_AddrStreet.getText(), "Address Street", 30);
            if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); upd_AddrStreet.requestFocus(); return; }
        }
        err = validatePincodeOptional(upd_AddrPincode.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.WARNING_MESSAGE); upd_AddrPincode.requestFocus(); return; }

        err = validateEmailOptional(upd_Mail.getText());
        if (err != null) { JOptionPane.showMessageDialog(this, err, "Invalid Email", JOptionPane.WARNING_MESSAGE); upd_Mail.requestFocus(); return; }

        int memberId = Integer.parseInt(upd_MemberId.getText().trim());

        try {
            conn.setAutoCommit(false);
            StringBuilder sb = new StringBuilder("UPDATE MEMBER SET MEMBER_ID=MEMBER_ID");

            if (!upd_Weight.getText().trim().isEmpty())
                sb.append(", WEIGHT=").append(Double.parseDouble(upd_Weight.getText().trim()));
            if (!upd_AddrCity.getText().trim().isEmpty())
                sb.append(", ADDRESS_CITY='").append(upd_AddrCity.getText().trim()).append("'");
            if (!upd_AddrStreet.getText().trim().isEmpty())
                sb.append(", ADDRESS_STREET='").append(upd_AddrStreet.getText().trim()).append("'");
            if (!upd_AddrPincode.getText().trim().isEmpty())
                sb.append(", ADDRESS_PINCODE='").append(upd_AddrPincode.getText().trim()).append("'");
            if (!upd_Mail.getText().trim().isEmpty())
                sb.append(", MAIL='").append(upd_Mail.getText().trim().toLowerCase()).append("'");

            sb.append(" WHERE MEMBER_ID=?");
            PreparedStatement ps = conn.prepareStatement(sb.toString());
            ps.setInt(1, memberId);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                    "No member found with Member ID: " + memberId,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            conn.commit();
            setStatus("Update successful \u2013 Member #" + memberId);
            JOptionPane.showMessageDialog(this, "Member updated successfully!");
            printMemberToConsole(memberId);
            clearUpdateForm();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { }
            showDbWarning(friendlyOraMessage(e.getErrorCode(), e.getMessage()));
        }
    }

    private void doDelete() {
        String err = validateMemberId(del_MemberId.getText());
        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Missing Field", JOptionPane.WARNING_MESSAGE);
            del_MemberId.requestFocus();
            return;
        }

        int memberId = Integer.parseInt(del_MemberId.getText().trim());

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete Member #" + memberId + "?\nThis action cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("DELETE FROM MEMBER WHERE MEMBER_ID=?");
            ps.setInt(1, memberId);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                    "No member found with Member ID: " + memberId,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            conn.commit();
            setStatus("Deleted Member #" + memberId);
            JOptionPane.showMessageDialog(this, "Member deleted successfully!");
            del_MemberId.setText("");
            del_MemberIdHint.setText(" ");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { }
            showDbWarning(friendlyOraMessage(e.getErrorCode(), e.getMessage()));
        }
    }

    // ─── View table ──────────────────────────────────────────────────────────

    private void loadViewTable() {
        viewTableModel.setRowCount(0);
        if (conn == null) return;
        String sql =
            "SELECT MEMBER_ID, BRANCH_ID, MEMBER_NAME, GENDER, AGE, BODY_TYPE, WEIGHT, " +
            "       DATE_OF_JOIN, ADDRESS_CITY, ADDRESS_STREET, ADDRESS_PINCODE, MAIL " +
            "FROM MEMBER ORDER BY MEMBER_ID";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                viewTableModel.addRow(new Object[]{
                    rs.getInt("MEMBER_ID"),
                    rs.getInt("BRANCH_ID"),
                    rs.getString("MEMBER_NAME"),
                    rs.getString("GENDER"),
                    rs.getInt("AGE"),
                    rs.getString("BODY_TYPE"),
                    rs.getDouble("WEIGHT"),
                    rs.getDate("DATE_OF_JOIN"),
                    rs.getString("ADDRESS_CITY"),
                    rs.getString("ADDRESS_STREET"),
                    rs.getString("ADDRESS_PINCODE"),
                    rs.getString("MAIL")
                });
                count++;
            }
            setStatus("Loaded " + count + " member(s) — showing all records.");
        } catch (SQLException e) {
            setStatus("Could not load members.");
            showDbWarning(friendlyOraMessage(e.getErrorCode(), e.getMessage()));
        }
    }

    // ─── Console print ────────────────────────────────────────────────────────

    private void printMemberToConsole(int memberId) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT MEMBER_ID, BRANCH_ID, MEMBER_NAME, GENDER, AGE, BODY_TYPE, WEIGHT, " +
                "       DATE_OF_JOIN, ADDRESS_CITY, ADDRESS_STREET, ADDRESS_PINCODE, MAIL " +
                "FROM MEMBER WHERE MEMBER_ID=?");
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String line = "=".repeat(60);
                System.out.println("\n" + line);
                System.out.println("       GYM MANAGEMENT SYSTEM \u2013 MEMBER DETAIL");
                System.out.println(line);
                System.out.printf("  %-22s : %d%n",   "Member ID",      rs.getInt("MEMBER_ID"));
                System.out.printf("  %-22s : %d%n",   "Branch ID",      rs.getInt("BRANCH_ID"));
                System.out.printf("  %-22s : %s%n",   "Member Name",    rs.getString("MEMBER_NAME"));
                System.out.printf("  %-22s : %s%n",   "Gender",         rs.getString("GENDER"));
                System.out.printf("  %-22s : %d%n",   "Age",            rs.getInt("AGE"));
                System.out.printf("  %-22s : %s%n",   "Body Type",      rs.getString("BODY_TYPE"));
                System.out.printf("  %-22s : %.2f%n", "Weight (kg)",    rs.getDouble("WEIGHT"));
                System.out.printf("  %-22s : %s%n",   "Date of Join",   rs.getDate("DATE_OF_JOIN"));
                System.out.printf("  %-22s : %s%n",   "Address City",   rs.getString("ADDRESS_CITY"));
                System.out.printf("  %-22s : %s%n",   "Address Street", rs.getString("ADDRESS_STREET"));
                System.out.printf("  %-22s : %s%n",   "Pincode",        rs.getString("ADDRESS_PINCODE"));
                System.out.printf("  %-22s : %s%n",   "Email",          rs.getString("MAIL"));
                System.out.printf("  %-22s : %s%n",   "Printed At",
                    new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                System.out.println(line + "\n");
                setStatus("Member #" + memberId + " printed to console.");
            } else {
                System.out.println("[PRINT] No record found for Member #" + memberId);
            }
        } catch (SQLException e) {
            System.out.println("[PRINT] Error: " + e.getMessage());
        }
    }

    // ─── Console CRUD — all use while(true) so any error restarts from top ───

    private void consoleInsert() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n" + "─".repeat(50));
                System.out.println("  CONSOLE — INSERT MEMBER");
                System.out.println("─".repeat(50));

                // Member ID
                System.out.print("Member ID                              : ");
                String memberIdRaw = sc.nextLine().trim();
                String err = validateMemberId(memberIdRaw);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }
                int memberId = Integer.parseInt(memberIdRaw);

                // Member Name
                System.out.print("Member Name                            : ");
                String name = sc.nextLine().trim();
                err = validateVarchar(name, "Member Name", 50);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // Age
                System.out.print("Age                                    : ");
                String ageRaw = sc.nextLine().trim();
                err = validateAge(ageRaw);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }
                int age = Integer.parseInt(ageRaw);

                // Gender
                System.out.print("Gender (Male / Female / Other)         : ");
                String gender = sc.nextLine().trim();
                err = validateGender(gender);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // Body Type
                System.out.print("Body Type (Mesoderm / Endoderm / Ectoderm) : ");
                String bodyType = sc.nextLine().trim();
                err = validateBodyType(bodyType);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // Weight
                System.out.print("Weight (kg)                            : ");
                String weightRaw = sc.nextLine().trim();
                err = validateWeight(weightRaw);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }
                double weight = Double.parseDouble(weightRaw);

                // Address City
                System.out.print("Address City                           : ");
                String city = sc.nextLine().trim();
                err = validateVarchar(city, "Address City", 30);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // Address Street
                System.out.print("Address Street                         : ");
                String street = sc.nextLine().trim();
                err = validateVarchar(street, "Address Street", 30);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // Pincode
                System.out.print("Pincode (digits only)                  : ");
                String pincode = sc.nextLine().trim();
                err = validatePincode(pincode);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // Branch ID
                System.out.print("Branch ID                              : ");
                String branchIdRaw = sc.nextLine().trim();
                err = validateBranchId(branchIdRaw);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }
                int branchId = Integer.parseInt(branchIdRaw);

                // Email
                System.out.print("Email                                  : ");
                String mail = sc.nextLine().trim();
                err = validateEmail(mail);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // All validations passed — attempt DB insert
                try {
                    conn.setAutoCommit(false);
                    PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO MEMBER " +
                        "(MEMBER_ID, BRANCH_ID, MEMBER_NAME, GENDER, AGE, BODY_TYPE, WEIGHT, DATE_OF_JOIN, " +
                        " ADDRESS_CITY, ADDRESS_STREET, ADDRESS_PINCODE, MAIL) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?)");
                    ps.setInt   (1,  memberId);
                    ps.setInt   (2,  branchId);
                    ps.setString(3,  name);
                    ps.setString(4,  gender);
                    ps.setInt   (5,  age);
                    ps.setString(6,  bodyType);
                    ps.setDouble(7,  weight);
                    ps.setString(8,  city);
                    ps.setString(9,  street);
                    ps.setString(10, pincode);
                    ps.setString(11, mail.toLowerCase());
                    ps.executeUpdate();
                    conn.commit();
                    System.out.println("[INSERT] Member #" + memberId + " inserted successfully.");
                    printMemberToConsole(memberId);
                    SwingUtilities.invokeLater(() ->
                        setStatus("Console insert done \u2013 Member #" + memberId));
                    break; // success — exit the loop
                } catch (SQLException e) {
                    try { conn.rollback(); } catch (SQLException ex) { }
                    String msg = friendlyOraMessage(e.getErrorCode(), e.getMessage());
                    System.out.println("[INSERT ERROR] " + msg);
                    System.out.println("[INFO] Please re-enter all details from the beginning.");
                    // loop continues — restarts from top
                }
            }
        }).start();
    }

    private void consoleUpdate() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n" + "─".repeat(50));
                System.out.println("  CONSOLE — UPDATE MEMBER");
                System.out.println("  (Leave a field blank to skip updating it)");

                // Member ID
                System.out.print("Member ID              : ");
                String memberIdRaw = sc.nextLine().trim();
                String err = validateMemberId(memberIdRaw);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }
                int memberId = Integer.parseInt(memberIdRaw);

                // Weight (optional)
                System.out.print("New Weight (blank=skip): ");
                String weight = sc.nextLine().trim();
                err = validateWeightOptional(weight);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // City (optional)
                System.out.print("New City   (blank=skip): ");
                String city = sc.nextLine().trim();
                if (!city.isEmpty()) {
                    err = validateVarchar(city, "Address City", 30);
                    if (err != null) { System.out.println("[ERROR] " + err); continue; }
                }

                // Street (optional)
                System.out.print("New Street (blank=skip): ");
                String street = sc.nextLine().trim();
                if (!street.isEmpty()) {
                    err = validateVarchar(street, "Address Street", 30);
                    if (err != null) { System.out.println("[ERROR] " + err); continue; }
                }

                // Pincode (optional)
                System.out.print("New Pincode(blank=skip): ");
                String pincode = sc.nextLine().trim();
                err = validatePincodeOptional(pincode);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // Email (optional)
                System.out.print("New Email  (blank=skip): ");
                String mail = sc.nextLine().trim();
                err = validateEmailOptional(mail);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }

                // Check at least one field is being updated
                if (weight.isEmpty() && city.isEmpty() && street.isEmpty()
                        && pincode.isEmpty() && mail.isEmpty()) {
                    System.out.println("[UPDATE] No fields to update. Please enter at least one field.");
                    continue; // restart — ask again
                }

                // Attempt DB update
                try {
                    conn.setAutoCommit(false);
                    StringBuilder sb = new StringBuilder("UPDATE MEMBER SET MEMBER_ID=MEMBER_ID");
                    if (!weight.isEmpty())  sb.append(", WEIGHT=").append(weight);
                    if (!city.isEmpty())    sb.append(", ADDRESS_CITY='").append(city).append("'");
                    if (!street.isEmpty())  sb.append(", ADDRESS_STREET='").append(street).append("'");
                    if (!pincode.isEmpty()) sb.append(", ADDRESS_PINCODE='").append(pincode).append("'");
                    if (!mail.isEmpty())    sb.append(", MAIL='").append(mail.toLowerCase()).append("'");
                    sb.append(" WHERE MEMBER_ID=?");
                    PreparedStatement ps = conn.prepareStatement(sb.toString());
                    ps.setInt(1, memberId);
                    int rows = ps.executeUpdate();
                    if (rows == 0) {
                        conn.rollback();
                        System.out.println("[UPDATE] No member found with ID: " + memberId
                            + ". Please re-enter all details.");
                        continue; // restart — member not found
                    }
                    conn.commit();
                    System.out.println("[UPDATE] Member #" + memberId + " updated successfully.");
                    printMemberToConsole(memberId);
                    SwingUtilities.invokeLater(() ->
                        setStatus("Console update done \u2013 Member #" + memberId));
                    break; // success — exit the loop
                } catch (SQLException e) {
                    try { conn.rollback(); } catch (SQLException ex) { }
                    String msg = friendlyOraMessage(e.getErrorCode(), e.getMessage());
                    System.out.println("[UPDATE ERROR] " + msg);
                    System.out.println("[INFO] Please re-enter all details from the beginning.");
                    // loop continues — restarts from top
                }
            }
        }).start();
    }

    private void consoleDelete() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n" + "─".repeat(50));
                System.out.println("  CONSOLE — DELETE MEMBER");
                System.out.println("─".repeat(50));
                System.out.println("  WARNING: This permanently deletes the member.");
                System.out.println("  If linked records exist, remove them first.");

                // Member ID
                System.out.print("Member ID            : ");
                String memberIdRaw = sc.nextLine().trim();
                String err = validateMemberId(memberIdRaw);
                if (err != null) { System.out.println("[ERROR] " + err); continue; }
                int memberId = Integer.parseInt(memberIdRaw);

                // Confirmation
                System.out.print("Confirm delete? (yes / no) : ");
                String confirm = sc.nextLine().trim();
                if (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("y")) {
                    System.out.println("[DELETE] Cancelled.");
                    break; // user chose not to delete — exit cleanly
                }

                // Attempt DB delete
                try {
                    conn.setAutoCommit(false);
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM MEMBER WHERE MEMBER_ID=?");
                    ps.setInt(1, memberId);
                    int rows = ps.executeUpdate();
                    if (rows == 0) {
                        conn.rollback();
                        System.out.println("[DELETE] No member found with ID: " + memberId
                            + ". Please re-enter the Member ID.");
                        continue; // restart — member not found
                    }
                    conn.commit();
                    System.out.println("[DELETE] Member #" + memberId + " deleted successfully.");
                    SwingUtilities.invokeLater(() ->
                        setStatus("Console delete done \u2013 Member #" + memberId));
                    break; // success — exit the loop
                } catch (SQLException e) {
                    try { conn.rollback(); } catch (SQLException ex) { }
                    String msg = friendlyOraMessage(e.getErrorCode(), e.getMessage());
                    System.out.println("[DELETE ERROR] " + msg);
                    System.out.println("[INFO] Please re-enter the Member ID and try again.");
                    // loop continues — restarts from top
                }
            }
        }).start();
    }

    private void consoleDisplay() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n" + "─".repeat(50));
                System.out.println("  CONSOLE — DISPLAY MEMBER");
                System.out.println("─".repeat(50));

                System.out.print("Member ID : ");
                String memberIdRaw = sc.nextLine().trim();
                String err = validateMemberId(memberIdRaw);
                if (err != null) {
                    System.out.println("[ERROR] " + err);
                    continue; // restart — bad ID
                }
                int memberId = Integer.parseInt(memberIdRaw);
                printMemberToConsole(memberId);
                break; // done
            }
        }).start();
    }

    // ─── Layout helpers ───────────────────────────────────────────────────────

    private void addLabel(JPanel panel, GridBagConstraints gc,
                          String text, int col, int row) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        gc.gridx = col; gc.gridy = row; gc.gridwidth = 1;
        panel.add(label, gc);
    }

    // ─── Clear form helpers ───────────────────────────────────────────────────

    private void clearInsertForm() {
        ins_MemberId.setText("");     ins_MemberIdHint.setText(" ");
        ins_Name.setText("");         ins_NameHint.setText(" ");
        ins_Age.setText("");          ins_AgeHint.setText(" ");
        ins_Weight.setText("");       ins_WeightHint.setText(" ");
        ins_BranchId.setText("");     ins_BranchIdHint.setText(" ");
        ins_AddrCity.setText("");     ins_AddrCityHint.setText(" ");
        ins_AddrStreet.setText("");   ins_AddrStreetHint.setText(" ");
        ins_AddrPincode.setText("");  ins_AddrPincodeHint.setText(" ");
        ins_Mail.setText("");         ins_MailHint.setText(" ");
        ins_GenderMale.setSelected(true);
        ins_BodyMeso.setSelected(true);
    }

    private void clearUpdateForm() {
        upd_MemberId.setText("");     upd_MemberIdHint.setText(" ");
        upd_Weight.setText("");       upd_WeightHint.setText(" ");
        upd_AddrCity.setText("");     upd_AddrCityHint.setText(" ");
        upd_AddrStreet.setText("");   upd_AddrStreetHint.setText(" ");
        upd_AddrPincode.setText("");  upd_AddrPincodeHint.setText(" ");
        upd_Mail.setText("");         upd_MailHint.setText(" ");
    }

    // ─── QuickDocListener ─────────────────────────────────────────────────────

    private abstract static class QuickDocListener implements DocumentListener {
        public abstract void update();
        public void insertUpdate(DocumentEvent e)  { update(); }
        public void removeUpdate(DocumentEvent e)  { update(); }
        public void changedUpdate(DocumentEvent e) { update(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GymMemberForm());
    }
}
