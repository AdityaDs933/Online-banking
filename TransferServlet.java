import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CSCI6810/Transfer")
public class TransferServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("UserID");
        if (userId == null || userId.isEmpty()) {
            Object s = req.getSession(false) != null ? req.getSession(false).getAttribute("Username") : null;
            if (s != null) userId = s.toString();
        }
        String from = req.getParameter("FromCheckingOrSavings");
        String to = req.getParameter("ToCheckingOrSavings");
        String amountStr = req.getParameter("AmountField");
        String message = "";
        if (!com.aditya.InputValidator.isValidAmount(amountStr)) {
            message = "Invalid amount.";
        } else if (userId == null || userId.isEmpty()) {
            message = "User not identified.";
        } else if (from == null || to == null || from.equalsIgnoreCase(to)) {
            message = "Source and destination must differ.";
        } else {
            try {
                float amount = Float.parseFloat(amountStr);
                com.aditya.AccountService svc = new com.aditya.AccountServiceImpl();
                boolean ok = svc.transfer(from, null, to, null, userId, amount);
                message = ok ? "Transfer successful." : "Transfer failed.";
            } catch (Exception e) {
                log("Transfer error", e);
                message = "Transfer error: " + e.getMessage();
            }
        }
        req.setAttribute("message", message);
        req.getRequestDispatcher("/Transfer.jsp").forward(req, resp);
    }
}
