package br.edu.ufersa.PizzariaDAOeSERVICE.model.Service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.exceptions.EstoqueInsuficienteException;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.*;

// Carrinho é um objeto de sessão (sem id, sem tabela própria), portanto não tem DAO.
// O CarrinhoService adiciona a verificação de estoque antes de permitir adicionais
// e delega a persistência do Pedido final ao PedidoService.
public class CarrinhoService {
    private PedidoService pedidoService   = new PedidoService();
    private EstoqueService estoqueService = new EstoqueService();

    // adicionar pizza ao carrinho (validações básicas já estão na entidade)
    public void adicionarPizza(Carrinho carrinho, Pizza pizza, Dono dono) {
        carrinho.adicionarPizza(pizza, dono);
    }

    // adicionar adicional ao carrinho verificando saldo no estoque antes
    public void adicionarAdicional(Carrinho carrinho, Adicional adicional, int quantidade) {
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
        carrinho.adicionarAdicional(adicional, quantidade);
    }

    // calcular total do carrinho sem finalizar
    public double calcularTotal(Carrinho carrinho) {
        return carrinho.calcularTotal();
    }

    // finalizar o pedido: cria o Pedido e persiste via PedidoService
    public Pedido finalizarPedido(Carrinho carrinho, TamanhoPizza tamanho) {
        Pedido pedido = carrinho.finalizarPedido(tamanho); // validação "sem pizza" já na entidade
        return pedidoService.registrarPedido(pedido);
    }
}