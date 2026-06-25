package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Dono;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO implements BaseDAO<Funcionario> {
    //Inserir o Dono no BD: salva no tb_usuario
    public Funcionario inserir(Funcionario entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "INSERT INTO tb_usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getSenha());
            ps.setString(4, entity.getTipo()); // sempre "Funcionario"
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return entity;
    }


    public void deletar(Funcionario entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "DELETE FROM tb_usuario WHERE id = ? AND tipo = 'Funcionario'";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, entity.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
    }


    public void alterar(Funcionario entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "UPDATE tb_usuario SET nome = ?, email = ?, senha = ? WHERE id = ? AND tipo = 'Funcionario'";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getSenha());
            ps.setLong(4, entity.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
    }

    public Funcionario buscarPorId(Long id) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario WHERE id = ? AND tipo = 'Funcionario'";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Funcionario f = new Funcionario(rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
                f.setId(rs.getLong("id"));
                ps.close();
                return f;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return null;
    }

    public List<Funcionario> buscar(String param) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario WHERE nome = ? AND tipo = 'Funcionario'";
        List<Funcionario> funcionarios = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario f = new Funcionario(rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
                f.setId(rs.getLong("id"));
                funcionarios.add(f);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return funcionarios;
    }

    public List<Funcionario> listar() {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_usuario WHERE tipo = 'Funcionario'";
        List<Funcionario> funcionarios = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario f = new Funcionario(rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
                f.setId(rs.getLong("id"));
                funcionarios.add(f);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return funcionarios;
    }

}
