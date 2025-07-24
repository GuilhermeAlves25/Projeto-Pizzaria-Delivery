package br.com.pizzaria.controller;

import br.com.pizzaria.model.Cliente;
import br.com.pizzaria.service.CadastroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    @Autowired
    private CadastroService cadastroService;


    @GetMapping("/login")
    public String exibirFormularioLogin() {

        return "login";
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            cadastroService.cadastrarNovoCliente(cliente);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Faça seu login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/cadastro";
        }
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }






}