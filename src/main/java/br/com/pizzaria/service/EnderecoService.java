package br.com.pizzaria.service;

import br.com.pizzaria.model.Endereco;
import br.com.pizzaria.model.Usuario;
import br.com.pizzaria.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }


    public void salvarNovoEndereco(Endereco endereco, Usuario usuarioLogado) {

        endereco.setUsuario(usuarioLogado);
        enderecoRepository.save(endereco);
    }
}