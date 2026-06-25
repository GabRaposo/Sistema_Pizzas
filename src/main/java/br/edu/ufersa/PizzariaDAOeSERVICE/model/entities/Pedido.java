package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id; // adicionado para operações no banco
    private Cliente cliente;
    private List<ItemPedido> itens; // várias pizzas por pedido (ver ItemPedido)
    private EstadoPedido estado;
    private ModoEntrega modoEntrega;
    private FormaPagamento formaPagamento;
    private LocalDateTime dataHora; // necessário pro Relatorio filtrar por período

    public Pedido(Cliente cliente, List<ItemPedido> itens, ModoEntrega modoEntrega, FormaPagamento formaPagamento) {
        if (cliente == null) {
            throw new RuntimeException("O pedido deve ter um cliente!");
        }
        if (itens == null || itens.isEmpty()) {
            throw new RuntimeException("O pedido deve ter ao menos uma pizza!");
        }
        if (modoEntrega == null) {
            throw new RuntimeException("É preciso informar o modo de entrega (Entrega ou Retirada)!");
        }
        if (formaPagamento == null) {
            throw new RuntimeException("É preciso informar a forma de pagamento (Pix ou Cartão)!");
        }
        this.cliente = cliente;
        this.itens = new ArrayList<>(itens);
        this.estado = EstadoPedido.PENDENTE;
        this.modoEntrega = modoEntrega;
        this.formaPagamento = formaPagamento;
        this.dataHora = LocalDateTime.now();
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.calcularTotal();
        }
        return total;
    }

    public void alterarEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    // todos os adicionais de todos os itens juntos — usado pelo SistemaService
    // pra dar baixa no estoque de cada adicional consumido pelo pedido
    public List<Adicional> getTodosAdicionais() {
        List<Adicional> todos = new ArrayList<>();
        for (ItemPedido item : itens) {
            todos.addAll(item.getAdicionais());
        }
        return todos;
    }

    // getters
    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public EstadoPedido getEstado() { return estado; }
    public ModoEntrega getModoEntrega() { return modoEntrega; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public LocalDateTime getDataHora() { return dataHora; }

    public void setId(Long id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new RuntimeException("Id inválido!");
        }
    }

    // usado pelo DAO ao reconstruir o pedido do banco com a data/hora original
    public void setDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new RuntimeException("Data/hora não pode ser nula!");
        }
        this.dataHora = dataHora;
    }
}
