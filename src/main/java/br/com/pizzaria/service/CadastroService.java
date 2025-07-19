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


@Service
public class CadastroService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FuncionarioRepository funcionarioRepository; // Adicione este
    @Autowired
    private ProdutoRepository produtoRepository; // Adicione este
    @Autowired
    private ProdutoFactory produtoFactory;

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

    public void cadastrarNovoProduto(Produto dadosProduto) {
        // AQUI ESTÁ O USO DO PADRÃO FACTORY METHOD:
        // Não usamos "new Produto()". Pedimos para a fábrica criar o objeto.
        Produto novoProduto = produtoFactory.criarProduto(
                dadosProduto.getNome(),
                dadosProduto.getDescricao(),
                dadosProduto.getPreco(),
                dadosProduto.getTipo(),
                dadosProduto.getTamanho(),
                dadosProduto.getCaminhoImagem()
        );
        produtoRepository.save(novoProduto);
    }

}