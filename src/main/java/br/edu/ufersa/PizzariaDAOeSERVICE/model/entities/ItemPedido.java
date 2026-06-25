package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

import java.util.ArrayList;
import java.util.List;


public class ItemPedido {
    private Long id; // adicionado para operações no banco
    private Pizza pizza;
    private TamanhoPizza tamanho;
    private int quantidade;
    private List<Adicional> adicionais; // lista "achatada": cada ocorrência = 1 unidade do adicional

    public ItemPedido(Pizza pizza, TamanhoPizza tamanho, int quantidade) {
        if (pizza == null) {
            throw new RuntimeException("Pizza não pode ser nula!");
        }
        if (tamanho == null) {
            throw new RuntimeException("Tamanho não pode ser nulo!");
        }
        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade da pizza deve ser maior que zero!");
        }
        this.pizza = pizza;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
        this.adicionais = new ArrayList<>();
    }

    public void adicionarAdicional(Adicional adicional, int quantidadeAdicional) {
        if (adicional == null) {
            throw new RuntimeException("Adicional não pode ser nulo!");
        }
        if (quantidadeAdicional <= 0) {
            throw new RuntimeException("Quantidade do adicional deve ser maior que zero!");
        }
        for (int i = 0; i < quantidadeAdicional; i++) {
            adicionais.add(adicional);
        }
    }

    // preço da pizza (já considerando o tamanho) * quantidade, mais os adicionais dessa linha
    public double calcularTotal() {
        double total = pizza.getValorPorTamanho(tamanho) * quantidade;
        for (Adicional adicional : adicionais) {
            total += adicional.getValor();
        }
        return total;
    }

    // getters
    public Long getId() { return id; }
    public Pizza getPizza() { return pizza; }
    public TamanhoPizza getTamanho() { return tamanho; }
    public int getQuantidade() { return quantidade; }
    public List<Adicional> getAdicionais() { return adicionais; }

    public void setId(Long id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new RuntimeException("Id inválido!");
        }
    }
}
