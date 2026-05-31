package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.ClienteService;

public class Funcionario extends Usuario {

    public Funcionario(String nome, String senha) {
        super(nome, senha, "Funcionario");
    }

    //falta a classe de registrar pedido pra conseguir implementar
    public void registrarPedido(/* Pedido pedido */) {
        // PedidoService pedidoService = new PedidoService();
        // pedidoService.registrarPedido(pedido);
        throw new UnsupportedOperationException("A");
    }


    public void editarCliente(Cliente cliente) {
        ClienteService clienteService = new ClienteService();
        clienteService.alterarCliente(cliente);
    }

   //lembrar de implemntar apos implementar o estoque
    public void atualizarEstoque(/* Estoque estoque */) {

        // EstoqueService estoqueService = new EstoqueService();
        // estoqueService.atualizar(estoque);
        throw new UnsupportedOperationException("A");
    }

    //esperando a classe pedido
    public void buscarPedido(/* String param */) {

        // PedidoService pedidoService = new PedidoService();
        // return pedidoService.buscarPedido(param);
        throw new UnsupportedOperationException("A");
    }

}
