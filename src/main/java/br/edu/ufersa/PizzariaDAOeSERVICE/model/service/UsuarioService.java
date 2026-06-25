package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.BaseDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.ClienteDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.UsuarioDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Dono;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Funcionario;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;

import java.util.List;

public class UsuarioService {
    private UsuarioDAO dao = new UsuarioDAO();

    //cadastrando
    public Usuario cadastrarUsuario(Usuario usuario){
        return dao.inserir(usuario);
    }

    //removendo
    public void removerUsuario(Usuario usuario){
        if (dao.buscarPorId(usuario.getId()) == null){
            throw new RuntimeException("Usuario não encontrado. id: "+ usuario.getId());
        }
        dao.deletar(usuario);
    }

    //alterando
    public void alterarUsuario(Usuario usuario) {
        if (dao.buscarPorId(usuario.getId()) == null) {
            throw new RuntimeException("Usuário não encontrado. Id: " + usuario.getId());
        }
        dao.alterar(usuario);
    }

    //buscando por nome
    public List<Usuario> buscarPorNome(String nome) {
        return dao.buscar(nome);
    }


    //listando
    public List<Usuario> listarUsuarios() {
        return dao.listar();
    }

    //autenticando senha e nome
    public Usuario autenticar(String nome, String senha) {
        List<Usuario> encontrados = dao.buscar(nome);
        for (Usuario u : encontrados) {
            if (u.getSenha().equals(senha)) {
                // Reconstrói o objeto com o subtipo correto baseado no campo "tipo" do banco
                if ("Dono".equals(u.getTipo())) {
                    Dono dono = new Dono(u.getNome(), u.getEmail(), u.getSenha());
                    dono.setId(u.getId());
                    return dono;
                } else {
                    Funcionario func = new Funcionario(u.getNome(), u.getEmail(), u.getSenha());
                    func.setId(u.getId());
                    return func;
                }
            }
        }
        return null;//nome ou senha incorretos
    }
}
