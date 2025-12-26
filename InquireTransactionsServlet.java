import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CSCI6810/InquireTransactions")
public class InquireTransactionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/InquireTransactions.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("UserID");
        if (userId == null || userId.isEmpty()) {
            Object s = req.getSession(false) != null ? req.getSession(false).getAttribute("Username") : null;
            if (s != null) userId = s.toString();
        }
        String start = req.getParameter("StartDate");
        String end = req.getParameter("EndDate");
        String message = "";
        java.util.List<java.util.List<String>> rows = new java.util.ArrayList<>();
        try {
            com.aditya.Transaction t = new com.aditya.Transaction(start, end);
            java.util.List<java.util.List<String>> res = t.searchTransaction(userId);
            if (res != null) {
                rows.addAll(res);
            }
        } catch (Exception e) {
            log("Inquiry error", e);
            message = "Error fetching transactions: " + e.getMessage();
        }
        req.setAttribute("transactions", rows);
        req.setAttribute("message", message);
        req.getRequestDispatcher("/InquireTransactions.jsp").forward(req, resp);
    }
}
