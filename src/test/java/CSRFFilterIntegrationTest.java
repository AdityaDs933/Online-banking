import com.aditya.CSRFFilter;
import com.aditya.CSRFTokenUtil;

import org.junit.jupiter.api.Test;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CSRFFilterIntegrationTest {

    @Test
    void postWithoutCsrfIsForbidden() throws Exception {
        CSRFFilter filter = new CSRFFilter();

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        HttpSession session = mapBackedSession();
        when(req.getSession(true)).thenReturn(session);
        when(req.getMethod()).thenReturn("POST");
        when(req.getParameter("_csrf")).thenReturn(null);

        filter.doFilter(req, res, chain);

        verify(res).sendError(eq(HttpServletResponse.SC_FORBIDDEN), contains("CSRF"));
        verifyNoInteractions(chain);
    }

    @Test
    void postWithValidCsrfAllowsServletToRun() throws Exception {
        CSRFFilter filter = new CSRFFilter();
        TransferServlet servlet = new TransferServlet();

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        HttpSession session = mapBackedSession();
        when(req.getSession(true)).thenReturn(session);
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("Username")).thenReturn("testuser");

        String token = CSRFTokenUtil.getOrCreateToken(session);
        when(req.getMethod()).thenReturn("POST");
        when(req.getParameter("_csrf")).thenReturn(token);

        // Keep servlet on validation-only path (no DB hit)
        when(req.getParameter("UserID")).thenReturn(null);
        when(req.getParameter("AmountField")).thenReturn("bad");
        when(req.getParameter("FromCheckingOrSavings")).thenReturn("Checking");
        when(req.getParameter("ToCheckingOrSavings")).thenReturn("Savings");

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher("/Transfer.jsp")).thenReturn(rd);

        FilterChain chain = (request, response) -> {
            try {
                servlet.doPost((HttpServletRequest) request, (HttpServletResponse) response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        };

        filter.doFilter(req, res, chain);

        verify(res, never()).sendError(anyInt(), anyString());
        verify(req).setAttribute(eq("message"), contains("Invalid amount"));
        verify(rd).forward(req, res);
    }

    private static HttpSession mapBackedSession() {
        Map<String, Object> attrs = new HashMap<>();
        HttpSession session = mock(HttpSession.class);

        when(session.getAttribute(anyString())).thenAnswer(inv -> attrs.get(inv.getArgument(0)));
        doAnswer(inv -> {
            attrs.put(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(session).setAttribute(anyString(), any());

        return session;
    }
}
