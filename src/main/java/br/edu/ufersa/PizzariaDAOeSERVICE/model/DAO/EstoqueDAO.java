package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Adicional;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Estoque;

// Assume que tb_adicional possui colunas: id, nome, valor
// Assume que tb_estoque possui colunas: id, adicional_id, quantidade
public class EstoqueDAO implements BaseDAO<Estoque> {

    // inserir estoque vinculado a um adicional já existente no banco
    public Estoque inserir(Estoque entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "INSERT INTO tb_estoque (adicional_id, quantidade) VALUES (?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entity.getAdicional().getId());
            ps.setInt(2, entity.getQuantidade());
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

    // deletar estoque pelo id
    public void deletar(Estoque entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "DELETE FROM tb_estoque WHERE id = ?";
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

    // atualiza apenas a quantidade (adicional não muda)
    public void alterar(Estoque entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "UPDATE tb_estoque SET quantidade = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, entity.getQuantidade());
            ps.setLong(2, entity.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
    }

    // busca pelo nome do adicional (JOIN com tb_adicional)
    public List<Estoque> buscar(String param) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT e.id, e.quantidade, " +
                     "a.id AS adicional_id, a.nome, a.valor " +
                     "FROM tb_estoque e " +
                     "JOIN tb_adicional a ON e.adicional_id = a.id " +
                     "WHERE a.nome = ?";
        List<Estoque> estoques = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                estoques.add(montarEstoque(rs));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return estoques;
    }

    // listar todos os estoques com seus adicionais
    public List<Estoque> listar() {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT e.id, e.quantidade, " +
                     "a.id AS adicional_id, a.nome, a.valor " +
                     "FROM tb_estoque e " +
                     "JOIN tb_adicional a ON e.adicional_id = a.id";
        List<Estoque> estoques = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                estoques.add(montarEstoque(rs));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return estoques;
    }

    // buscar estoque pelo id
    public Estoque buscarPorId(Long id) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT e.id, e.quantidade, " +
                     "a.id AS adicional_id, a.nome, a.valor " +
                     "FROM tb_estoque e " +
                     "JOIN tb_adicional a ON e.adicional_id = a.id " +
                     "WHERE e.id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Estoque estoque = montarEstoque(rs);
                ps.close();
                return estoque;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return null;
    }

    // buscar estoque diretamente pelo id do adicional
    public Estoque buscarPorAdicionalId(Long adicionalId) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT e.id, e.quantidade, " +
                     "a.id AS adicional_id, a.nome, a.valor " +
                     "FROM tb_estoque e " +
                     "JOIN tb_adicional a ON e.adicional_id = a.id " +
                     "WHERE e.adicional_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, adicionalId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Estoque estoque = montarEstoque(rs);
                ps.close();
                return estoque;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return null;
    }

    // monta o objeto Estoque (com Adicional embutido) a partir do ResultSet
    private Estoque montarEstoque(ResultSet rs) throws SQLException {
        Adicional adicional = new Adicional(rs.getString("nome"), rs.getDouble("valor"));
        adicional.setId(rs.getLong("adicional_id"));
        Estoque estoque = new Estoque(adicional, rs.getInt("quantidade"));
        estoque.setId(rs.getLong("id"));
        return estoque;
    }
}