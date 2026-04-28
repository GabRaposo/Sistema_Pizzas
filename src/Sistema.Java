import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private List<Cliente> clientes = new ArrayList<>();
    private List<Pizza> pizzas = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();
    private List<Estoque> estoques = new ArrayList<>();

    public void adicionarCliente(Cliente c) { if(c != null) clientes.add(c); }
    
    public void adicionarPizza(Pizza p) { if(p != null) pizzas.add(p); }

    public void adicionarPedido(Pedido p) {
        if(p != null) {
            pedidos.add(p);
            atualizarEstoque(p.getAdicionais());
        }
    }

    // Abate automático do estoque
    private void atualizarEstoque(List<Adicional> adcs) {
        for (Adicional a : adcs) {
            for (Estoque e : estoques) {
                if (e.getAdicional().equals(a)) {
                    e.remover(1);
                }
            }
        }
    }

    // Métodos de busca 
    public List<Pedido> buscarPedidosPorCliente(Cliente c) {
        List<Pedido> resultado = new ArrayList<>();
        for(Pedido p : pedidos) {
            if(p.getCliente().equals(c)) resultado.add(p);
        }
        return resultado;
    }

    public double calcularFaturamento() {
        double total = 0;
        for(Pedido p : pedidos) total += p.calcularTotal();
        return total;
    }
}