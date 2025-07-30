package br.com.pizzaria.controller;

import br.com.pizzaria.model.*;
import br.com.pizzaria.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Controller
@RequestMapping("/admin")
public class AdminController {


    private final CadastroService cadastroService;
    private final FuncionarioService funcionarioService;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final PedidoService pedidoService;

    public AdminController(CadastroService cadastroService, FuncionarioService funcionarioService, ProdutoService produtoService, ClienteService clienteService, PedidoService pedidoService) {
        this.cadastroService = cadastroService;
        this.funcionarioService = funcionarioService;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/faturamento")
    public String faturamento(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                              @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {


        if (dataInicio != null && dataFim != null) {
            Pageable pageable = PageRequest.of(page, 10, Sort.by("dataHora").descending());


            LocalDateTime inicio = dataInicio.atStartOfDay();
            LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

            Page<PedidoCompletoView> pedidosFaturamento = pedidoService.buscarPedidosPorPeriodoView(inicio, fim, pageable);
            BigDecimal faturamentoTotal = pedidoService.calcularFaturamentoPorPeriodo(inicio, fim);

            model.addAttribute("pedidosFaturamento", pedidosFaturamento);
            model.addAttribute("faturamentoTotal", faturamentoTotal);
            model.addAttribute("dataInicio", dataInicio);
            model.addAttribute("dataFim", dataFim);
        }

        return "admin/faturamento";
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model,
                                 @RequestParam(name = "funcPage", defaultValue = "0") int funcPage,
                                 @RequestParam(name = "prodPage", defaultValue = "0") int prodPage,
                                 @RequestParam(name = "clientePage", defaultValue = "0") int clientePage) {

        int pageSize = 10;

        // Paginação para Funcionários
        Pageable funcionarioPageable = PageRequest.of(funcPage, pageSize, Sort.by("nome"));
        model.addAttribute("paginaDeFuncionarios", funcionarioService.listarTodosPaginado(funcionarioPageable));

        // Paginação para Produtos
        Pageable produtoPageable = PageRequest.of(prodPage, pageSize, Sort.by("nome"));
        model.addAttribute("paginaDeProdutos", produtoService.listarTodosParaAdminPaginado(produtoPageable));

        // Paginação para Clientes
        Pageable clientePageable = PageRequest.of(clientePage, pageSize, Sort.by("nome"));
        model.addAttribute("paginaDeClientes", clienteService.listarTodosPaginado(clientePageable));

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
    public String salvarProduto(@ModelAttribute Produto produto,
                                @RequestParam("imagemFile") MultipartFile imagemFile, // Recebe o arquivo
                                RedirectAttributes redirectAttributes) {
        try {

            cadastroService.cadastrarNovoProduto(produto, imagemFile);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto cadastrado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar produto.");
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/clientes/{clienteId}/pedidos")
    public String verPedidosDoCliente(@PathVariable("clienteId") Long clienteId,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      Model model) {

        Cliente cliente = clienteService.buscarPorId(clienteId);
        Pageable pageable = PageRequest.of(page, 10);

        Page<Pedido> paginaDePedidos = pedidoService.buscarHistoricoCompletoDoCliente(cliente, pageable);

        model.addAttribute("cliente", cliente);
        model.addAttribute("paginaDePedidos", paginaDePedidos);
        return "admin/cliente-pedidos";
    }


    @GetMapping("/produtos/editar/{id}")
    public String editarProdutoForm(@PathVariable("id") Long id, Model model) {

        Produto produto = produtoService.listarTodosParaAdmin().stream()
                .filter(p -> p.getId().equals(id)).findFirst().orElse(null);

        model.addAttribute("produto", produto);
        return "admin/form-produto-editar";
    }


    @PostMapping("/produtos/atualizar")
    public String atualizarProduto(@ModelAttribute Produto produto,
                                   @RequestParam("imagemFile") MultipartFile imagemFile,
                                   RedirectAttributes redirectAttributes) {
        try {
            cadastroService.atualizarProduto(produto, imagemFile);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar o produto.");
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/produtos/alternar-status/{id}")
    public String alternarStatusProduto(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {

            produtoService.alternarStatusProduto(id);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Status do produto alterado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao alterar o status do produto.");
        }
        return "redirect:/admin/dashboard";
    }
}