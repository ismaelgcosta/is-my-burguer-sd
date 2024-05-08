package br.com.ismyburguer;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServerConfiguration implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("org.eclipse.jetty.servlet.SessionIdPathParameterName", "none");
    }
    
}