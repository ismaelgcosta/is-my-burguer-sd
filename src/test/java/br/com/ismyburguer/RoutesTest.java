package br.com.ismyburguer;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoutesTest {

    @Test
    public void testAutenticacao() {
        Routes rotas = new Routes();
        RouterFunction<ServerResponse> autenticacao = rotas.auth();
        assertThat(autenticacao).isNotNull();
    }

    @Test
    public void testClientes() {
        Routes rotas = new Routes();
        RouterFunction<ServerResponse> clientes = rotas.clientes();
        assertThat(clientes).isNotNull();
    }

    @Test
    public void testUsuarios() {
        Routes rotas = new Routes();
        RouterFunction<ServerResponse> usuarios = rotas.users();
        assertThat(usuarios).isNotNull();
    }

    @Test
    public void testPedidos() {
        Routes rotas = new Routes();
        RouterFunction<ServerResponse> pedidos = rotas.pedidos();
        assertThat(pedidos).isNotNull();
    }

    @Test
    public void testProdutos() {
        Routes rotas = new Routes();
        RouterFunction<ServerResponse> produtos = rotas.produtos();
        assertThat(produtos).isNotNull();
    }

    @Test
    public void testPagamentos() {
        Routes rotas = new Routes();
        RouterFunction<ServerResponse> pagamentos = rotas.pagamentos();
        assertThat(pagamentos).isNotNull();
    }

    @Test
    public void testControleDePedidos() {
        Routes rotas = new Routes();
        RouterFunction<ServerResponse> controleDePedidos = rotas.controlePedidos();
        assertThat(controleDePedidos).isNotNull();
    }
}
