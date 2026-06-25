package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.*;


public class PedidoDAO implements BaseDAO<Pedido> {

    // insere o pedido (cabeçalho), depois cada item e os adicionais de cada item — tudo na mesma conexão
    public Pedido inserir(Pedido entity) {
        Connection con = BaseDAO.getConnection();
        try {
            String sqlPedido = "INSERT INTO tb_pedido (cliente_id, estado, modo_entrega, forma_pagamento, data_hora) " +
                                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entity.getCliente().getId());
            ps.setString(2, entity.getEstado().name());
            ps.setString(3, entity.getModoEntrega().name());
            ps.setString(4, entity.getFormaPagamento().name());
            ps.setTimestamp(5, Timestamp.valueOf(entity.getDataHora()));
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
            ps.close();

            for (ItemPedido item : entity.getItens()) {
                inserirItem(con, entity.getId(), item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return entity;
    }

    // deletar o pedido inteiro: adicionais de cada item -> itens -> pedido (ordem por causa das FKs)
    public void deletar(Pedido entity) {
        Connection con = BaseDAO.getConnection();
        try {
            String sqlAdicionais = "DELETE FROM tb_item_pedido_adicional WHERE item_pedido_id IN " +
                                    "(SELECT id FROM tb_item_pedido WHERE pedido_id = ?)";
            PreparedStatement ps1 = con.prepareStatement(sqlAdicionais);
            ps1.setLong(1, entity.getId());
            ps1.execute();
            ps1.close();

            String sqlItens = "DELETE FROM tb_item_pedido WHERE pedido_id = ?";
            PreparedStatement ps2 = con.prepareStatement(sqlItens);
            ps2.setLong(1, entity.getId());
            ps2.execute();
            ps2.close();

            String sqlPedido = "DELETE FROM tb_pedido WHERE id = ?";
            PreparedStatement ps3 = con.prepareStatement(sqlPedido);
            ps3.setLong(1, entity.getId());
            ps3.execute();
            ps3.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
    }

    //atualiza apenas o estado (os itens, entrega e pagamento são imutáveis depois que o pedido é criado)
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

    //busca por estado
    public List<Pedido> buscar(String param) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.estado, p.modo_entrega, p.forma_pagamento, p.data_hora, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco " +
                     "FROM tb_pedido p JOIN tb_cliente c ON p.cliente_id = c.id " +
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

    //listar todos os pedidos
    public List<Pedido> listar() {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.estado, p.modo_entrega, p.forma_pagamento, p.data_hora, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco " +
                     "FROM tb_pedido p JOIN tb_cliente c ON p.cliente_id = c.id";
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

    //buscar pedido pelo id
    public Pedido buscarPorId(Long id) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.estado, p.modo_entrega, p.forma_pagamento, p.data_hora, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco " +
                     "FROM tb_pedido p JOIN tb_cliente c ON p.cliente_id = c.id " +
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

    //busca todos os pedidos de um cliente pelo CPF
    public List<Pedido> buscarPorCliente(String cpf) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.estado, p.modo_entrega, p.forma_pagamento, p.data_hora, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco " +
                     "FROM tb_pedido p JOIN tb_cliente c ON p.cliente_id = c.id " +
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

    //busca todos os pedidos que contenham determinado tipo de pizza em algum item
    //(item b do enunciado: "buscar Pedidos por Cliente, por Pizza, por estado")
    public List<Pedido> buscarPorPizza(Long pizzaId) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT DISTINCT p.id, p.estado, p.modo_entrega, p.forma_pagamento, p.data_hora, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco " +
                     "FROM tb_pedido p " +
                     "JOIN tb_cliente c ON p.cliente_id = c.id " +
                     "JOIN tb_item_pedido ip ON ip.pedido_id = p.id " +
                     "WHERE ip.pizza_id = ?";
        List<Pedido> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, pizzaId);
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

    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT p.id, p.estado, p.modo_entrega, p.forma_pagamento, p.data_hora, " +
                     "c.id AS cliente_id, c.nome AS cliente_nome, c.cpf, c.endereco " +
                     "FROM tb_pedido p JOIN tb_cliente c ON p.cliente_id = c.id " +
                     "WHERE p.data_hora BETWEEN ? AND ?";
        List<Pedido> pedidos = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fim));
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


    public boolean existePedidoComPizza(Long pizzaId) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT 1 FROM tb_item_pedido WHERE pizza_id = ? LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, pizzaId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ps.close();
            return existe;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return false;
    }

