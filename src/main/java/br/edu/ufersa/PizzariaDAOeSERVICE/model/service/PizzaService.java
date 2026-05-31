package br.edu.ufersa.PizzariaDAOeSERVICE.model.Service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.PizzaDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;

public class PizzaService {
    private PizzaDAO dao = new PizzaDAO();

    // cadastrar pizza: impede cadastro de tipo duplicado
    public Pizza cadastrarPizza(Pizza pizza) {
        List<Pizza> encontradas = dao.buscar(pizza.getTipo());
        if (!encontradas.isEmpty()) {
            throw new RuntimeException("Já existe uma pizza do tipo: " + pizza.getTipo());
        }
        return dao.inserir(pizza);
    }

    // remover pizza: verifica existência pelo id antes de deletar
    public void removerPizza(Pizza pizza) {
        if (dao.buscarPorId(pizza.getId()) == null) {
            throw new RuntimeException("Pizza não encontrada. Id: " + pizza.getId());
        }
        dao.deletar(pizza);
    }

    // alterar pizza: verifica existência pelo id antes de atualizar
    public void alterarPizza(Pizza pizza) {
        if (dao.buscarPorId(pizza.getId()) == null) {
            throw new RuntimeException("Pizza não encontrada. Id: " + pizza.getId());
        }
        dao.alterar(pizza);
    }

    // buscar por tipo
    public List<Pizza> buscarPorTipo(String tipo) {
        return dao.buscar(tipo);
    }

    // listar todas as pizzas
    public List<Pizza> listarPizzas() {
        return dao.listar();
    }

    // buscar pelo id
    public Pizza buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }
}