package br.edu.ufersa.PizzariaDAOeSERVICE.model.Entities;
 
public class Estoque {
    private Adicional adicional;
    private int quantidade;
 
    public Estoque(Adicional adicional, int quantidade) {
        if (adicional == null) {
            throw new RuntimeException("Adicional não pode ser nulo em um Estoque!");
        }
        if (quantidade < 0) {
            throw new RuntimeException("Quantidade inicial do estoque não pode ser negativa!");
        }
        this.adicional = adicional;
        this.quantidade = quantidade;
    }
 
    public void adicionar(int qtd) {
        if (qtd <= 0) {
            throw new RuntimeException("Quantidade a adicionar deve ser maior que zero!");
        }
        quantidade += qtd;
    }
 
    public void remover(int qtd) {
        if (qtd <= 0) {
            throw new RuntimeException("Quantidade a remover deve ser maior que zero!");
        }
        if (quantidade >= qtd) {
            quantidade -= qtd;
        } else {
            System.out.println("Estoque insuficiente para " + adicional.getNome());
        }
    }
 
    public int getQuantidade() {
        return quantidade;
    }
 
    public Adicional getAdicional() {
        return adicional;
    }
}