import org.junit.jupiter.api.Test;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class WithdrawServletTest {
    @Test
    public void invalidAmountShowsError() throws Exception {
        WithdrawServlet servlet = new WithdrawServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("Username")).thenReturn("testuser");
        when(req.getParameter("AmountField")).thenReturn("abc");
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher("/Withdraw.jsp")).thenReturn(rd);

        servlet.doPost(req, resp);

        verify(req).setAttribute(eq("message"), contains("Invalid amount"));
        verify(rd).forward(req, resp);
    }
}
