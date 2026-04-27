import java.util.List;

public class Pedido {
    private Cliente cliente;
    private Pizza pizza;
    private List<Adicional> adicionais;
    private int qtdPizza;
    private EstadoPedido estado;

    public Pedido(Cliente cliente, Pizza pizza, List<Adicional> adicionais, int qtdPizza) {
        this.cliente = cliente;
        this.pizza = pizza;
        this.adicionais = adicionais;
        this.qtdPizza = qtdPizza;
        this.estado = EstadoPedido.PENDENTE;
    }
     public double calcularTotal() {
        double total = pizza.getValor() * qtdPizza;

        for (Adicional adc : adicionais) {
            total += adc.getValor();
        }

        return total;
     }

     public void alterarEstado(EstadoPedido estado) {
        this.estado = estado;
     }

     public Cliente getCliente() {
        return cliente;
     }

     public Pizza getPizza() {
        return pizza;
     }

     public List<Adicional> getAdicionais() {
        return adicionais;
     }

     public EstadoPedido getEstado() {
        return estado;
     }
}