package br.com.ismyburguer.auth;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.client.resourceserver.cognito.issuer-uri}")
    @Setter
    private String issuerUri;

    @Value("${spring.security.user.name}")
    @Setter
    private String userName;

    @Value("${spring.security.user.password}")
    @Setter
    private String userPassword;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .authorizeHttpRequests(authz -> {
                    authz
                            .requestMatchers(
                                    "/**")
                            .authenticated();
                    authz
                            .requestMatchers(
                                    "/user/**",
                                    "/produtos/**",
                                    "/clientes/**",
                                    "/pedidos/**",
                                    "/controle-pedidos/**"
                            )
                            .authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .authenticationProvider(new JwtAuthenticationProvider(jwtDecoder))
        ;
        return http.build();
    }

    @Bean
    @Profile(value = {"dev", "production"})
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        var encoders = new HashMap<String, PasswordEncoder>(
                Map.of("bcrypt", new BCryptPasswordEncoder())
        );

        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }

    @Bean
    public UserDetailsService userDetailsManager() {
        UserDetails admin = User.builder()
                .username(userName)
                .password("{bcrypt}" + new BCryptPasswordEncoder().encode(userPassword))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }


}