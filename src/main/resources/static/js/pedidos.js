/**
 * Carrega a lista de pedidos recentes (status 'RECEBIDO') no dropdown de recentes.
 */
function carregarPedidosRecentes(page = 0) {
    const dropdownBody = document.getElementById('pedidos-dropdown-body');
    fetch(`/cliente/meus-pedidos/recentes?page=${page}`)
        .then(response => response.ok ? response.text() : Promise.reject('Erro'))
        .then(html => { dropdownBody.innerHTML = html; })
        .catch(error => console.error("Erro ao carregar pedidos recentes:", error));
}


function verDetalhesPedido(pedidoId, origem) {
    // Determina em qual dropdown os detalhes devem ser exibidos
    const targetDivId = origem === 'historico' ? 'historico-dropdown-body' : 'pedidos-dropdown-body';
    const dropdownBody = document.getElementById(targetDivId);

    fetch(`/cliente/meus-pedidos/${pedidoId}`)
        .then(response => response.ok ? response.text() : Promise.reject('Erro'))
        .then(html => { dropdownBody.innerHTML = html; })
        .catch(error => console.error("Erro ao carregar detalhes:", error));
}





function mostrarDetalhesHistorico(pedidoId) {
    const container = document.getElementById('detalhes-pedido-container');
    if (!container) return;

    // Faz a requisição para buscar o HTML dos detalhes
    fetch(`/cliente/meus-pedidos/detalhe-fragmento/${pedidoId}`)
        .then(response => response.text())
        .then(html => {
            container.innerHTML = html; // Injeta o fragmento no container
            container.scrollIntoView({ behavior: 'smooth', block: 'center' }); // Rola a tela para o painel
        })
        .catch(error => console.error("Erro ao carregar detalhes:", error));
}


function fecharDetalhesHistorico() {
    const container = document.getElementById('detalhes-pedido-container');
    if (container) {
        container.innerHTML = ''; // Limpa o conteúdo
    }
}