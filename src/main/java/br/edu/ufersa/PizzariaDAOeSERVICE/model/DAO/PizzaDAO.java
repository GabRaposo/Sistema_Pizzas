package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;

public class PizzaDAO implements BaseDAO<Pizza> {

    // inserir pizza no banco
    public Pizza inserir(Pizza entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "INSERT INTO tb_pizza (tipo, valor) VALUES (?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getTipo());
            ps.setDouble(2, entity.getValor());
            ps.execute();

            // pegando o id gerado
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

    // deletar pizza pelo id
    public void deletar(Pizza entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "DELETE FROM tb_pizza WHERE id = ?";
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

    // atualizar tipo e valor da pizza
    public void alterar(Pizza entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "UPDATE tb_pizza SET tipo = ?, valor = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getTipo());
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

    // busca por tipo
    public List<Pizza> buscar(String param) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_pizza WHERE tipo = ?";
        List<Pizza> pizzas = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pizza p = new Pizza(rs.getString("tipo"), rs.getDouble("valor"));
                p.setId(rs.getLong("id"));
                pizzas.add(p);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return pizzas;
    }

    // listar todas as pizzas
    public List<Pizza> listar() {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_pizza";
        List<Pizza> pizzas = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pizza p = new Pizza(rs.getString("tipo"), rs.getDouble("valor"));
                p.setId(rs.getLong("id"));
                pizzas.add(p);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return pizzas;
    }

    // buscar pizza pelo id
    public Pizza buscarPorId(Long id) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT * FROM tb_pizza WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Pizza p = new Pizza(rs.getString("tipo"), rs.getDouble("valor"));
                p.setId(rs.getLong("id"));
                ps.close();
                return p;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return null;
    }

    // buscar pizzas distintas já pedidas por um cliente (item b: Pizza por Cliente)
    public List<Pizza> buscarPorCliente(String cpf) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT DISTINCT p.id, p.tipo, p.valor " +
                     "FROM tb_pizza p " +
                     "JOIN tb_item_pedido ip ON ip.pizza_id = p.id " +
                     "JOIN tb_pedido ped ON ip.pedido_id = ped.id " +
                     "JOIN tb_cliente c ON ped.cliente_id = c.id " +
                     "WHERE c.cpf = ?";
        List<Pizza> pizzas = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pizza p = new Pizza(rs.getString("tipo"), rs.getDouble("valor"));
                p.setId(rs.getLong("id"));
                pizzas.add(p);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return pizzas;
    }
}