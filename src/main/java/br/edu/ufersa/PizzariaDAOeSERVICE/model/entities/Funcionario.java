package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.PedidoService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.ClienteService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.EstoqueService;

import java.util.List;

public class Funcionario extends Usuario {

    public Funcionario(String nome, String email, String senha) {
        super(nome, email, senha, "Funcionario");
    }

    public void registrarPedido(Pedido pedido) {
        PedidoService pedidoService = new PedidoService();
        pedidoService.registrarPedido(pedido);
    }

    public void editarCliente(Cliente cliente) {
        ClienteService clienteService = new ClienteService();
        clienteService.alterarCliente(cliente);
    }
    public void atualizarEstoque(Estoque estoque, int qtd) {
        EstoqueService estoqueService = new EstoqueService();
        estoqueService.adicionarQuantidade(estoque, qtd);
    }
    public List<Pedido> buscarPedido(String cpf) {
        PedidoService pedidoService = new PedidoService();
        return pedidoService.buscarPorCliente(cpf);
    }


}