    public boolean existePedidoComAdicional(Long adicionalId) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT 1 FROM tb_item_pedido_adicional WHERE adicional_id = ? LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, adicionalId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ps.close();
            return existe;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return false;
    }

    public boolean existePedidoComCliente(Long clienteId) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT 1 FROM tb_pedido WHERE cliente_id = ? LIMIT 1";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, clienteId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ps.close();
            return existe;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return false;
    }

    private Pedido montarPedido(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
                rs.getString("cliente_nome"),
                rs.getString("cpf"),
                rs.getString("endereco")
        );
        cliente.setId(rs.getLong("cliente_id"));

        long pedidoId = rs.getLong("id");
        EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));
        ModoEntrega modoEntrega = ModoEntrega.valueOf(rs.getString("modo_entrega"));
        FormaPagamento formaPagamento = FormaPagamento.valueOf(rs.getString("forma_pagamento"));

        List<ItemPedido> itens = buscarItensDoPedido(pedidoId);

        Pedido pedido = new Pedido(cliente, itens, modoEntrega, formaPagamento);
        pedido.setId(pedidoId);
        pedido.alterarEstado(estado); // corrige o estado para o valor salvo no banco
        pedido.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime()); // corrige pra data/hora original
        return pedido;
    }

    private void inserirItem(Connection con, Long pedidoId, ItemPedido item) throws SQLException {
        String sql = "INSERT INTO tb_item_pedido (pedido_id, pizza_id, tamanho, quantidade) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, pedidoId);
        ps.setLong(2, item.getPizza().getId());
        ps.setString(3, item.getTamanho().name());
        ps.setInt(4, item.getQuantidade());
        ps.execute();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            item.setId(rs.getLong(1));
        }
        ps.close();

        String sqlAdicional = "INSERT INTO tb_item_pedido_adicional (item_pedido_id, adicional_id) VALUES (?, ?)";
        for (Adicional adicional : item.getAdicionais()) {
            PreparedStatement psAdicional = con.prepareStatement(sqlAdicional);
            psAdicional.setLong(1, item.getId());
            psAdicional.setLong(2, adicional.getId());
            psAdicional.execute();
            psAdicional.close();
        }
    }

    private List<ItemPedido> buscarItensDoPedido(Long pedidoId) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT ip.id, ip.tamanho, ip.quantidade, pz.id AS pizza_id, pz.tipo, pz.valor " +
                     "FROM tb_item_pedido ip " +
                     "JOIN tb_pizza pz ON ip.pizza_id = pz.id " +
                     "WHERE ip.pedido_id = ?";
        List<ItemPedido> itens = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, pedidoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pizza pizza = new Pizza(rs.getString("tipo"), rs.getDouble("valor"));
                pizza.setId(rs.getLong("pizza_id"));

                TamanhoPizza tamanho = TamanhoPizza.valueOf(rs.getString("tamanho"));
                int quantidade = rs.getInt("quantidade");
                long itemId = rs.getLong("id");

                ItemPedido item = new ItemPedido(pizza, tamanho, quantidade);
                item.setId(itemId);

                // cada adicional encontrado já representa 1 unidade (convenção da tabela de junção)
                for (Adicional adicional : buscarAdicionaisDoItem(itemId)) {
                    item.adicionarAdicional(adicional, 1);
                }

                itens.add(item);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDAO.closeConnection(con);
        }
        return itens;
    }

    private List<Adicional> buscarAdicionaisDoItem(Long itemPedidoId) {
        Connection con = BaseDAO.getConnection();
        String sql = "SELECT a.id, a.nome, a.valor " +
                     "FROM tb_item_pedido_adicional ipa " +
                     "JOIN tb_adicional a ON ipa.adicional_id = a.id " +
                     "WHERE ipa.item_pedido_id = ?";
        List<Adicional> adicionais = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, itemPedidoId);
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
