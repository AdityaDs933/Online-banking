import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CSCI6810/Withdraw")
public class WithdrawServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("UserID");
        if (userId == null || userId.isEmpty()) {
            Object s = req.getSession(false) != null ? req.getSession(false).getAttribute("Username") : null;
            if (s != null) userId = s.toString();
        }
        String from = req.getParameter("CheckingOrSavings");
        String amount = req.getParameter("AmountField");
        String message = "";
        if (!com.aditya.InputValidator.isValidAmount(amount)) {
            message = "Invalid amount.";
        } else if (userId == null || userId.isEmpty()) {
            message = "User not identified.";
        } else {
            try {
                DBConnection db = new DBConnection();
                try (java.sql.Connection conn = db.openConn()) {
                    conn.setAutoCommit(false);
                    boolean ok = false;
                    if ("Checking".equalsIgnoreCase(from)) {
                        com.aditya.CheckingAccount temp = new com.aditya.CheckingAccount();
                        String ca = temp.getAccno(userId);
                        com.aditya.CheckingAccount acct = new com.aditya.CheckingAccount(ca, "", userId, amount);
                        acct.setCheckingAccountNumber(ca);
                        ok = acct.withdrawWithConnection(conn, userId);
                    } else {
                        com.aditya.SavingAccount temp = new com.aditya.SavingAccount();
                        String sa = temp.getAccno(userId);
                        com.aditya.SavingAccount acct = new com.aditya.SavingAccount(sa, "", userId, amount);
                        acct.setSavingAccountNumber(sa);
                        ok = acct.withdrawWithConnection(conn, userId);
                    }
                    if (ok) {
                        String insertSQL = "INSERT INTO Transactions(TransactionNumber, TransactionType, TransactionAmount, TransactionTime, TransactionDate, FromAccount, ToAccount, CustomerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        try (java.sql.PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                            String txId = java.util.UUID.randomUUID().toString();
                            java.time.LocalTime nowTime = java.time.LocalTime.now();
                            java.time.LocalDate nowDate = java.time.LocalDate.now();
                            ps.setString(1, txId);
                            ps.setString(2, "Withdraw");
                            ps.setFloat(3, Float.parseFloat(amount));
                            ps.setString(4, nowTime.toString());
                            ps.setString(5, nowDate.toString());
                            ps.setString(6, from);
                            ps.setString(7, "");
                            ps.setString(8, userId);
                            ps.executeUpdate();
                        }
                        conn.commit();
                        message = "Withdrawal processed.";
                    } else {
                        conn.rollback();
                        message = "Withdrawal failed or insufficient funds.";
                    }
                }
            } catch (Exception e) {
                log("Withdraw error", e);
                message = "Withdrawal error: " + e.getMessage();
            }
        }
        req.setAttribute("message", message);
        req.getRequestDispatcher("/Withdraw.jsp").forward(req, resp);
    }
}
