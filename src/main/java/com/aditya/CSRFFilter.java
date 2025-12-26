package com.aditya;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class CSRFFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String token = req.getParameter("_csrf");
            if (!CSRFTokenUtil.validateToken(session, token)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                return;
            }
        } else {
            // ensure token exists for forms
            String token = CSRFTokenUtil.getOrCreateToken(session);
            req.setAttribute("csrfToken", token);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
