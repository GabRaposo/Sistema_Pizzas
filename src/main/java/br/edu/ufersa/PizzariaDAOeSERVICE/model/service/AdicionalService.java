package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.AdicionalDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.PedidoDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Adicional;


public class AdicionalService {
    private AdicionalDAO dao = new AdicionalDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public Adicional cadastrarAdicional(Adicional adicional) {
        List<Adicional> encontrados = dao.buscar(adicional.getNome());
        if (!encontrados.isEmpty()) {
            throw new RuntimeException("Já existe um adicional com o nome: " + adicional.getNome());
        }
        return dao.inserir(adicional);
    }

    public void removerAdicional(Adicional adicional) {
        if (dao.buscarPorId(adicional.getId()) == null) {
            throw new RuntimeException("Adicional não encontrado. Id: " + adicional.getId());
        }
        if (pedidoDAO.existePedidoComAdicional(adicional.getId())) {
            throw new RuntimeException(
                "Não é possível excluir o adicional '" + adicional.getNome() +
                "': ele já foi usado em pedidos existentes."
            );
        }
        dao.deletar(adicional);
    }

    public void alterarAdicional(Adicional adicional) {
        if (dao.buscarPorId(adicional.getId()) == null) {
            throw new RuntimeException("Adicional não encontrado. Id: " + adicional.getId());
        }
        dao.alterar(adicional);
    }

    public List<Adicional> buscarPorNome(String nome) {
        return dao.buscar(nome);
    }

    public List<Adicional> listarAdicionais() {
        return dao.listar();
    }

    public Adicional buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }
}
