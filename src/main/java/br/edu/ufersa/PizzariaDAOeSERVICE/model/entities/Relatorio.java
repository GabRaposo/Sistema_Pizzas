package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Relatorio {
    private List<Pedido> pedidos;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Relatorio(List<Pedido> pedidos, LocalDate dataInicio, LocalDate dataFim) {
        if (pedidos == null) {
            throw new RuntimeException("A lista de pedidos do relatório não pode ser nula!");
        }
        if (dataInicio == null || dataFim == null) {
            throw new RuntimeException("As datas do relatório não podem ser nulas!");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new RuntimeException("A data fim não pode ser anterior à data início!");
        }
        this.pedidos = pedidos;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    // pedidos do relatório que caem dentro do período (dataInicio/dataFim)
    public List<Pedido> filtrarPorPeriodo() {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            LocalDate data = pedido.getDataHora().toLocalDate();
            if (!data.isBefore(dataInicio) && !data.isAfter(dataFim)) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }

    public List<Pedido> filtrarPorEstado(EstadoPedido estado) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : filtrarPorPeriodo()) {
            if (pedido.getEstado() == estado) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }

    // pedidos do período que contenham a pizza informada em algum dos seus itens
    public List<Pedido> filtrarPorPizza(Pizza pizza) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : filtrarPorPeriodo()) {
            for (ItemPedido item : pedido.getItens()) {
                if (item.getPizza().getId().equals(pizza.getId())) {
                    resultado.add(pedido);
                    break;
                }
            }
        }
        return resultado;
    }

    public List<Pedido> filtrarPorCliente(Cliente cliente) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : filtrarPorPeriodo()) {
            if (pedido.getCliente().getCpf().equals(cliente.getCpf())) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }

    // soma o total de todos os pedidos do período — foco do item (c): valores recebidos com as vendas
    public double calcularTotalVendas() {
        double total = 0;
        for (Pedido pedido : filtrarPorPeriodo()) {
            total += pedido.calcularTotal();
        }
        return total;
    }

    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
}
