package br.com.ismyburguer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools.reconstructURI;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.redirectTo;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.forward;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.https;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class Routes {

    public static final String IS_MY_BURGUER_PEDIDO = "IS-MY-BURGUER-PEDIDO";
    public static final String IS_MY_BURGUER_AUTH = "IS-MY-BURGUER-AUTH";
    public static final String IS_MY_BURGUER_PAGAMENTO = "IS-MY-BURGUER-PAGAMENTO";
    public static final String IS_MY_BURGUER_CONTROLE_PEDIDO = "IS-MY-BURGUER-CONTROLE-PEDIDO";

    @Bean
    public RouterFunction<ServerResponse> auth() {
        return route(path("/auth/**"), https())
                .filter(lb(IS_MY_BURGUER_AUTH));
    }

    @Bean
    public RouterFunction<ServerResponse> clientes() {
        return route(path("/clientes/**"), https())
                .filter(lb(IS_MY_BURGUER_AUTH));
    }

    @Bean
    public RouterFunction<ServerResponse> users() {
        return route(path("/user/**"), https())
                .filter(lb(IS_MY_BURGUER_AUTH));
    }

    @Bean
    public RouterFunction<ServerResponse> pedidos() {
        return route(path("/pedidos/**"), https())
                .filter(lb(IS_MY_BURGUER_PEDIDO));
    }

    @Bean
    public RouterFunction<ServerResponse> produtos() {
        return route(path("/produtos/**"), https())
                .filter(lb(IS_MY_BURGUER_PEDIDO));
    }

    @Bean
    public RouterFunction<ServerResponse> pagamentos() {
        return route(path("/pagamentos/**"), https())
                .filter(lb(IS_MY_BURGUER_PAGAMENTO));
    }

    @Bean
    public RouterFunction<ServerResponse> controlePedidos() {
        return route(path("/controle-pedidos/**"), https())
                .filter(lb(IS_MY_BURGUER_CONTROLE_PEDIDO));
    }

}
