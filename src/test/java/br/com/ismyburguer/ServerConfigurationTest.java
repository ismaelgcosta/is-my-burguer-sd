package br.com.ismyburguer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockServletContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ServerConfigurationTest {

    @Mock
    ServletContext servletContext;

    @InjectMocks
    ServerConfiguration serverConfiguration;

    @Test
    void onStartup_ShouldSetSessionIdPathParameterNameToNone() throws ServletException {

        // Act
        serverConfiguration.onStartup(servletContext);

        // Assert
        verify(servletContext).setInitParameter("org.eclipse.jetty.servlet.SessionIdPathParameterName", "none");
    }
}
