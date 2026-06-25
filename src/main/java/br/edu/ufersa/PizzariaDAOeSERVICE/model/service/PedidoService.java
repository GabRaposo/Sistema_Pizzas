package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.PedidoDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.EstadoPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;

public class PedidoService {
    private PedidoDAO dao = new PedidoDAO();

    //registrar (inserir) um novo pedido
    public Pedido registrarPedido(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new RuntimeException("O pedido deve ter um cliente!");
        }
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new RuntimeException("O pedido deve ter ao menos uma pizza!");
        }
        return dao.inserir(pedido);
    }

    //cancelar pedido: impede cancelar o que já foi entregue ou pago
    public void cancelarPedido(Pedido pedido) {
        if (dao.buscarPorId(pedido.getId()) == null) {
            throw new RuntimeException("Pedido não encontrado. Id: " + pedido.getId());
        }
        if (pedido.getEstado() == EstadoPedido.ENTREGUE || pedido.getEstado() == EstadoPedido.PAGO) {
            throw new RuntimeException(
                    "Não é possível cancelar um pedido já " + pedido.getEstado().name().toLowerCase() + "."
            );
        }
        pedido.alterarEstado(EstadoPedido.CANCELADO);
        dao.alterar(pedido);
    }

    //alterar estado do pedido (fluxo normal da cozinha/entrega)
    public void alterarEstadoPedido(Pedido pedido, EstadoPedido novoEstado) {
        if (dao.buscarPorId(pedido.getId()) == null) {
            throw new RuntimeException("Pedido não encontrado. Id: " + pedido.getId());
        }
        pedido.alterarEstado(novoEstado);
        dao.alterar(pedido);
    }

    // buscar pedidos por estado
    public List<Pedido> buscarPorEstado(EstadoPedido estado) {
        return dao.buscar(estado.name());
    }

    // buscar todos os pedidos de um cliente pelo CPF
    public List<Pedido> buscarPorCliente(String cpf) {
        return dao.buscarPorCliente(cpf);
    }

    //buscar todos os pedidos de um tipo de pizza (item b: Pedidos por Cliente, por Pizza, por estado)
    public List<Pedido> buscarPorPizza(Pizza pizza) {
        return dao.buscarPorPizza(pizza.getId());
    }

    // listar todos os pedidos
    public List<Pedido> listarPedidos() {
        return dao.listar();
    }

    //buscar pelo id
    public Pedido buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    // delegar o cálculo ao próprio pedido
    public double calcularTotal(Pedido pedido) {
        return pedido.calcularTotal();
    }
}