package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements BaseDAO<Usuario>{

    //Inserir o cliente no BD
    public Usuario inserir (Usuario entity){
        Connection con = BaseDAO.getConnection();
        String sql = "INSERT INTO tb_usuario (nome, email, senha, tipo) VALUE (?,?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getSenha());
            ps.setString(4, entity.getTipo());
            ps.execute();

            //pegando o id
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                entity.setId(rs.getLong(1));
            }
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
        return entity;
    }

    //deletar
    public void deletar(Usuario entity){
        Connection con = BaseDAO.getConnection();
        String sql = "DELETE FROM tb_usuario WHERE id = ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1,entity.getId());
            ps.execute();
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
    }

    //update/alterar
    public void alterar(Usuario entity){
        Connection con = BaseDAO.getConnection();
        String sql = "UPDATE tb_usuario SET nome = ?, email = ?, senha = ?, tipo = ? WHERE id = ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getSenha());
            ps.setString(4, entity.getTipo());
            ps.setLong(5, entity.getId());
            ps.execute();
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
    }

    //Select para cada cpf (lista)
    public List<Usuario> buscar(String param){
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario WHERE nome = ?";
        List<Usuario> usuarios = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,param);
            ResultSet rs = ps.executeQuery();

            //tratamento saida da lista: cada linha vira um objeto cliente
            while (rs.next()){
                Usuario u = new Usuario(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("tipo")
                );
                u.setId(rs.getLong("id"));
                usuarios.add(u);
            }
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
        return usuarios;
    }

    //Selecionar todos: listar
    public List<Usuario> listar(){
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            //tratamento saida da lista: cada linha vira um objeto cliente
            while (rs.next()){
                Usuario u = new Usuario(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("tipo")
                );
                u.setId(rs.getLong("id"));
                usuarios.add(u);
            }
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
        return usuarios;
    }

    public Usuario buscarPorId(Long id) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("tipo")
                );
                u.setId(rs.getLong("id"));
                ps.close();
                return u;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return null; // não encontrado
    }


}
