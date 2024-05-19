package br.com.ismyburguer.steps;

import br.com.ismyburguer.Routes;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class RoutesSteps {

    private Routes rotas;
    private RouterFunction<ServerResponse> rota;

    @Given("que eu tenho uma instância de Routes")
    public void que_eu_tenho_uma_instancia_de_Routes() {
        rotas = new Routes();
    }

    @When("eu chamo a rota de autenticação")
    public void eu_chamo_a_rota_de_autenticacao() {
        rota = rotas.auth();
    }

    @When("eu chamo a rota de clientes")
    public void eu_chamo_a_rota_de_clientes() {
        rota = rotas.clientes();
    }

    @When("eu chamo a rota de usuários")
    public void eu_chamo_a_rota_de_usuarios() {
        rota = rotas.users();
    }

    @When("eu chamo a rota de pedidos")
    public void eu_chamo_a_rota_de_pedidos() {
        rota = rotas.pedidos();
    }

    @When("eu chamo a rota de produtos")
    public void eu_chamo_a_rota_de_produtos() {
        rota = rotas.produtos();
    }

    @When("eu chamo a rota de pagamentos")
    public void eu_chamo_a_rota_de_pagamentos() {
        rota = rotas.pagamentos();
    }

    @When("eu chamo a rota de controle de pedidos")
    public void eu_chamo_a_rota_de_controle_de_pedidos() {
        rota = rotas.controlePedidos();
    }

    @Then("a rota de autenticação não deve ser nula")
    public void a_rota_de_autenticacao_nao_deve_ser_nula() {
        assertThat(rota).isNotNull();
    }

    @Then("a rota de clientes não deve ser nula")
    public void a_rota_de_clientes_nao_deve_ser_nula() {
        assertThat(rota).isNotNull();
    }

    @Then("a rota de usuários não deve ser nula")
    public void a_rota_de_usuarios_nao_deve_ser_nula() {
        assertThat(rota).isNotNull();
    }

    @Then("a rota de pedidos não deve ser nula")
    public void a_rota_de_pedidos_nao_deve_ser_nula() {
        assertThat(rota).isNotNull();
    }

    @Then("a rota de produtos não deve ser nula")
    public void a_rota_de_produtos_nao_deve_ser_nula() {
        assertThat(rota).isNotNull();
    }

    @Then("a rota de pagamentos não deve ser nula")
    public void a_rota_de_pagamentos_nao_deve_ser_nula() {
        assertThat(rota).isNotNull();
    }

    @Then("a rota de controle de pedidos não deve ser nula")
    public void a_rota_de_controle_de_pedidos_nao_deve_ser_nula() {
        assertThat(rota).isNotNull();
    }

    @Then("a rota de autenticação deve corresponder ao esperado")
    public void a_rota_de_autenticacao_deve_corresponder_ao_esperado() {
        assertThat(rota).hasToString(rotas.auth().toString());
    }

    @Then("a rota de clientes deve corresponder ao esperado")
    public void a_rota_de_clientes_deve_corresponder_ao_esperado() {
        assertThat(rota).hasToString(rotas.clientes().toString());
    }

    @Then("a rota de usuários deve corresponder ao esperado")
    public void a_rota_de_usuarios_deve_corresponder_ao_esperado() {
        assertThat(rota).hasToString(rotas.users().toString());
    }

    @Then("a rota de pedidos deve corresponder ao esperado")
    public void a_rota_de_pedidos_deve_corresponder_ao_esperado() {
        assertThat(rota).hasToString(rotas.pedidos().toString());
    }

    @Then("a rota de produtos deve corresponder ao esperado")
    public void a_rota_de_produtos_deve_corresponder_ao_esperado() {
        assertThat(rota).hasToString(rotas.produtos().toString());
    }

    @Then("a rota de pagamentos deve corresponder ao esperado")
    public void a_rota_de_pagamentos_deve_corresponder_ao_esperado() {
        assertThat(rota).hasToString(rotas.pagamentos().toString());
    }

    @Then("a rota de controle de pedidos deve corresponder ao esperado")
    public void a_rota_de_controle_de_pedidos_deve_corresponder_ao_esperado() {
        assertThat(rota).hasToString(rotas.controlePedidos().toString());
    }
}
