# Sistema de Delivery de Pizzaria - Spring Boot

Este é um projeto acadêmico de um sistema web completo para o gerenciamento de uma pizzaria delivery, desenvolvido para as disciplians de Engenharia de Software II, Banco de dados II e Programação Para Internet I. A aplicação cobre todo o fluxo, desde o cadastro e login de clientes e funcionários até a finalização de um pedido e seu acompanhamento em tempo real.


## Funcionalidades Principais

O sistema é dividido em diferentes módulos, cada um com funcionalidades específicas para os perfis de usuário.

### Área do Cliente
* **Autenticação:** Cadastro e Login seguros.
* **Cardápio Dinâmico:** Visualização de produtos (pizzas, bebidas, etc.) carregados do banco de dados.
* **Carrinho de Compras Interativo:** Adicionar/remover itens e ajustar quantidades sem recarregar a página.
* **Checkout Completo:** Seleção de endereço de entrega e forma de pagamento para finalizar o pedido.
* **Acompanhamento de Pedidos:** Um menu exibe os pedidos recentes em andamento e um painel de histórico mostra os pedidos já finalizados (entregues ou cancelados).
* **Notificações em Tempo Real:** O cliente recebe alertas visuais na tela sempre que o status do seu pedido é atualizado por um funcionário.

### Área do Funcionário
* **Painel de Gestão (Caixa/Gerente):** Acompanhamento de todos os pedidos em andamento, com a funcionalidade de alterar o status de "Recebido" para "Pronto para Entrega".
* **Painel do Entregador:**
    * Visualização de pedidos prontos para retirada.
    * Funcionalidade para "Iniciar Entrega", associando o pedido ao entregador.
    * Funcionalidade para "Concluir Entrega", finalizando o ciclo do pedido.

### Painel Administrativo (Apenas Gerente)
* **Gestão de Funcionários:** Cadastro e listagem de todos os funcionários.
* **Gestão de Produtos:** Cadastro, listagem e edição de produtos do cardápio, incluindo upload de imagens para um bucket AWS S3.
* **Gestão de Clientes:** Listagem de todos os clientes cadastrados com a opção de ver o histórico de pedidos de cada um.
* **Faturamento:** Geração de relatórios de pedidos filtrados por período e status, com cálculo do faturamento total.

## Tecnologias Utilizadas

* **Backend:**
    * Java 17+
    * Spring Boot 3
    * Spring Security (Autenticação e Autorização)
    * Spring Data JPA & Hibernate (Persistência de Dados)
    * Spring Web (Controladores MVC)
* **Frontend:**
    * Thymeleaf (Server-Side Rendering)
    * Bootstrap 5 (Design Responsivo)
    * JavaScript & Server-Sent Events (SSE) para notificações em tempo real.
    * HTMX (para atualizações parciais da página no painel de gestão).
* **Banco de Dados:**
    * MySQL 8+
* **Cloud:**
    * Amazon S3 (para armazenamento de imagens dos produtos).
* **Build:**
    * Maven

## Padrões de Projeto Aplicados

Conforme solicitado na atividade, dois padrões de projeto de categorias diferentes foram implementados:

1.  **Observer (Comportamental):** Utilizado para notificar o cliente em tempo real sobre mudanças no status do seu pedido. A entidade `Pedido` atua como `Subject`, e um `SseNotificationObserver` atua como `Observer`, enviando um evento para o frontend quando o status é alterado.
2.  **Factory Method (Criacional):** Aplicado na criação de objetos `Produto`. Uma classe `ProdutoFactory` centraliza a lógica de instanciação, permitindo que o sistema seja facilmente estendido para novos tipos de produto no futuro sem alterar o código que solicita a criação.

## Recursos de Banco de Dados

O projeto também explora funcionalidades avançadas de banco de dados:

* **Transactions (`@Transactional`):** Para garantir a atomicidade de operações críticas como a criação de um pedido.
* **Stored Procedures:** Para encapsular regras de negócio no banco, como o cancelamento de um pedido (`sp_cancelar_pedido`).
* **Views (`v_pedidos_completos`):** Para simplificar consultas complexas no painel administrativo, unindo dados de múltiplas tabelas em uma única tabela virtual.
* **Triggers (`trg_log_mudanca_status`):** Para criar um log de auditoria automático de todas as mudanças de status dos pedidos.

## Como Executar o Projeto

1.  **Banco de Dados:**
    * Crie um banco de dados MySQL chamado `pizzaria_db`.
    * Importe o arquivo `pizzaria_db.sql` que está na raiz do projeto para criar todas as tabelas, views, procedures e dados iniciais.

2.  **Configuração da Aplicação:**
    * No arquivo `src/main/resources/application.properties`, configure as credenciais do seu banco de dados (usuário e senha).
    * Se for utilizar a funcionalidade de upload de imagens, configure suas credenciais da AWS e o nome do seu bucket S3 no mesmo arquivo.

3.  **Execução:**
    * Com o Maven configurado na sua IDE, execute a classe principal `PizzariaApplication.java`.
    * A aplicação estará disponível em `http://localhost:8080`.

4.  **Usuários de Teste:**
    * **Cliente:** `gui@gmail.com` / Senha: `14102001`
    * **Caixa/Gerente:** `aninha@gmail.com` / Senha: `14102001`
    * **Entregador:** `jose@gmail.com` / Senha: `14102001`
