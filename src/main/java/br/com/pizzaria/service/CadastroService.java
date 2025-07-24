package br.com.pizzaria.service;

import br.com.pizzaria.factory.ProdutoFactory;
import br.com.pizzaria.model.Cliente;
import br.com.pizzaria.model.Funcionario;
import br.com.pizzaria.model.Produto;
import br.com.pizzaria.repository.ClienteRepository;
import br.com.pizzaria.repository.FuncionarioRepository;
import br.com.pizzaria.repository.ProdutoRepository;
import br.com.pizzaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class CadastroService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ProdutoFactory produtoFactory;
    @Autowired
    private S3Service s3Service;

    @Transactional
    public void cadastrarNovoCliente(Cliente cliente) {

        if (usuarioRepository.findByEmail(cliente.getEmail()).isPresent()) {

            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }


        String senhaCriptografada = passwordEncoder.encode(cliente.getSenha());
        cliente.setSenha(senhaCriptografada);


        clienteRepository.save(cliente);
    }
    @Transactional
    public void cadastrarNovoFuncionario(Funcionario funcionario) {
        if (usuarioRepository.findByEmail(funcionario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        funcionarioRepository.save(funcionario);
    }

    public void cadastrarNovoProduto(Produto dadosProduto, MultipartFile imagem) {
        String urlDaImagem = null;

        // Se um arquivo de imagem foi enviado, faz o upload
        if (imagem != null && !imagem.isEmpty()) {
            urlDaImagem = s3Service.uploadFile(imagem);
        }

        Produto novoProduto = produtoFactory.criarProduto(
                dadosProduto.getNome(),
                dadosProduto.getDescricao(),
                dadosProduto.getPreco(),
                dadosProduto.getTipo(),
                dadosProduto.getTamanho(),
                urlDaImagem // Usa a URL retornada pelo S3
        );
        produtoRepository.save(novoProduto);
    }


    @Transactional
    public void atualizarProduto(Produto dadosProduto, MultipartFile imagem) {
        // Busca o produto original no banco de dados para garantir que ele existe
        Produto produtoExistente = produtoRepository.findById(dadosProduto.getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        // Atualiza os dados do produto com as informações do formulário
        produtoExistente.setNome(dadosProduto.getNome());
        produtoExistente.setDescricao(dadosProduto.getDescricao());
        produtoExistente.setPreco(dadosProduto.getPreco());
        produtoExistente.setTipo(dadosProduto.getTipo().toUpperCase());
        produtoExistente.setTamanho(dadosProduto.getTamanho().toUpperCase());

        // Lógica para a imagem:
        // Se um novo arquivo de imagem foi enviado (e não está vazio)...
        if (imagem != null && !imagem.isEmpty()) {
            // ...faz o upload da nova imagem para o S3...
            String novaUrlDaImagem = s3Service.uploadFile(imagem);
            // ...e atualiza o caminho da imagem no produto.
            produtoExistente.setCaminhoImagem(novaUrlDaImagem);
        }
        // Se nenhuma imagem nova foi enviada, o caminho da imagem antiga é mantido.

        produtoRepository.save(produtoExistente);
    }
}