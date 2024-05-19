Feature: Testar Rotas

  Scenario: Testar rota de autenticação
    Given que eu tenho uma instância de Routes
    When eu chamo a rota de autenticação
    Then a rota de autenticação não deve ser nula
    And a rota de autenticação deve corresponder ao esperado

  Scenario: Testar rota de clientes
    Given que eu tenho uma instância de Routes
    When eu chamo a rota de clientes
    Then a rota de clientes não deve ser nula
    And a rota de clientes deve corresponder ao esperado

  Scenario: Testar rota de usuários
    Given que eu tenho uma instância de Routes
    When eu chamo a rota de usuários
    Then a rota de usuários não deve ser nula
    And a rota de usuários deve corresponder ao esperado

  Scenario: Testar rota de pedidos
    Given que eu tenho uma instância de Routes
    When eu chamo a rota de pedidos
    Then a rota de pedidos não deve ser nula
    And a rota de pedidos deve corresponder ao esperado

  Scenario: Testar rota de produtos
    Given que eu tenho uma instância de Routes
    When eu chamo a rota de produtos
    Then a rota de produtos não deve ser nula
    And a rota de produtos deve corresponder ao esperado

  Scenario: Testar rota de pagamentos
    Given que eu tenho uma instância de Routes
    When eu chamo a rota de pagamentos
    Then a rota de pagamentos não deve ser nula
    And a rota de pagamentos deve corresponder ao esperado

  Scenario: Testar rota de controle de pedidos
    Given que eu tenho uma instância de Routes
    When eu chamo a rota de controle de pedidos
    Then a rota de controle de pedidos não deve ser nula
    And a rota de controle de pedidos deve corresponder ao esperado
