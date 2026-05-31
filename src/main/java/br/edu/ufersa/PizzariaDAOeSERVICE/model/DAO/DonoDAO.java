package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Dono;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonoDAO implements BaseDAO<Dono>{

    //Inserir o Dono no BD: salva no tb_usuario
    public Dono inserir (Dono entity){
        Connection con = BaseDAO.getConnection();
        String sql = "INSERT INTO tb_usuario (nome, senha, tipo) VALUE (?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getSenha());
            ps.setString(3, entity.getTipo());
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
    public void deletar(Dono entity){
        Connection con = BaseDAO.getConnection();
        String sql = "DELETE FROM tb_usuario WHERE id = ? AND tipo = 'Dono'";
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

    //update/alterar do dono
    public void alterar(Dono entity){
        Connection con = BaseDAO.getConnection();
        String sql = "UPDATE tb_usuario SET nome = ?, senha = ? WHERE id = ? AND tipo = 'Dono'";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getSenha());
            ps.setLong(3, entity.getId());
            ps.execute();
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
    }

    //Select por nome
    public List<Dono> buscar(String param){
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario WHERE nome = ? AND tipo = 'Dono'";
        List<Dono> donos = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,param);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Dono d = new Dono(rs.getString("nome"), rs.getString("senha"));
                d.setId(rs.getLong("id"));
                donos.add(d);
            }
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
        return donos;
    }

    //Selecionar todos: listar
    public List<Dono> listar(){
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario WHERE tipo = 'Dono'";
        List<Dono> donos = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Dono d = new Dono(rs.getString("nome"), rs.getString("senha"));
                d.setId(rs.getLong("id"));
                donos.add(d);
            }
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
        return donos;
    }

    //selecionar por id
    public Dono buscarPorId(Long id){
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario WHERE id = ? AND tipo = 'Dono'";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                Dono d = new Dono(rs.getString("nome"), rs.getString("senha"));
                d.setId(rs.getLong("id"));
                ps.close();
                return d;
            }
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
        return null;
    }
}
