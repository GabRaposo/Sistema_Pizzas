package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.exceptions.EstoqueInsuficienteException;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.*;

public class CarrinhoService {
    private PedidoService pedidoService   = new PedidoService();
    private EstoqueService estoqueService = new EstoqueService();

    //adiciona uma pizza (com tamanho e quantidade) como uma nova linha do carrinho.
    //retorna o ItemPedido criado para o chamador poder adicionar adicionais a ele.
    public ItemPedido adicionarPizza(Carrinho carrinho, Pizza pizza, TamanhoPizza tamanho, int quantidade) {
        return carrinho.adicionarPizza(pizza, tamanho, quantidade);
    }

    // adiciona um adicional a uma linha específica do carrinho, verificando saldo no estoque antes
    public void adicionarAdicional(Carrinho carrinho, ItemPedido item, Adicional adicional, int quantidade) {
        List<Estoque> estoques = estoqueService.buscarPorAdicional(adicional.getNome());
        if (estoques.isEmpty()) {
            throw new RuntimeException(
                    "Adicional '" + adicional.getNome() + "' não encontrado no estoque!"
            );
        }
        Estoque estoque = estoques.get(0);
        if (estoque.getQuantidade() < quantidade) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para '" + adicional.getNome() +
                    "'. Disponível: " + estoque.getQuantidade() + ", solicitado: " + quantidade
            );
        }
        carrinho.adicionarAdicional(item, adicional, quantidade);
    }

    //calcular total do carrinho sem finalizar
    public double calcularTotal(Carrinho carrinho) {
        return carrinho.calcularTotal();
    }

    //finalizar o pedido: cria o Pedido (com todas as linhas de pizza, modo de entrega e
    //forma de pagamento) e persiste via PedidoService
    public Pedido finalizarPedido(Carrinho carrinho, ModoEntrega modoEntrega, FormaPagamento formaPagamento) {
        Pedido pedido = carrinho.finalizarPedido(modoEntrega, formaPagamento); // validação "sem pizza" já na entidade
        return pedidoService.registrarPedido(pedido);
    }
}
