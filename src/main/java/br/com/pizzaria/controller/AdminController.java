package br.com.pizzaria.controller;

import br.com.pizzaria.model.*;
import br.com.pizzaria.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CadastroService cadastroService;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PedidoService pedidoService;




    @GetMapping("/faturamento")
    public String faturamento(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                              @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        // Só executa a busca se as datas foram fornecidas
        if (dataInicio != null && dataFim != null) {
            Pageable pageable = PageRequest.of(page, 10, Sort.by("dataHora").descending());

            // Converte LocalDate para LocalDateTime para a busca no banco
            LocalDateTime inicio = dataInicio.atStartOfDay();
            LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

            Page<PedidoCompletoView> pedidosFaturamento = pedidoService.buscarPedidosPorPeriodoView(inicio, fim, pageable);
            BigDecimal faturamentoTotal = pedidoService.calcularFaturamentoPorPeriodo(inicio, fim);

            model.addAttribute("pedidosFaturamento", pedidosFaturamento);
            model.addAttribute("faturamentoTotal", faturamentoTotal);
            // Devolve as datas para manter os campos do formulário preenchidos
            model.addAttribute("dataInicio", dataInicio);
            model.addAttribute("dataFim", dataFim);
        }

        return "admin/faturamento"; // Retorna a nova página de faturamento
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model,
                                 @RequestParam(name = "clientePage", defaultValue = "0") int clientePage) {

        // Paginação para os clientes: 10 por página, ordenado por nome
        Pageable clientePageable = PageRequest.of(clientePage, 10, Sort.by("nome"));
        Page<Cliente> paginaDeClientes = clienteService.listarTodosPaginado(clientePageable);
        model.addAttribute("paginaDeClientes", paginaDeClientes);

        // Mantém as outras listas como estavam (não paginadas)
        model.addAttribute("funcionarios", funcionarioService.listarTodos());
        model.addAttribute("produtos", produtoService.listarTodos());

        return "admin/dashboard";
    }



    @GetMapping("/funcionarios/novo")
    public String novoFuncionarioForm(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "admin/form-funcionario";
    }

    @PostMapping("/funcionarios/salvar")
    public String salvarFuncionario(@ModelAttribute Funcionario funcionario, RedirectAttributes redirectAttributes) {
        try {
            cadastroService.cadastrarNovoFuncionario(funcionario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Funcionário cadastrado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/produtos/novo")
    public String novoProdutoForm(Model model) {
        model.addAttribute("produto", new Produto());
        return "admin/form-produto";
    }

    @PostMapping("/produtos/salvar")
    public String salvarProduto(@ModelAttribute Produto produto, RedirectAttributes redirectAttributes) {
        try {
            cadastroService.cadastrarNovoProduto(produto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto cadastrado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar produto.");
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/clientes/{clienteId}/pedidos")
    public String verPedidosDoCliente(@PathVariable("clienteId") Long clienteId,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      Model model) {

        Cliente cliente = clienteService.buscarPorId(clienteId);
        // Define que queremos 10 pedidos por página
        Pageable pageable = PageRequest.of(page, 10);

        Page<Pedido> paginaDePedidos = pedidoService.buscarHistoricoCompletoDoCliente(cliente, pageable);

        model.addAttribute("cliente", cliente);
        model.addAttribute("paginaDePedidos", paginaDePedidos); // Agora enviamos o objeto de página
        return "admin/cliente-pedidos";
    }
}