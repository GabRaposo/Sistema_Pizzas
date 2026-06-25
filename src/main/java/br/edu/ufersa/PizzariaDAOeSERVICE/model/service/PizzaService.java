package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.PedidoDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.PizzaDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;

public class PizzaService implements IPizzaService {
    private PizzaDAO dao = new PizzaDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    // cadastrar pizza: impede cadastro de tipo duplicado
    public Pizza cadastrarPizza(Pizza pizza) {
        List<Pizza> encontradas = dao.buscar(pizza.getTipo());
        if (!encontradas.isEmpty()) {
            throw new RuntimeException("Já existe uma pizza do tipo: " + pizza.getTipo());
        }
        return dao.inserir(pizza);
    }

    // remover pizza: verifica existência pelo id e se ela já não está em algum
    // pedido (excluir quebraria a reconstrução desse pedido e os totais do Relatório)
    public void removerPizza(Pizza pizza) {
        if (dao.buscarPorId(pizza.getId()) == null) {
            throw new RuntimeException("Pizza não encontrada. Id: " + pizza.getId());
        }
        if (pedidoDAO.existePedidoComPizza(pizza.getId())) {
            throw new RuntimeException(
                "Não é possível excluir a pizza '" + pizza.getTipo() +
                "': ela já está associada a pedidos existentes."
            );
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

    // buscar pizzas distintas já pedidas por um cliente (item b: Pizza por Cliente)
    public List<Pizza> buscarPorCliente(String cpf) {
        return dao.buscarPorCliente(cpf);
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