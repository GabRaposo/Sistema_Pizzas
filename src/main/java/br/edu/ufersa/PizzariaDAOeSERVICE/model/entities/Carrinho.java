package br.edu.ufersa.PizzariaDAOeSERVICE.model.Entities;
import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private Cliente cliente;
    private Pizza pizza;
    private int quantidadePizza;                 
    private List<Adicional> adicionais;
    private List<Integer> quantidadeAdicionais;  

    public Carrinho(Cliente cliente) {
        this.cliente = cliente;
        this.adicionais = new ArrayList<>();
        this.quantidadeAdicionais = new ArrayList<>();
        this.quantidadePizza = 0;
    }

    public void adicionarPizza(Pizza pizza, Dono dono) {
        if (pizza == null) {
            throw new RuntimeException("Pizza não pode ser nula!");
        }
        if (dono == null) {
            throw new RuntimeException("Dono não pode ser nulo!");
        }
        this.pizza = pizza;
        this.quantidadePizza = 1;
    }

    public void adicionarAdicional(Adicional adicional, int quantidade) {
        if (adicional == null) {
            throw new RuntimeException("Adicional não pode ser nulo!");
        }
        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade do adicional deve ser maior que zero!");
        }
        adicionais.add(adicional);
        quantidadeAdicionais.add(quantidade);
    }

    public double calcularTotal() {
        double total = 0;
        if (pizza != null) {
            total += pizza.getValor() * quantidadePizza;
        }
        for (int i = 0; i < adicionais.size(); i++) {
            total += adicionais.get(i).getValor() * quantidadeAdicionais.get(i);
        }
        return total;
    }

    public Pedido finalizarPedido(TamanhoPizza tamanho) {
        if (pizza == null) {
            throw new RuntimeException("Não é possível finalizar um pedido sem pizza!");
        }
        List<Adicional> listaFinal = new ArrayList<>();
        for (int i = 0; i < adicionais.size(); i++) {
            for (int j = 0; j < quantidadeAdicionais.get(i); j++) {
                listaFinal.add(adicionais.get(i));
            }
        }
        return new Pedido(cliente, pizza, listaFinal, tamanho);
    }
}