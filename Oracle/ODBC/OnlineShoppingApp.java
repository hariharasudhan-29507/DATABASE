package hackerrank;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class OnlineShoppingApp {

    static final String URL  = "jdbc:oracle:thin:@localhost:1521/xepdb1";
    static final String USER = "system";
    static final String PASS = "system123";

    static Connection conn;
    static int loggedInCustomerId = -1;
    static int currentCartId      = -1;

    static JFrame     frame;
    static JPanel     cards;
    static CardLayout cl;

    static final String CARD_LOGIN         = "LOGIN";
    static final String CARD_REGISTER      = "REGISTER";
    static final String CARD_PRODUCTS      = "PRODUCTS";
    static final String CARD_CART          = "CART";
    static final String CARD_CHECKOUT      = "CHECKOUT";
    static final String CARD_ORDERS        = "ORDERS";
    static final String CARD_ADMIN_LOGIN   = "ADMIN_LOGIN";
    static final String CARD_ADMIN_DASH    = "ADMIN_DASH";
    static final String CARD_ADMIN_ORDERS  = "ADMIN_ORDERS";

    static JTable tblProducts, tblCart, tblOrders, tblAdminProd, tblAdminOrders;
    static JLabel lblCartTotal = new JLabel("Total:  ₹0.00");
    static JLabel lblOrderInfo = new JLabel(" ");

    static double checkoutTotal = 0;

    static JTextField tfProdName, tfProdCat, tfProdPrice, tfProdStock, tfProdDesc;
    static JLabel     lblEditMode  = new JLabel("Mode: Add New Product");
    static int        editingProdId = -1;

    static JTextArea  txaSummary   = new JTextArea(8, 46);
    static JTextField tfDelivAddr  = new JTextField(30);
    static JComboBox<String> cmbPay =
            new JComboBox<>(new String[]{"Cash on Delivery", "UPI", "Card"});

    public static void main(String[] args) throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection(URL, USER, PASS);

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Online Shopping Application");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(860, 580);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            cl    = new CardLayout();
            cards = new JPanel(cl);
            cards.add(buildLoginPanel(),       CARD_LOGIN);
            cards.add(buildRegisterPanel(),    CARD_REGISTER);
            cards.add(buildProductsPanel(),    CARD_PRODUCTS);
            cards.add(buildCartPanel(),        CARD_CART);
            cards.add(buildCheckoutPanel(),    CARD_CHECKOUT);
            cards.add(buildOrdersPanel(),      CARD_ORDERS);
            cards.add(buildAdminLoginPanel(),  CARD_ADMIN_LOGIN);
            cards.add(buildAdminDashPanel(),   CARD_ADMIN_DASH);
            cards.add(buildAdminOrdersPanel(), CARD_ADMIN_ORDERS);

            frame.setContentPane(cards);
            cl.show(cards, CARD_LOGIN);
            frame.setVisible(true);
        });
    }

    static void show(String card) { cl.show(cards, card); }

    static PreparedStatement ps(String sql, Object... v) throws SQLException {
        PreparedStatement s = conn.prepareStatement(sql);
        for (int i = 0; i < v.length; i++) s.setObject(i + 1, v[i]);
        return s;
    }
    static void err(Exception ex) {
        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    static void info(String m) {
        JOptionPane.showMessageDialog(frame, m, "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    static JTable makeTable(String... cols) {
        JTable t = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setReorderingAllowed(false);
        t.setRowHeight(22);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return t;
    }
    static DefaultTableModel mdl(JTable t) { return (DefaultTableModel) t.getModel(); }

    static void row(JPanel p, GridBagConstraints g, int r,
                    String lbl, JComponent field) {
        g.gridy = r;
        g.gridx = 0; g.anchor = GridBagConstraints.EAST;
        g.weightx = 0; g.fill = GridBagConstraints.NONE;
        p.add(new JLabel(lbl), g);
        g.gridx = 1; g.anchor = GridBagConstraints.WEST;
        g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        p.add(field, g);
    }
    static GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        return g;
    }
    
    static JLabel header(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Dialog", Font.BOLD, 15));
        l.setBorder(new EmptyBorder(8, 0, 6, 0));
        return l;
    }

    static JPanel btnBar(JButton... btns) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        for (JButton b : btns) p.add(b);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // VALIDATION METHODS (manual string processing – no regex)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Email must contain exactly one '@', a local part before it, and a domain
     * after it that ends with either ".com" or ".net".
     * Allowed domain hosts (case-insensitive): gmail, yahoo.
     * Manual character-by-character approach – no regex used.
     */
    static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;

        // ── locate '@' ──────────────────────────────────────────────────────
        int atIndex = -1;
        int atCount = 0;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') { atIndex = i; atCount++; }
        }
        if (atCount != 1) return false;          // must have exactly one '@'

        String local  = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        // ── local part checks ────────────────────────────────────────────────
        if (local.isEmpty()) return false;
        for (int i = 0; i < local.length(); i++) {
            char c = local.charAt(i);
            boolean ok = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
                      || (c >= '0' && c <= '9')
                      || c == '.' || c == '_' || c == '%' || c == '+' || c == '-';
            if (!ok) return false;
        }

        // ── domain checks ────────────────────────────────────────────────────
        // accepted full domains
        String domLower = domain.toLowerCase();
        return domLower.equals("gmail.com") || domLower.equals("yahoo.com");
    }

    /**
     * Phone must be exactly 10 digits (all characters must be 0-9).
     * Manual digit-check – no regex used.
     */
    static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        String ph = phone.trim();
        if (ph.length() != 10) return false;
        for (int i = 0; i < ph.length(); i++) {
            char c = ph.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    /**
     * Password rules (manual scan – no regex):
     *   • At least 8 characters
     *   • At least one uppercase letter (A-Z)
     *   • At least one digit (0-9)
     *   • At least one special character from the allowed set
     */
    static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper   = false;
        boolean hasDigit   = false;
        boolean hasSpecial = false;
        String  specials   = "!@#$%^&*()_+-=[]{};\':\"\\|,.<>/?";

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= 'A' && c <= 'Z')           hasUpper   = true;
            else if (c >= '0' && c <= '9')       hasDigit   = true;
            else if (specials.indexOf(c) >= 0)   hasSpecial = true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }

    // ─────────────────────────────────────────────────────────────────────────

    static JPanel buildLoginPanel() {
        JPanel root = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.gridwidth = 2; g.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel(" Online Shopping Application",
                SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 20));
        g.gridx = 0; g.gridy = 0; g.insets = new Insets(28, 10, 12, 10);
        root.add(title, g);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints fc = gbc();
        JTextField  tfEmail = new JTextField(20);
        JPasswordField tfPwd = new JPasswordField(20);
        row(form, fc, 0, "Email :", tfEmail);
        row(form, fc, 1, "Password :", tfPwd);
        g.gridy = 2; g.insets = new Insets(0, 100, 8, 100);
        root.add(form, g);

        JButton btnLogin = new JButton("Login");
        JButton btnReg   = new JButton("Register");
        JButton btnAdmin = new JButton("Admin Login");
        g.gridy = 3; g.insets = new Insets(4, 10, 24, 10);
        root.add(btnBar(btnLogin, btnReg, btnAdmin), g);

        btnLogin.addActionListener(e -> {
            try (ResultSet rs = ps(
                    "SELECT customer_id FROM Customers WHERE email=? AND password=?",
                    tfEmail.getText().trim(),
                    new String(tfPwd.getPassword())).executeQuery()) {
                if (rs.next()) {
                    loggedInCustomerId = rs.getInt(1);
                    createOrGetCart();
                    refreshProducts();
                    show(CARD_PRODUCTS);
                } else info("Invalid email or password.");
            } catch (Exception ex) { err(ex); }
        });
        btnReg.addActionListener(e -> show(CARD_REGISTER));
        btnAdmin.addActionListener(e -> show(CARD_ADMIN_LOGIN));
        return root;
    }

    static JPanel buildRegisterPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 4));
        root.add(header("Customer Registration"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 130, 10, 130));
        GridBagConstraints gc = gbc();

        JTextField    tfName  = new JTextField(22);
        JTextField    tfEmail = new JTextField(22);
        JTextField    tfPhone = new JTextField(22);
        JPasswordField tfPwd  = new JPasswordField(22);
        JTextField    tfAddr  = new JTextField(22);
        row(form, gc, 0, "Full Name :", tfName);
        row(form, gc, 1, "Email :",     tfEmail);
        row(form, gc, 2, "Phone :",     tfPhone);
        row(form, gc, 3, "Password :",  tfPwd);
        row(form, gc, 4, "Address :",   tfAddr);
        root.add(form, BorderLayout.CENTER);

        JButton btnSave = new JButton("Register");
        JButton btnBack = new JButton("Back to Login");
        root.add(btnBar(btnSave, btnBack), BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            String nm = tfName.getText().trim();
            String em = tfEmail.getText().trim();
            String ph = tfPhone.getText().trim();
            String pw = new String(tfPwd.getPassword());
            String ad = tfAddr.getText().trim();

            // ── field presence check ────────────────────────────────────────
            if (nm.isEmpty() || em.isEmpty() || pw.isEmpty()) {
                info("Name, Email and Password are required."); return;
            }

            // ── email validation ────────────────────────────────────────────
            if (!isValidEmail(em)) {
                info("Invalid email address.\n"
                   + "Only gmail.com and yahoo.com addresses are accepted.\n"
                   + "Example: yourname@gmail.com");
                return;
            }

            // ── phone validation ────────────────────────────────────────────
            if (!ph.isEmpty() && !isValidPhone(ph)) {
                info("Invalid phone number.\n"
                   + "Phone must be exactly 10 digits (numbers only).");
                return;
            }

            // ── password validation ─────────────────────────────────────────
            if (!isValidPassword(pw)) {
                info("Weak password.\nPassword must have:\n"
                   + "  • At least 8 characters\n"
                   + "  • At least 1 uppercase letter (A-Z)\n"
                   + "  • At least 1 digit (0-9)\n"
                   + "  • At least 1 special character (e.g. @, #, !, $)");
                return;
            }

            try {
                ps("INSERT INTO Customers(customer_id,name,email,phone,password,address,created_date) "
                   + "VALUES(cust_seq.NEXTVAL,?,?,?,?,?,SYSDATE)",
                   nm, em, ph, pw, ad).executeUpdate();
                info("Registration successful! Please login.");
                tfName.setText(""); tfEmail.setText(""); tfPhone.setText("");
                tfPwd.setText(""); tfAddr.setText("");
                show(CARD_LOGIN);
            } catch (Exception ex) { err(ex); }
        });
        btnBack.addActionListener(e -> show(CARD_LOGIN));
        return root;
    }

    static JPanel buildProductsPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 4));
        root.add(header("Available Products"), BorderLayout.NORTH);

        tblProducts = makeTable("ID", "Product Name", "Category",
                                "Price (₹)", "Stock", "Description");
        root.add(new JScrollPane(tblProducts), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(new EmptyBorder(4, 8, 6, 8));

        JPanel leftRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JTextField tfQty = new JTextField("1", 5);
        JButton btnAdd = new JButton("Add to Cart");
        leftRow.add(new JLabel("Quantity :"));
        leftRow.add(tfQty);
        leftRow.add(btnAdd);
        south.add(leftRow, BorderLayout.WEST);

        JPanel rightRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        JButton btnCart   = new JButton("View Cart");
        JButton btnOrders = new JButton("My Orders");
        JButton btnLogout = new JButton("Logout");
        rightRow.add(btnCart); rightRow.add(btnOrders); rightRow.add(btnLogout);
        south.add(rightRow, BorderLayout.EAST);
        root.add(south, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            int row = tblProducts.getSelectedRow();
            if (row < 0) { info("Select a product first."); return; }
            int pid   = (int) mdl(tblProducts).getValueAt(row, 0);
            int stock = (int) mdl(tblProducts).getValueAt(row, 4);
            int q;
            try { q = Integer.parseInt(tfQty.getText().trim()); }
            catch (NumberFormatException ex) { info("Enter a valid quantity."); return; }
            if (q <= 0) { info("Quantity must be at least 1."); return; }

            // ── FIX: check how many units are already in the cart ──────────
            int alreadyInCart = 0;
            try (ResultSet rs = ps(
                    "SELECT quantity FROM Cart_Items WHERE cart_id=? AND product_id=?",
                    currentCartId, pid).executeQuery()) {
                if (rs.next()) alreadyInCart = rs.getInt(1);
            } catch (Exception ex) { err(ex); return; }

            int totalRequested = alreadyInCart + q;
            if (totalRequested > stock) {
                info("Cannot add " + q + " unit(s).\n"
                   + "Stock available : " + stock + "\n"
                   + "Already in cart : " + alreadyInCart + "\n"
                   + "Maximum you can add : " + (stock - alreadyInCart));
                return;
            }
            // ── end fix ────────────────────────────────────────────────────

            try {
                if (alreadyInCart > 0) {
                    // item already exists in cart – update quantity directly
                    ps("UPDATE Cart_Items SET quantity=? "
                       + "WHERE cart_id=? AND product_id=?",
                       totalRequested, currentCartId, pid).executeUpdate();
                } else {
                    ps("INSERT INTO Cart_Items(cart_item_id,cart_id,product_id,quantity) "
                       + "VALUES(ci_seq.NEXTVAL,?,?,?)",
                       currentCartId, pid, q).executeUpdate();
                }
                info("Added to cart!"); tfQty.setText("1");
            } catch (Exception ex) { err(ex); }
        });
        btnCart.addActionListener(e -> { refreshCart(); show(CARD_CART); });
        btnOrders.addActionListener(e -> { refreshOrders(); show(CARD_ORDERS); });
        btnLogout.addActionListener(e -> {
            loggedInCustomerId = -1; currentCartId = -1; show(CARD_LOGIN);
        });
        return root;
    }

    static void refreshProducts() {
        mdl(tblProducts).setRowCount(0);
        try (ResultSet rs = ps("SELECT product_id,product_name,category,price,"
                + "stock_quantity,description FROM Products "
                + "WHERE stock_quantity>0 ORDER BY product_id").executeQuery()) {
            while (rs.next())
                mdl(tblProducts).addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getDouble(4), rs.getInt(5), rs.getString(6)});
        } catch (Exception ex) { err(ex); }
    }

    static JPanel buildCartPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 4));
        root.add(header("Shopping Cart"), BorderLayout.NORTH);

        tblCart = makeTable("Item ID","Product ID","Name","Unit Price (₹)","Qty","Subtotal (₹)");
        root.add(new JScrollPane(tblCart), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(new EmptyBorder(4, 8, 6, 8));

        lblCartTotal.setFont(new Font("Dialog", Font.BOLD, 13));
        south.add(lblCartTotal, BorderLayout.WEST);

        JPanel actRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        JTextField tfQty    = new JTextField("1", 5);
        JButton btnUpd      = new JButton("Update Qty");
        JButton btnRemove   = new JButton("Remove Item");
        JButton btnCheckout = new JButton("Checkout");
        JButton btnBack     = new JButton("Back");
        actRow.add(new JLabel("New Qty :"));
        actRow.add(tfQty);
        actRow.add(btnUpd); actRow.add(btnRemove);
        actRow.add(btnCheckout); actRow.add(btnBack);
        south.add(actRow, BorderLayout.EAST);
        root.add(south, BorderLayout.SOUTH);

        btnUpd.addActionListener(e -> {
            int row = tblCart.getSelectedRow();
            if (row < 0) { info("Select an item."); return; }
            int cid = (int) mdl(tblCart).getValueAt(row, 0);
            int pid = (int) mdl(tblCart).getValueAt(row, 1);
            try {
                int q = Integer.parseInt(tfQty.getText().trim());
                if (q <= 0) { info("Quantity must be >= 1."); return; }

                // ── FIX: validate updated qty against actual stock ─────────
                int stock = 0;
                try (ResultSet rs = ps(
                        "SELECT stock_quantity FROM Products WHERE product_id=?",
                        pid).executeQuery()) {
                    if (rs.next()) stock = rs.getInt(1);
                }
                if (q > stock) {
                    info("Cannot set quantity to " + q
                       + ".\nOnly " + stock + " unit(s) available in stock.");
                    return;
                }
                // ── end fix ────────────────────────────────────────────────

                ps("UPDATE Cart_Items SET quantity=? WHERE cart_item_id=?", q, cid)
                        .executeUpdate();
                refreshCart();
            } catch (NumberFormatException ex) { info("Enter valid quantity."); }
              catch (Exception ex) { err(ex); }
        });
        btnRemove.addActionListener(e -> {
            int row = tblCart.getSelectedRow();
            if (row < 0) { info("Select an item."); return; }
            int cid = (int) mdl(tblCart).getValueAt(row, 0);
            try { ps("DELETE FROM Cart_Items WHERE cart_item_id=?", cid).executeUpdate();
                refreshCart(); }
            catch (Exception ex) { err(ex); }
        });
        btnCheckout.addActionListener(e -> {
            if (mdl(tblCart).getRowCount() == 0) { info("Cart is empty."); return; }
            prepareCheckout(); show(CARD_CHECKOUT);
        });
        btnBack.addActionListener(e -> show(CARD_PRODUCTS));
        return root;
    }

    static void refreshCart() {
        mdl(tblCart).setRowCount(0);
        double tot = 0;
        try (ResultSet rs = ps(
                "SELECT ci.cart_item_id,p.product_id,p.product_name,p.price,"
                + "ci.quantity,p.price*ci.quantity "
                + "FROM Cart_Items ci JOIN Products p ON ci.product_id=p.product_id "
                + "WHERE ci.cart_id=?", currentCartId).executeQuery()) {
            while (rs.next()) {
                double sub = rs.getDouble(6); tot += sub;
                mdl(tblCart).addRow(new Object[]{
                    rs.getInt(1), rs.getInt(2), rs.getString(3),
                    rs.getDouble(4), rs.getInt(5), sub});
            }
        } catch (Exception ex) { err(ex); }
        lblCartTotal.setText(String.format("Total:  ₹%.2f", tot));
        checkoutTotal = tot;
    }

    static JPanel buildCheckoutPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 4));
        root.add(header("Order Checkout – Review & Confirm"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(new EmptyBorder(8, 40, 8, 40));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 6, 5, 6);
        gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;

        txaSummary.setEditable(false);
        txaSummary.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txaSummary.setBorder(BorderFactory.createEtchedBorder());
        JScrollPane spSum = new JScrollPane(txaSummary);
        spSum.setPreferredSize(new Dimension(560, 160));

        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        center.add(new JLabel("Order Summary:"), gc);
        gc.gridy = 1;
        center.add(spSum, gc);

        gc.gridwidth = 1; gc.gridy = 2; gc.gridx = 0;
        gc.weightx = 0; gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.EAST;
        center.add(new JLabel("Delivery Address :"), gc);
        gc.gridx = 1; gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL; gc.anchor = GridBagConstraints.WEST;
        center.add(tfDelivAddr, gc);

        gc.gridy = 3; gc.gridx = 0; gc.weightx = 0;
        gc.fill = GridBagConstraints.NONE; gc.anchor = GridBagConstraints.EAST;
        center.add(new JLabel("Payment Mode :"), gc);
        gc.gridx = 1; gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL; gc.anchor = GridBagConstraints.WEST;
        center.add(cmbPay, gc);

        root.add(center, BorderLayout.CENTER);

        JButton btnPlace = new JButton("Place Order");
        JButton btnBack  = new JButton("Back to Cart");
        root.add(btnBar(btnPlace, btnBack), BorderLayout.SOUTH);

        btnPlace.addActionListener(e -> {
            if (tfDelivAddr.getText().trim().isEmpty()) {
                info("Enter a delivery address."); return;
            }
            placeOrder();
        });
        btnBack.addActionListener(e -> show(CARD_CART));
        return root;
    }

    static void prepareCheckout() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  %-28s %6s  %12s  %12s%n",
                "Product", "Qty", "Unit Price", "Subtotal"));
        sb.append("  ").append("─".repeat(64)).append("\n");
        for (int r = 0; r < mdl(tblCart).getRowCount(); r++)
            sb.append(String.format("  %-28s %6s  ₹%11.2f  ₹%11.2f%n",
                mdl(tblCart).getValueAt(r, 2),
                mdl(tblCart).getValueAt(r, 4),
                mdl(tblCart).getValueAt(r, 3),
                mdl(tblCart).getValueAt(r, 5)));
        sb.append("  ").append("─".repeat(64)).append("\n");
        sb.append(String.format("  %-28s %6s  %12s  ₹%11.2f%n",
                "ORDER TOTAL", "", "", checkoutTotal));
        txaSummary.setText(sb.toString());
        txaSummary.setCaretPosition(0);
    }

    static void placeOrder() {
        try {
            conn.setAutoCommit(false);

            // ── FIX: re-validate every cart item against live stock ─────────
            for (int r = 0; r < mdl(tblCart).getRowCount(); r++) {
                int    pid      = (int)    mdl(tblCart).getValueAt(r, 1);
                int    qNeeded  = (int)    mdl(tblCart).getValueAt(r, 4);
                String prodName = (String) mdl(tblCart).getValueAt(r, 2);
                int liveStock = 0;
                try (ResultSet rs = ps(
                        "SELECT stock_quantity FROM Products WHERE product_id=?",
                        pid).executeQuery()) {
                    if (rs.next()) liveStock = rs.getInt(1);
                }
                if (qNeeded > liveStock) {
                    conn.setAutoCommit(true);
                    info("Cannot place order.\n\""
                       + prodName + "\" only has " + liveStock
                       + " unit(s) left in stock,\nbut your cart has "
                       + qNeeded + " unit(s).\nPlease update your cart.");
                    return;
                }
            }
            // ── end fix ────────────────────────────────────────────────────

            ps("INSERT INTO Orders(order_id,customer_id,order_date,total_amount,order_status) "
               + "VALUES(ord_seq.NEXTVAL,?,SYSDATE,?,'Pending')",
               loggedInCustomerId, checkoutTotal).executeUpdate();

            int orderId;
            try (ResultSet rs = ps(
                    "SELECT MAX(order_id) FROM Orders WHERE customer_id=?",
                    loggedInCustomerId).executeQuery()) {
                rs.next(); orderId = rs.getInt(1);
            }
            for (int r = 0; r < mdl(tblCart).getRowCount(); r++) {
                int    pid   = (int)    mdl(tblCart).getValueAt(r, 1);
                int    q     = (int)    mdl(tblCart).getValueAt(r, 4);
                double price = (double) mdl(tblCart).getValueAt(r, 3);
                ps("INSERT INTO Order_Items(order_item_id,order_id,product_id,quantity,price) "
                   + "VALUES(oi_seq.NEXTVAL,?,?,?,?)", orderId, pid, q, price).executeUpdate();
                ps("UPDATE Products SET stock_quantity=stock_quantity-? WHERE product_id=?",
                        q, pid).executeUpdate();
            }
            ps("DELETE FROM Cart_Items WHERE cart_id=?", currentCartId).executeUpdate();
            conn.commit(); conn.setAutoCommit(true);
            info("Order placed successfully!\nOrder ID : " + orderId
               + "\nStatus   : Pending\nDeliver to: " + tfDelivAddr.getText().trim());
            tfDelivAddr.setText("");
            refreshProducts();
            show(CARD_PRODUCTS);
        } catch (Exception ex) {
            try { conn.rollback(); conn.setAutoCommit(true); } catch (Exception ignored){}
            err(ex);
        }
    }

    static JPanel buildOrdersPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 4));
        root.add(header("My Order History"), BorderLayout.NORTH);

        tblOrders = makeTable("Order ID", "Date", "Total (₹)", "Status");
        JTable tblItems = makeTable("Product", "Qty", "Unit Price (₹)", "Subtotal (₹)");

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tblOrders), new JScrollPane(tblItems));
        split.setDividerLocation(200);
        split.setBorder(new EmptyBorder(0, 8, 0, 8));
        root.add(split, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(new EmptyBorder(4, 8, 6, 8));
        lblOrderInfo.setFont(new Font("Dialog", Font.ITALIC, 11));
        south.add(lblOrderInfo, BorderLayout.WEST);

        JButton btnView = new JButton("View Order Items");
        JButton btnBack = new JButton("Back");
        south.add(btnBar(btnView, btnBack), BorderLayout.EAST);
        root.add(south, BorderLayout.SOUTH);

        btnView.addActionListener(e -> {
            int row = tblOrders.getSelectedRow();
            if (row < 0) { info("Select an order."); return; }
            int oid = (int) mdl(tblOrders).getValueAt(row, 0);
            lblOrderInfo.setText("Showing items for Order #" + oid
                    + "   Status: " + mdl(tblOrders).getValueAt(row, 3));
            mdl(tblItems).setRowCount(0);
            try (ResultSet rs = ps(
                    "SELECT p.product_name,oi.quantity,oi.price,oi.quantity*oi.price "
                    + "FROM Order_Items oi "
                    + "JOIN Products p ON oi.product_id=p.product_id "
                    + "WHERE oi.order_id=?", oid).executeQuery()) {
                while (rs.next())
                    mdl(tblItems).addRow(new Object[]{
                        rs.getString(1), rs.getInt(2),
                        rs.getDouble(3), rs.getDouble(4)});
            } catch (Exception ex) { err(ex); }
        });
        btnBack.addActionListener(e -> show(CARD_PRODUCTS));
        return root;
    }

    static void refreshOrders() {
        mdl(tblOrders).setRowCount(0);
        lblOrderInfo.setText(" ");
        try (ResultSet rs = ps(
                "SELECT order_id,order_date,total_amount,order_status "
                + "FROM Orders WHERE customer_id=? ORDER BY order_date DESC",
                loggedInCustomerId).executeQuery()) {
            while (rs.next())
                mdl(tblOrders).addRow(new Object[]{
                    rs.getInt(1), rs.getString(2),
                    rs.getDouble(3), rs.getString(4)});
        } catch (Exception ex) { err(ex); }
    }
    
    static JPanel buildAdminLoginPanel() {
        JPanel root = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.gridwidth = 2; g.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Administrator Login", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 18));
        g.gridx = 0; g.gridy = 0; g.insets = new Insets(40, 10, 14, 10);
        root.add(title, g);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Admin Credentials"));
        GridBagConstraints fc = gbc();
        JTextField    tfUser = new JTextField(18);
        JPasswordField tfPwd = new JPasswordField(18);
        row(form, fc, 0, "Username :", tfUser);
        row(form, fc, 1, "Password :", tfPwd);
        g.gridy = 1; g.insets = new Insets(0, 120, 8, 120);
        root.add(form, g);

        JButton btnLogin = new JButton("Login");
        JButton btnBack  = new JButton("Back");
        g.gridy = 2; g.insets = new Insets(6, 10, 30, 10);
        root.add(btnBar(btnLogin, btnBack), g);

        btnLogin.addActionListener(e -> {
            if ("admin".equals(tfUser.getText()) &&
                "admin123".equals(new String(tfPwd.getPassword()))) {
                refreshAdminProducts(); show(CARD_ADMIN_DASH);
            } else info("Invalid admin credentials.");
        });
        btnBack.addActionListener(e -> show(CARD_LOGIN));
        return root;
    }

    static JPanel buildAdminDashPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 4));
        root.add(header("Admin Dashboard – Product Management"), BorderLayout.NORTH);

        // Left: product table
        tblAdminProd = makeTable("ID","Name","Category","Price (₹)","Stock","Description");

        // Right: inline product form
        JPanel formPanel = new JPanel(new BorderLayout(0, 4));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Product Details"));
        formPanel.setPreferredSize(new Dimension(280, 0));

        JPanel innerForm = new JPanel(new GridBagLayout());
        innerForm.setBorder(new EmptyBorder(6, 8, 6, 8));
        GridBagConstraints gc = gbc();
        tfProdName  = new JTextField(16);
        tfProdCat   = new JTextField(16);
        tfProdPrice = new JTextField(16);
        tfProdStock = new JTextField(16);
        tfProdDesc  = new JTextField(16);
        row(innerForm, gc, 0, "Name :",        tfProdName);
        row(innerForm, gc, 1, "Category :",    tfProdCat);
        row(innerForm, gc, 2, "Price (₹) :",   tfProdPrice);
        row(innerForm, gc, 3, "Stock Qty :",   tfProdStock);
        row(innerForm, gc, 4, "Description :", tfProdDesc);

        lblEditMode.setFont(new Font("Dialog", Font.ITALIC, 11));
        lblEditMode.setBorder(new EmptyBorder(4, 8, 2, 4));

        JButton btnSave  = new JButton("Save / Update");
        JButton btnClear = new JButton("Clear Form");
        JButton btnDel   = new JButton("Delete Selected");

        JPanel fBtns = new JPanel(new GridLayout(3, 1, 0, 4));
        fBtns.setBorder(new EmptyBorder(6, 10, 8, 10));
        fBtns.add(btnSave); fBtns.add(btnClear); fBtns.add(btnDel);

        formPanel.add(lblEditMode, BorderLayout.NORTH);
        formPanel.add(innerForm,   BorderLayout.CENTER);
        formPanel.add(fBtns,       BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tblAdminProd), formPanel);
        split.setDividerLocation(520);
        split.setBorder(new EmptyBorder(0, 8, 0, 8));
        root.add(split, BorderLayout.CENTER);

        JButton btnOrders = new JButton("All Orders");
        JButton btnLogout = new JButton("Logout");
        root.add(btnBar(btnOrders, btnLogout), BorderLayout.SOUTH);

        // Auto-fill form when row is selected
        tblAdminProd.getSelectionModel().addListSelectionListener(ev -> {
            if (ev.getValueIsAdjusting()) return;
            int r = tblAdminProd.getSelectedRow();
            if (r < 0) return;
            editingProdId = (int) mdl(tblAdminProd).getValueAt(r, 0);
            tfProdName .setText((String) mdl(tblAdminProd).getValueAt(r, 1));
            tfProdCat  .setText((String) mdl(tblAdminProd).getValueAt(r, 2));
            tfProdPrice.setText(String.valueOf(mdl(tblAdminProd).getValueAt(r, 3)));
            tfProdStock.setText(String.valueOf(mdl(tblAdminProd).getValueAt(r, 4)));
            tfProdDesc .setText((String) mdl(tblAdminProd).getValueAt(r, 5));
            lblEditMode.setText("Mode: Editing Product ID " + editingProdId);
        });

        btnSave.addActionListener(e -> {
            String nm  = tfProdName.getText().trim();
            String cat = tfProdCat.getText().trim();
            String dsc = tfProdDesc.getText().trim();
            if (nm.isEmpty() || cat.isEmpty()) {
                info("Name and Category are required."); return;
            }
            try {
                double pr = Double.parseDouble(tfProdPrice.getText().trim());
                int    st = Integer.parseInt(tfProdStock.getText().trim());
                if (editingProdId < 0) {
                    ps("INSERT INTO Products(product_id,product_name,category,"
                       + "price,stock_quantity,description) VALUES(prod_seq.NEXTVAL,?,?,?,?,?)",
                       nm, cat, pr, st, dsc).executeUpdate();
                    info("Product added successfully.");
                } else {
                    ps("UPDATE Products SET product_name=?,category=?,price=?,"
                       + "stock_quantity=?,description=? WHERE product_id=?",
                       nm, cat, pr, st, dsc, editingProdId).executeUpdate();
                    info("Product updated successfully.");
                }
                clearProdForm(); refreshAdminProducts();
            } catch (NumberFormatException ex) { info("Price and Stock must be numeric."); }
              catch (Exception ex) { err(ex); }
        });

        btnClear.addActionListener(e -> clearProdForm());

        btnDel.addActionListener(e -> {
            int r = tblAdminProd.getSelectedRow();
            if (r < 0) { info("Select a product to delete."); return; }
            int pid = (int) mdl(tblAdminProd).getValueAt(r, 0);
            int c = JOptionPane.showConfirmDialog(frame,
                "Delete Product ID " + pid + "?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            try { ps("DELETE FROM Products WHERE product_id=?", pid).executeUpdate();
                clearProdForm(); refreshAdminProducts(); info("Product deleted.");
            } catch (Exception ex) { err(ex); }
        });

        btnOrders.addActionListener(e -> { refreshAdminOrders(); show(CARD_ADMIN_ORDERS); });
        btnLogout.addActionListener(e -> show(CARD_LOGIN));
        return root;
    }

    static void clearProdForm() {
        editingProdId = -1;
        tfProdName.setText(""); tfProdCat.setText("");
        tfProdPrice.setText(""); tfProdStock.setText(""); tfProdDesc.setText("");
        lblEditMode.setText("Mode: Add New Product");
        tblAdminProd.clearSelection();
    }

    static void refreshAdminProducts() {
        mdl(tblAdminProd).setRowCount(0);
        try (ResultSet rs = ps("SELECT product_id,product_name,category,price,"
                + "stock_quantity,description FROM Products ORDER BY product_id").executeQuery()) {
            while (rs.next())
                mdl(tblAdminProd).addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getDouble(4), rs.getInt(5), rs.getString(6)});
        } catch (Exception ex) { err(ex); }
    }

    static JPanel buildAdminOrdersPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 4));
        root.add(header("Admin – All Customer Orders"), BorderLayout.NORTH);

        tblAdminOrders = makeTable("Order ID","Customer","Date","Total (₹)","Status");
        JTable tblOrdItems = makeTable("Product","Qty","Unit Price (₹)","Subtotal (₹)");

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tblAdminOrders), new JScrollPane(tblOrdItems));
        split.setDividerLocation(220);
        split.setBorder(new EmptyBorder(0, 8, 0, 8));
        root.add(split, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(new EmptyBorder(4, 8, 6, 8));

        JPanel leftRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JComboBox<String> cmbStatus = new JComboBox<>(
                new String[]{"Pending", "Shipped", "Delivered"});
        JButton btnUpdSt  = new JButton("Update Status");
        JButton btnViewIt = new JButton("View Items");
        leftRow.add(new JLabel("Set Status :"));
        leftRow.add(cmbStatus); leftRow.add(btnUpdSt); leftRow.add(btnViewIt);
        south.add(leftRow, BorderLayout.WEST);

        JButton btnBack = new JButton("Back to Dashboard");
        south.add(btnBar(btnBack), BorderLayout.EAST);
        root.add(south, BorderLayout.SOUTH);

        btnViewIt.addActionListener(e -> {
            int row = tblAdminOrders.getSelectedRow();
            if (row < 0) { info("Select an order."); return; }
            int oid = (int) mdl(tblAdminOrders).getValueAt(row, 0);
            mdl(tblOrdItems).setRowCount(0);
            try (ResultSet rs = ps(
                    "SELECT p.product_name,oi.quantity,oi.price,oi.quantity*oi.price "
                    + "FROM Order_Items oi "
                    + "JOIN Products p ON oi.product_id=p.product_id "
                    + "WHERE oi.order_id=?", oid).executeQuery()) {
                while (rs.next())
                    mdl(tblOrdItems).addRow(new Object[]{
                        rs.getString(1), rs.getInt(2),
                        rs.getDouble(3), rs.getDouble(4)});
            } catch (Exception ex) { err(ex); }
        });

        btnUpdSt.addActionListener(e -> {
            int row = tblAdminOrders.getSelectedRow();
            if (row < 0) { info("Select an order."); return; }
            int oid = (int) mdl(tblAdminOrders).getValueAt(row, 0);
            String st = (String) cmbStatus.getSelectedItem();
            try { ps("UPDATE Orders SET order_status=? WHERE order_id=?", st, oid).executeUpdate();
                mdl(tblAdminOrders).setValueAt(st, row, 4);
                info("Status updated to: " + st);
            } catch (Exception ex) { err(ex); }
        });

        btnBack.addActionListener(e -> show(CARD_ADMIN_DASH));
        return root;
    }

    static void refreshAdminOrders() {
        mdl(tblAdminOrders).setRowCount(0);
        try (ResultSet rs = ps(
                "SELECT o.order_id,c.name,o.order_date,o.total_amount,o.order_status "
                + "FROM Orders o JOIN Customers c ON o.customer_id=c.customer_id "
                + "ORDER BY o.order_date DESC").executeQuery()) {
            while (rs.next())
                mdl(tblAdminOrders).addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getDouble(4), rs.getString(5)});
        } catch (Exception ex) { err(ex); }
    }

    static void createOrGetCart() throws SQLException {
        try (ResultSet rs = ps("SELECT cart_id FROM Cart WHERE customer_id=?",
                loggedInCustomerId).executeQuery()) {
            if (rs.next()) { currentCartId = rs.getInt(1); return; }
        }
        ps("INSERT INTO Cart(cart_id,customer_id,created_date) VALUES(cart_seq.NEXTVAL,?,SYSDATE)",
                loggedInCustomerId).executeUpdate();
        try (ResultSet rs = ps("SELECT cart_id FROM Cart WHERE customer_id=?",
                loggedInCustomerId).executeQuery()) {
            if (rs.next()) currentCartId = rs.getInt(1);
        }
    }
}
