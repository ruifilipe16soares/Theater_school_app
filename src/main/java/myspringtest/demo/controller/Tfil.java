package myspringtest.demo.controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(urlPatterns = {"/admin/*", "/register/*", "/student/*", "/professor/*", "/course/*", "/discipline/*", "/registerDiscipline/*", "/registerCourse/*"}) 
public class Tfil implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(Tfil.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Tfil initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String referer = req.getHeader("Referer");
        String serverName = req.getServerName();

        logger.info("Request URI: " + req.getRequestURI());
        logger.info("Referer: " + referer);

        // Verifica se a URL contém /admin e se o referenciador é interno
        if (referer == null || !referer.contains(serverName)) {
            logger.info("Redirecting to /home due to invalid referer");
            res.sendRedirect("/");
            return;
        }

        // Continue o filtro se a URL não for /admin ou se o referenciador for válido
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("Tfil destroyed");
    }
}
