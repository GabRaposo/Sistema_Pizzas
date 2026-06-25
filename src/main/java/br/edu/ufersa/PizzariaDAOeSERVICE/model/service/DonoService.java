package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.DonoDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.UsuarioDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Dono;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;

import java.util.List;

public class DonoService {
    private DonoDAO dao = new DonoDAO();

    //cadastrando — só pode existir UM Dono no sistema (o sr. Michelangelo)
    public Dono cadastrarDono(Dono dono){
        if (!dao.listar().isEmpty()) {
            throw new RuntimeException("Já existe um Dono cadastrado no sistema! Só é permitido um Dono.");
        }
        return dao.inserir(dono);
    }

    //removendo pelo id
    public void removerDono(Dono dono){
        if (dao.buscarPorId(dono.getId()) == null){
            throw new RuntimeException("Dono não encontrado. id: "+ dono.getId());
        }
        dao.deletar(dono);
    }

    //alterando pelo id
    public void alterarDono(Dono dono) {
        if (dao.buscarPorId(dono.getId()) == null) {
            throw new RuntimeException("Dono não encontrado. Id: " + dono.getId());
        }
        dao.alterar(dono);
    }

    //buscando por nome
    public List<Dono> buscarPorNome(String nome) {
        return dao.buscar(nome);
    }


    //listando
    public List<Dono> listarDonos() {
        return dao.listar();
    }


}
