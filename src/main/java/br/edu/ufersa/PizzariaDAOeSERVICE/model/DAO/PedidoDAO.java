package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.*;

// Tabelas envolvidas:
//   tb_pedido:            id, cliente_id, pizza_id, tamanho (VARCHAR), estado (VARCHAR)
//   tb_pedido_adicional:  pedido_id, adicional_id  (tabela de junção)
//   tb_adicional:         id, nome, valor           (responsabilidade de outro membro)
public class PedidoDAO implements BaseDAO<Pedido> {

    // inserir pedido e seus adicionais na mesma conexão
    public Pedido inserir(Pedido entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "INSERT INTO tb_pedido (cliente_id, pizza_id, tamanho, estado) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entity.getCliente().getId());
            ps.setLong(2, entity.getPizza().getId());
            ps.setString(3, entity.getTamanho().name());
            ps.setString(4, entity.getEstado().name());
            ps.execute();

            // pegando o id gerado antes de fechar o ps
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
            ps.close();

            // inserir adicionais na tabela de junção (mesma conexão, ps já fechado)
            inserirAdicionais(con, entity);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return entity;
    }

    // deletar pedido e seus adicionais (FK exige deletar junção primeiro)
    public void deletar(Pedido entity) {
        Connection con = BaseDAO.getConnection();
        try {
            // 1. remove registros da tabela de junção
            String sqlAdicionais = "DELETE FROM tb_pedido_adicional WHERE pedido_id = ?";
            PreparedStatement ps1 = con.prepareStatement(sqlAdicionais);
            ps1.setLong(1, entity.getId());
            ps1.execute();
            ps1.close();

            // 2. remove o pedido
            String sql = "DELETE FROM tb_pedido WHERE id = ?";
            PreparedStatement ps2 = con.prepareStatement(sql);
            ps2.setLong(1, entity.getId());
            ps2.execute();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
    }

    // atualiza apenas o estado (o restante do pedido é imutável após criado)
    public void alterar(Pedido entity) {
        Connection con = BaseDAO.getConnection();
        String sql = "UPDATE tb_pedido SET estado = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getEstado().name());
            ps.setLong(2, entity.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
    }

    // busca por estado (ex: "PENDENTE", "EM_PREPARO", "ENTREGUE" ...)
    public List<Pedido> buscar(String param) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.tamanho, p.estado, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco, " +
                     "pz.id AS pizza_id, pz.tipo, pz.valor " +
                     "FROM tb_pedido p " +
                     "JOIN tb_cliente c  ON p.cliente_id = c.id " +
                     "JOIN tb_pizza   pz ON p.pizza_id   = pz.id " +
                     "WHERE p.estado = ?";
        List<Pedido> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(montarPedido(rs));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return pedidos;
    }

    // listar todos os pedidos
    public List<Pedido> listar() {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.tamanho, p.estado, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco, " +
                     "pz.id AS pizza_id, pz.tipo, pz.valor " +
                     "FROM tb_pedido p " +
                     "JOIN tb_cliente c  ON p.cliente_id = c.id " +
                     "JOIN tb_pizza   pz ON p.pizza_id   = pz.id";
        List<Pedido> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(montarPedido(rs));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return pedidos;
    }

    // buscar pedido pelo id
    public Pedido buscarPorId(Long id) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.tamanho, p.estado, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco, " +
                     "pz.id AS pizza_id, pz.tipo, pz.valor " +
                     "FROM tb_pedido p " +
                     "JOIN tb_cliente c  ON p.cliente_id = c.id " +
                     "JOIN tb_pizza   pz ON p.pizza_id   = pz.id " +
                     "WHERE p.id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Pedido pedido = montarPedido(rs);
                ps.close();
                return pedido;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return null;
    }

    // busca todos os pedidos de um cliente pelo CPF
    public List<Pedido> buscarPorCliente(String cpf) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.tamanho, p.estado, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco, " +
                     "pz.id AS pizza_id, pz.tipo, pz.valor " +
                     "FROM tb_pedido p " +
                     "JOIN tb_cliente c  ON p.cliente_id = c.id " +
                     "JOIN tb_pizza   pz ON p.pizza_id   = pz.id " +
                     "WHERE c.cpf = ?";
        List<Pedido> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(montarPedido(rs));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return pedidos;
    }

    // -------------------------------------------------------------------------
    // Helpers privados
    // -------------------------------------------------------------------------

    // reconstrói o objeto Pedido completo a partir do ResultSet
    private Pedido montarPedido(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
                rs.getString("cliente_nome"),
                rs.getString("cpf"),
                rs.getString("endereco")
        );
        cliente.setId(rs.getLong("cliente_id"));

        Pizza pizza = new Pizza(rs.getString("tipo"), rs.getDouble("valor"));
        pizza.setId(rs.getLong("pizza_id"));

        long pedidoId = rs.getLong("id");
        TamanhoPizza tamanho = TamanhoPizza.valueOf(rs.getString("tamanho"));
        EstadoPedido estado   = EstadoPedido.valueOf(rs.getString("estado"));

        // busca adicionais em conexão separada para evitar conflito de ResultSets
        List<Adicional> adicionais = buscarAdicionaisDoPedido(pedidoId);

        Pedido pedido = new Pedido(cliente, pizza, adicionais, tamanho);
        pedido.setId(pedidoId);
        pedido.alterarEstado(estado); // corrige o estado para o valor salvo no banco
        return pedido;
    }

    // insere cada adicional do pedido na tabela de junção (reutiliza a conexão aberta)
    private void inserirAdicionais(Connection con, Pedido pedido) throws SQLException {
        String sql = "INSERT INTO tb_pedido_adicional (pedido_id, adicional_id) VALUES (?, ?)";
        for (Adicional adicional : pedido.getAdicionais()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, pedido.getId());
            ps.setLong(2, adicional.getId());
            ps.execute();
            ps.close();
        }
    }

    // busca os adicionais de um pedido via tabela de junção (abre conexão própria)
    private List<Adicional> buscarAdicionaisDoPedido(Long pedidoId) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT a.id, a.nome, a.valor " +
                     "FROM tb_pedido_adicional pa " +
                     "JOIN tb_adicional a ON pa.adicional_id = a.id " +
                     "WHERE pa.pedido_id = ?";
        List<Adicional> adicionais = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, pedidoId);
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
}
