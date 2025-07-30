package br.com.pizzaria.service;

import br.com.pizzaria.model.Funcionario;
import br.com.pizzaria.repository.FuncionarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {


    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Page<Funcionario> listarTodosPaginado(Pageable pageable) {
        return funcionarioRepository.findAll(pageable);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }
}