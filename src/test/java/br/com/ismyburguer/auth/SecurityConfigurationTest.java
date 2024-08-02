package br.com.ismyburguer.auth;

import br.com.ismyburguer.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigurationTest {

    static final String AUTH0_TOKEN = "token";
    static final String SUB = "sub";
    static final String AUTH0ID = "sms|12345678";

    @InjectMocks
    private SecurityConfiguration securityConfiguration;

    @Mock
    private HttpSecurity httpSecurity;


    private String issuerUri;

    @BeforeEach
    void setUp() {
        securityConfiguration = new SecurityConfiguration();
        // Configuração do valor do atributo
        issuerUri = "http://example.com";
        securityConfiguration.setIssuerUri(issuerUri);
        securityConfiguration.setUserName("admin");
        securityConfiguration.setUserPassword("pass");
    }

    @BeforeAll
    public static void init() {
        mockStatic(JwtDecoders.class);
    }

    @Test
    public void deveConfigurarFiltroDeSeguranca() throws Exception {
        // Arrange
        JwtDecoder jwtDecoder = new TestSecurityConfig().jwtDecoder();

        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.oauth2ResourceServer(any())).thenReturn(httpSecurity);
        when(httpSecurity.formLogin(any())).thenReturn(httpSecurity);
        when(httpSecurity.httpBasic(any())).thenReturn(httpSecurity);

        // Act
        securityConfiguration.filterChain(httpSecurity, jwtDecoder);

        // Assert
        verify(httpSecurity).headers(any());
        verify(httpSecurity).csrf(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).formLogin(any());
        verify(httpSecurity).httpBasic(any());
        verify(httpSecurity).cors(any());
        verify(httpSecurity).oauth2ResourceServer(any());
        verify(httpSecurity).authenticationProvider(any());
    }

    @Test
    void deveCriarJwtDecoderCorretamente() {

        when(JwtDecoders.fromIssuerLocation(any())).thenReturn((JwtDecoder) token -> jwt());

        // Execução do método a ser testado
        JwtDecoder jwtDecoder = securityConfiguration.jwtDecoder();

        // Verificações
        assertNotNull(jwtDecoder);
        assertEquals(JwtDecoders.fromIssuerLocation(issuerUri), jwtDecoder);
    }

    @Test
    void deveCriarpasswordEncoder() {

        when(JwtDecoders.fromIssuerLocation(any())).thenReturn((JwtDecoder) token -> jwt());

        // Execução do método a ser testado
        PasswordEncoder passwordEncoder = securityConfiguration.passwordEncoder();

        // Verificações
        assertNotNull(passwordEncoder);
        DelegatingPasswordEncoder delegatingPasswordEncoder = (DelegatingPasswordEncoder) passwordEncoder;
        Map<String, PasswordEncoder> idToPasswordEncoder = (Map<String, PasswordEncoder>) ReflectionTestUtils.getField(delegatingPasswordEncoder, "idToPasswordEncoder");

        assertInstanceOf(BCryptPasswordEncoder.class, idToPasswordEncoder.get("bcrypt"));
    }

    @Test
    void deveCriarUserDetailsService() {

        when(JwtDecoders.fromIssuerLocation(any())).thenReturn((JwtDecoder) token -> jwt());

        // Execução do método a ser testado
        UserDetailsService userDetailsService = securityConfiguration.userDetailsManager();

        // Verificações
        assertNotNull(userDetailsService);
        assertInstanceOf(UserDetailsService.class, userDetailsService);

        assertNotNull(userDetailsService.loadUserByUsername("admin"));
    }

    public Jwt jwt() {

        // This is a place to add general and maybe custom claims which should be available after parsing token in the live system
        Map<String, Object> claims = Map.of(
                SUB, AUTH0ID
        );

        //This is an object that represents contents of jwt token after parsing
        return new Jwt(
                AUTH0_TOKEN,
                Instant.now(),
                Instant.now().plusSeconds(30),
                Map.of("alg", "none"),
                claims
        );
    }
}
