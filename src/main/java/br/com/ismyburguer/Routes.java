package br.com.ismyburguer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.https;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> auth() {
        return route(path("/auth/**"), https())
                .filter(lb("IS-MY-BURGUER-AUTH"));
    }

    @Bean
    public RouterFunction<ServerResponse> clientes() {
        return route(path("/clientes/**"), https())
                .filter(lb("IS-MY-BURGUER-AUTH"));
    }

    @Bean
    public RouterFunction<ServerResponse> users() {
        return route(path("/user/**"), https())
                .filter(lb("IS-MY-BURGUER-AUTH"));
    }

    @Bean
    public RouterFunction<ServerResponse> swaggerAuth() {
        return route(path("/swagger-auth"), https(URI.create("/swagger-ui/index.html")))
                .filter(lb("IS-MY-BURGUER-AUTH"));
    }

}
