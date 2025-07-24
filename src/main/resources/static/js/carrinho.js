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



    window.mostrarFormNovoEndereco = () => {
        fetch('/endereco/novo')
            .then(response => response.text())
            .then(htmlFragment => {

                document.getElementById('novo-endereco-form-container').innerHTML = htmlFragment;
            })
            .catch(error => console.error('Erro ao buscar formulário de endereço:', error));
    };


    window.salvarNovoEndereco = () => {

        const formContainer = document.getElementById('form-novo-endereco');


        const rua = formContainer.querySelector('input[name="rua"]').value;
        const numero = formContainer.querySelector('input[name="numero"]').value;
        const bairro = formContainer.querySelector('input[name="bairro"]').value;
        const cidade = formContainer.querySelector('input[name="cidade"]').value;
        const cep = formContainer.querySelector('input[name="cep"]').value;
        const complemento = formContainer.querySelector('input[name="complemento"]').value;


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


        atualizarViewCarrinho('/endereco/salvar', options);
    };
});