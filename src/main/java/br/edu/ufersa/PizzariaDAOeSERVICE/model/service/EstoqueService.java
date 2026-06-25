package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.exceptions.EstoqueInsuficienteException;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.EstoqueDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Estoque;

public class EstoqueService {
    private EstoqueDAO dao = new EstoqueDAO();

    // cadastrar estoque para um adicional
    public Estoque cadastrarEstoque(Estoque estoque) {
        return dao.inserir(estoque);
    }

    // remover o registro de estoque: verifica existência antes de deletar
    public void removerEstoque(Estoque estoque) {
        if (dao.buscarPorId(estoque.getId()) == null) {
            throw new RuntimeException("Estoque não encontrado. Id: " + estoque.getId());
        }
        dao.deletar(estoque);
    }

    // adicionar quantidade ao estoque e persistir
    public void adicionarQuantidade(Estoque estoque, int qtd) {
        if (dao.buscarPorId(estoque.getId()) == null) {
            throw new RuntimeException("Estoque não encontrado. Id: " + estoque.getId());
        }
        estoque.adicionar(qtd); // validação de qtd > 0 já está na entidade
        dao.alterar(estoque);
    }

    // remover quantidade do estoque: lança EstoqueInsuficienteException se não houver saldo
    public void removerQuantidade(Estoque estoque, int qtd) {
        if (dao.buscarPorId(estoque.getId()) == null) {
            throw new RuntimeException("Estoque não encontrado. Id: " + estoque.getId());
        }
        if (estoque.getQuantidade() < qtd) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para " + estoque.getAdicional().getNome() +
                    ". Disponível: " + estoque.getQuantidade() + ", solicitado: " + qtd
            );
        }
        estoque.remover(qtd); // validação de qtd > 0 já está na entidade
        dao.alterar(estoque);
    }

    // buscar estoque pelo nome do adicional
    public List<Estoque> buscarPorAdicional(String nomeAdicional) {
        return dao.buscar(nomeAdicional);
    }

    // listar todos os estoques
    public List<Estoque> listarEstoques() {
        return dao.listar();
    }

    // buscar pelo id
    public Estoque buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }
}