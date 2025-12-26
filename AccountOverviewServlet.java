import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CSCI6810/AccountOverview")
public class AccountOverviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("UserID");
        if (userId == null || userId.isEmpty()) {
            Object sUser = req.getSession(false) != null ? req.getSession(false).getAttribute("Username") : null;
            if (sUser != null) userId = sUser.toString();
        }

        // Default empty attributes
        req.setAttribute("checkingLast4", "");
        req.setAttribute("checkingBalance", "");
        req.setAttribute("savingsLast4", "");
        req.setAttribute("savingsBalance", "");

        if (userId != null && !userId.isEmpty()) {
            try {
                com.aditya.CheckingAccount ca = new com.aditya.CheckingAccount();
                ca.getAccountInfo(userId);
                String caNum = ca.getAccountNumber();
                String caLast4 = caNum != null && caNum.length() >= 4 ? caNum.substring(caNum.length()-4) : caNum;
                req.setAttribute("checkingLast4", caLast4 != null ? caLast4 : "");
                req.setAttribute("checkingBalance", String.valueOf(ca.getBalanceValue()));

                com.aditya.SavingsAccount sa = new com.aditya.SavingsAccount();
                sa.getAccountInfo(userId);
                String saNum = sa.getSavingsAccountNumber();
                String saLast4 = saNum != null && saNum.length() >= 4 ? saNum.substring(saNum.length()-4) : saNum;
                req.setAttribute("savingsLast4", saLast4 != null ? saLast4 : "");
                req.setAttribute("savingsBalance", String.valueOf(sa.getBalanceValue()));
            } catch (Exception e) {
                log("Error preparing account overview", e);
            }
        }

        // forward to JSP view
        req.getRequestDispatcher("/AccountOverview.jsp").forward(req, resp);
    }
}
