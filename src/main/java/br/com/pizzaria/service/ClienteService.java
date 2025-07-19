package br.com.pizzaria.service;

import br.com.pizzaria.model.Cliente;
import br.com.pizzaria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Método antigo para listar todos (pode ser mantido ou removido)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // NOVO MÉTODO: Busca todos os clientes de forma paginada.
    public Page<Cliente> listarTodosPaginado(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
    }
}