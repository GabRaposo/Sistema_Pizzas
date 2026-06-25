package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private Cliente cliente;
    private List<ItemPedido> itens; // antes: "Pizza pizza" + listas paralelas de adicional/quantidade

    public Carrinho(Cliente cliente) {
        this.cliente = cliente;
        this.itens = new ArrayList<>();
    }

    public ItemPedido adicionarPizza(Pizza pizza, TamanhoPizza tamanho, int quantidade) {
        ItemPedido item = new ItemPedido(pizza, tamanho, quantidade); // validações já são feitas aqui dentro
        itens.add(item);
        return item;
    }

    public void adicionarAdicional(ItemPedido item, Adicional adicional, int quantidade) {
        if (!itens.contains(item)) {
            throw new RuntimeException("Esse item de pizza não pertence a este carrinho!");
        }
        item.adicionarAdicional(adicional, quantidade);
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.calcularTotal();
        }
        return total;
    }

    public Pedido finalizarPedido(ModoEntrega modoEntrega, FormaPagamento formaPagamento) {
        if (itens.isEmpty()) {
            throw new RuntimeException("Não é possível finalizar um pedido sem nenhuma pizza!");
        }
        return new Pedido(cliente, itens, modoEntrega, formaPagamento);
    }

    // getters
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }

    // o cliente só é escolhido na tela de Confirmar Pedido, depois que o
    // carrinho de pizzas já foi montado na tela de Registrar Pedido
    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new RuntimeException("Cliente não pode ser nulo!");
        }
        this.cliente = cliente;
    }
}
