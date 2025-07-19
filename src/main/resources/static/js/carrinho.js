document.addEventListener('DOMContentLoaded', () => {


    async function atualizarViewCarrinho(url, options = {}) {

        try {
            const response = await fetch(url, options);
            if (!response.ok) throw new Error('Erro na requisição ao servidor.');
            const htmlFragment = await response.text();
            document.getElementById('cart-body').innerHTML = htmlFragment;
        } catch (error) {
            console.error("Erro ao atualizar o carrinho:", error);
            document.getElementById('cart-body').innerHTML = '<p class="text-center text-danger p-3">Erro ao carregar o carrinho.</p>';
        }
    }

    const cartOffcanvas = document.getElementById('cartOffcanvas');
    if (cartOffcanvas) {
        cartOffcanvas.addEventListener('show.bs.offcanvas', () => atualizarViewCarrinho('/carrinho/ver'));
    }

    window.adicionarAoCarrinho = (produtoId) => {
        atualizarViewCarrinho(`/carrinho/adicionar/${produtoId}`, { method: 'POST' });
    };

    window.atualizarQuantidade = (produtoId, quantidade) => {
        const formData = new FormData();
        formData.append('produtoId', produtoId);
        formData.append('quantidade', quantidade);
        const options = {
            method: 'POST',
            body: new URLSearchParams(formData)
        };
        atualizarViewCarrinho('/carrinho/atualizar', options);
    };

    window.irParaCheckout = () => {
        atualizarViewCarrinho('/pedido/checkout');
    };


    // MOSTRA O FORMULÁRIO DE NOVO ENDEREÇO
    window.mostrarFormNovoEndereco = () => {
        fetch('/endereco/novo') // Chama o backend para pegar o HTML do formulário
            .then(response => response.text())
            .then(htmlFragment => {
                // Injeta o formulário no container que criamos
                document.getElementById('novo-endereco-form-container').innerHTML = htmlFragment;
            })
            .catch(error => console.error('Erro ao buscar formulário de endereço:', error));
    };

    // SALVA O NOVO ENDEREÇO
    window.salvarNovoEndereco = () => {
        // Seleciona o container dos nossos inputs
        const formContainer = document.getElementById('form-novo-endereco');

        // Pega o valor de cada input manualmente
        const rua = formContainer.querySelector('input[name="rua"]').value;
        const numero = formContainer.querySelector('input[name="numero"]').value;
        const bairro = formContainer.querySelector('input[name="bairro"]').value;
        const cidade = formContainer.querySelector('input[name="cidade"]').value;
        const cep = formContainer.querySelector('input[name="cep"]').value;
        const complemento = formContainer.querySelector('input[name="complemento"]').value;

        // Monta o corpo da requisição que será enviado para o backend
        const body = new URLSearchParams();
        body.append('rua', rua);
        body.append('numero', numero);
        body.append('bairro', bairro);
        body.append('cidade', cidade);
        body.append('cep', cep);
        body.append('complemento', complemento);

        const options = {
            method: 'POST',
            body: body
        };

        // Envia os dados para o backend e atualiza a view com a resposta
        atualizarViewCarrinho('/endereco/salvar', options);
    };
});