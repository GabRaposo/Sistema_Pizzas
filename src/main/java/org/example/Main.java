package org.example;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Dono;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Funcionario;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //main pra testar se ta tudo certo
        DonoService donoService = new DonoService();
        FuncionarioService funcService = new FuncionarioService();
        UsuarioService usuarioService = new UsuarioService();
        LoginService loginService = new LoginService();


        //1. CADASTRAR
        System.out.println("=== CADASTRANDO ===");
        Dono dono = new Dono("Gadelha", "senha123");
        donoService.cadastrarDono(dono);
        System.out.println("Dono cadastrado. Id: " + dono.getId());

        Funcionario func = new Funcionario("Periclo", "senha456");
        funcService.cadastrarFuncionario(func);
        System.out.println("Funcionario cadastrado. Id: " + func.getId());

        //2. Listar todos
        System.out.println("\n=== LISTANDO TODOS OS USUARIOS ===");
        for (Usuario u : usuarioService.listarUsuarios()) {
            System.out.println("- " + u.getId() + " | " + u.getNome() + " | " + u.getTipo());
        }

        //3. Listar por tipo
        System.out.println("\n=== LISTANDO APENAS DONOS ===");
        for (Dono d : donoService.listarDonos()) {
            System.out.println("- " + d.getId() + " | " + d.getNome());
        }

        System.out.println("\n=== LISTANDO APENAS FUNCIONARIOS ===");
        for (Funcionario f : funcService.listarFuncionarios()) {
            System.out.println("- " + f.getId() + " | " + f.getNome());
        }

        //4. Autenticar
        System.out.println("\n=== AUTENTICANDO ===");

        boolean loginDono = loginService.autenticar("Gadelha", "senha123");
        System.out.println("Login Dono: " + (loginDono ? "sucesso" : "falhou"));
        if (loginService.estaLogado()) {
            System.out.println("Logado como: " + loginService.getUsuarioLogado().getNome()
                    + " | Tipo: " + loginService.getUsuarioLogado().getTipo());
        }

        loginService.logout();
        System.out.println("Logout realizado. Logado: " + loginService.estaLogado());

        boolean loginErrado = loginService.autenticar("Gadelha", "senhaErrada");
        System.out.println("Login com senha errada: " + (loginErrado ? "sucesso" : "falhou"));

        //5. Alterar
        System.out.println("\n=== ALTERANDO DONO ===");
        dono.setNome("Gadelha new");
        donoService.alterarDono(dono);
        System.out.println("Nome alterado para: " + donoService.buscarPorNome("Gadelha new").get(0).getNome());

        //6. REMOVER
        System.out.println("\n=== REMOVENDO ===");
        donoService.removerDono(dono);
        funcService.removerFuncionario(func);
        System.out.println("Removidos! Total restante: " + usuarioService.listarUsuarios().size());
    }
}