package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Adicional;


public class AdicionalDAO implements BaseDAO<Adicional> {

    public Adicional inserir(Adicional entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "INSERT INTO tb_adicional (nome, valor) VALUES (?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setDouble(2, entity.getValor());
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

    public void deletar(Adicional entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "DELETE FROM tb_adicional WHERE id = ?";
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

    public void alterar(Adicional entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "UPDATE tb_adicional SET nome = ?, valor = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setDouble(2, entity.getValor());
            ps.setLong(3, entity.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
    }

    public List<Adicional> buscar(String param) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_adicional WHERE nome = ?";
        List<Adicional> adicionais = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Adicional a = new Adicional(rs.getString("nome"), rs.getDouble("valor"));
                a.setId(rs.getLong("id"));
                adicionais.add(a);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return adicionais;
    }

    public List<Adicional> listar() {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_adicional";
        List<Adicional> adicionais = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Adicional a = new Adicional(rs.getString("nome"), rs.getDouble("valor"));
                a.setId(rs.getLong("id"));
                adicionais.add(a);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return adicionais;
    }

    public Adicional buscarPorId(Long id) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_adicional WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Adicional a = new Adicional(rs.getString("nome"), rs.getDouble("valor"));
                a.setId(rs.getLong("id"));
                ps.close();
                return a;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return null;
    }
}
