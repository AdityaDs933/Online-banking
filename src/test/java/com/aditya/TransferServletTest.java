import org.junit.jupiter.api.Test;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class TransferServletTest {
    @Test
    public void invalidAmountShowsError() throws Exception {
        TransferServlet servlet = new TransferServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("Username")).thenReturn("testuser");
        when(req.getParameter("AmountField")).thenReturn("bad");
        when(req.getParameter("FromCheckingOrSavings")).thenReturn("Checking");
        when(req.getParameter("ToCheckingOrSavings")).thenReturn("Savings");
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher("/Transfer.jsp")).thenReturn(rd);

        servlet.doPost(req, resp);

        verify(req).setAttribute(eq("message"), contains("Invalid amount"));
        verify(rd).forward(req, resp);
    }
}
