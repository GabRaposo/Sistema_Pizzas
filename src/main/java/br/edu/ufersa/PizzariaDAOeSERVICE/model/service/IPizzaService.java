package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;

public interface IPizzaService {
    Pizza cadastrarPizza(Pizza pizza);
    void removerPizza(Pizza pizza);
    void alterarPizza(Pizza pizza);
    List<Pizza> buscarPorTipo(String tipo);
    List<Pizza> buscarPorCliente(String cpf);
    List<Pizza> listarPizzas();
    Pizza buscarPorId(Long id);
}
