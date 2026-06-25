package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements BaseDAO<Cliente>{

    //Inserir o cliente no BD
    public Cliente inserir (Cliente entity){
        Connection con = BaseDAO.getConnection();
        String sql = "INSERT INTO tb_cliente (nome, cpf, endereco) VALUE (?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getCpf());
            ps.setString(3, entity.getEndereco());
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
    public void deletar(Cliente entity){
        Connection con = BaseDAO.getConnection();
        String sql = "DELETE FROM tb_cliente WHERE id = ?";
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

    //update/alterar — CPF é tratado como imutável (identifica o cliente),
    //por isso não entra no UPDATE; só nome e endereço podem ser alterados.
    public void alterar(Cliente entity){
        Connection con = BaseDAO.getConnection();
        String sql = "UPDATE tb_cliente SET nome = ?, endereco = ? WHERE id = ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setLong(3, entity.getId());
            ps.execute();
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
    }

    //Select para cada cpf (lista)
    public List<Cliente> buscar(String param){
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_cliente WHERE cpf = ?";
        List<Cliente> clientes = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,param);
            ResultSet rs = ps.executeQuery();

            //tratamento saida da lista: cada linha vira um objeto cliente
            while (rs.next()){
                Cliente c = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("endereco")
                );
                c.setId(rs.getLong("id"));
                clientes.add(c);
            }
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
        return clientes;
    }

    //Selecionar todos
    public List<Cliente> listar(){
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_cliente";
        List<Cliente> clientes = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Cliente c = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("endereco")
                );
                c.setId(rs.getLong("id"));
                clientes.add(c);
            }
            ps.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            BaseDAO.closeConnection(con);
        }
        return clientes;
    }
}
