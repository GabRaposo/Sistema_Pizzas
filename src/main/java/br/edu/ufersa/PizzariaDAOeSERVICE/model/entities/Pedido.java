package br.edu.ufersa.PizzariaDAOeSERVICE.model.Entities;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Cliente cliente;
    private Pizza pizza;
    private List<Adicional> adicionais;
    private TamanhoPizza tamanho;    
    private EstadoPedido estado;

    public Pedido(Cliente cliente, Pizza pizza, List<Adicional> adicionais, TamanhoPizza tamanho) {
        this.cliente = cliente;
        this.pizza = pizza;
        this.adicionais = new ArrayList<>(adicionais);
        this.tamanho = tamanho;
        this.estado = EstadoPedido.PENDENTE; 
    }

    public double calcularTotal() {
        double total = pizza.getValor();
        for (Adicional adc : adicionais) {
            total += adc.getValor();
        }
        return total;
    }

    public void alterarEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public List<Adicional> getAdicionais() {
        return adicionais;
    }

    public TamanhoPizza getTamanho() {
        return tamanho;
    }

    public EstadoPedido getEstado() {
        return estado;
    }
}