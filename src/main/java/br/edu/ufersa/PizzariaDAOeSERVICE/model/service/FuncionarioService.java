package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.FuncionarioDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Funcionario;

import java.util.List;

public class FuncionarioService {

    private FuncionarioDAO dao = new FuncionarioDAO();

    public Funcionario cadastrarFuncionario(Funcionario funcionario) {
        return dao.inserir(funcionario);
    }

    public void removerFuncionario(Funcionario funcionario) {
        if (dao.buscarPorId(funcionario.getId()) == null) {
            throw new RuntimeException("Funcionário não encontrado. Id: " + funcionario.getId());
        }
        dao.deletar(funcionario);
    }

    public void alterarFuncionario(Funcionario funcionario) {
        if (dao.buscarPorId(funcionario.getId()) == null) {
            throw new RuntimeException("Funcionário não encontrado. Id: " + funcionario.getId());
        }
        dao.alterar(funcionario);
    }

    public List<Funcionario> buscarPorNome(String nome) {
        return dao.buscar(nome);
    }

    public List<Funcionario> listarFuncionarios() {
        return dao.listar();
    }
}
