package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.exceptions.EstoqueInsuficienteException;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Adicional;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.EstadoPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Estoque;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.EstoqueDAO;

public class SistemaService {

    private final EstoqueService estoqueService = new EstoqueService();
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();

    public double processarPedido(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.PENDENTE) {
            throw new RuntimeException(
                "Pedido não pode ser processado pois não está PENDENTE. " +
                "Estado atual: " + pedido.getEstado()
            );
        }

        List<Adicional> adicionais = pedido.getTodosAdicionais();

        for (Adicional adicional : adicionais) {
            Estoque estoque = estoqueDAO.buscarPorAdicionalId(adicional.getId());

            if (estoque == null) {
                throw new RuntimeException(
                    "Estoque não cadastrado para o adicional: " + adicional.getNome()
                );
            }
            if (estoque.getQuantidade() < 1) {
                throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para o adicional: " + adicional.getNome() +
                    ". Disponível: " + estoque.getQuantidade() + ", solicitado: 1"
                );
            }
        }

       
        double total = calcularTotal(pedido);

        
        for (Adicional adicional : adicionais) {
            Estoque estoque = estoqueDAO.buscarPorAdicionalId(adicional.getId());
            estoqueService.removerQuantidade(estoque, 1);
        }

     
        pedido.alterarEstado(EstadoPedido.EM_PREPARO);

        System.out.println("Pedido processado com sucesso! Total: R$ " + String.format("%.2f", total));
        return total;
    }

    public double calcularTotal(Pedido pedido) {
        if (pedido == null) {
            throw new RuntimeException("Pedido não pode ser nulo para calcular o total!");
        }
        return pedido.calcularTotal();
    }

    public void cancelarPedido(Pedido pedido) {
        if (pedido.getEstado() == EstadoPedido.ENTREGUE) {
            throw new RuntimeException("Pedido já entregue não pode ser cancelado.");
        }
        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new RuntimeException("Pedido já está cancelado.");
        }

        
        if (pedido.getEstado() == EstadoPedido.EM_PREPARO || pedido.getEstado() == EstadoPedido.PRONTO) {
            for (Adicional adicional : pedido.getTodosAdicionais()) {
                Estoque estoque = estoqueDAO.buscarPorAdicionalId(adicional.getId());
                if (estoque != null) {
                    estoqueService.adicionarQuantidade(estoque, 1);
                }
            }
        }

        pedido.alterarEstado(EstadoPedido.CANCELADO);
        System.out.println("Pedido cancelado. Estoque revertido para os adicionais.");
    }

   
    public void avancarEstadoPedido(Pedido pedido) {
        switch (pedido.getEstado()) {
            case PENDENTE:
                // processa o pedido (valida e desconta estoque) e avança para EM_PREPARO
                processarPedido(pedido);
                System.out.println("Pedido processado e marcado como EM_PREPARO.");
                break;
            case EM_PREPARO:
                pedido.alterarEstado(EstadoPedido.PRONTO);
                System.out.println("Pedido marcado como PRONTO.");
                break;
            case PRONTO:
                pedido.alterarEstado(EstadoPedido.ENTREGUE);
                System.out.println("Pedido marcado como ENTREGUE.");
                break;
            case ENTREGUE:
                throw new RuntimeException("Pedido já foi entregue.");
            case CANCELADO:
                throw new RuntimeException("Pedido cancelado não pode ter o estado avançado.");
        }
    }
}