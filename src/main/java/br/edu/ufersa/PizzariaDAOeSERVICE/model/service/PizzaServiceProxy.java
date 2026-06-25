package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Dono;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;


public class PizzaServiceProxy implements IPizzaService {

    private final PizzaService pizzaServiceReal;
    private final LoginService loginService;

    public PizzaServiceProxy(LoginService loginService) {
        this.pizzaServiceReal = new PizzaService();
        this.loginService = loginService;
    }

    //--------operações restritas que só o Dono pode executar ----------
    @Override
    public Pizza cadastrarPizza(Pizza pizza) {
        verificarPermissaoDeDono("cadastrar uma pizza");
        return pizzaServiceReal.cadastrarPizza(pizza);
    }

    @Override
    public void removerPizza(Pizza pizza) {
        verificarPermissaoDeDono("remover uma pizza");
        pizzaServiceReal.removerPizza(pizza);
    }

    @Override
    public void alterarPizza(Pizza pizza) {
        verificarPermissaoDeDono("alterar uma pizza");
        pizzaServiceReal.alterarPizza(pizza);
    }

    //---------operações de leitura: liberadas para qualquer usuário----------

    @Override
    public List<Pizza> buscarPorTipo(String tipo) {
        return pizzaServiceReal.buscarPorTipo(tipo);
    }

    public List<Pizza> buscarPorCliente(String cpf) {
        return pizzaServiceReal.buscarPorCliente(cpf);
    }

    @Override
    public List<Pizza> listarPizzas() {
        return pizzaServiceReal.listarPizzas();
    }

    @Override
    public Pizza buscarPorId(Long id) {
        return pizzaServiceReal.buscarPorId(id);
    }

    //Verificação de acesso

    private void verificarPermissaoDeDono(String acao) {
        if (!loginService.estaLogado()) {
            throw new RuntimeException("Acesso negado: é preciso estar logado para " + acao + ".");
        }
        Usuario usuario = loginService.getUsuarioLogado();
        if (!(usuario instanceof Dono)) {
            throw new RuntimeException(
                "Acesso negado: apenas o Dono pode " + acao +
                ". Usuário '" + usuario.getNome() + "' não tem permissão."
            );
        }
    }
}
