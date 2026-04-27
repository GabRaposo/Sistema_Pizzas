import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private Cliente cliente;
    private Pizza pizza;
    private int qtdPizza;

    private List<Adicional> adicionais;
    private List<Integer> qtdAdicionais;

    public Carrinho(Cliente cliente) {
        this.cliente = cliente;
        this.adicionais = new ArrayList<>();
        this.qtdAdicionais = new ArrayList<>();
    }

    public void adicionarPizza(Pizza pizza, int qtd) {
        this.pizza = pizza;
        this.qtdPizza = qtd;
    }

    public void adicionarAdicional(Adicional adicional, int qtd) {
        adicionais.add(adicional);
        qtdAdicionais.add(qtd);
    }
     public double calcularTotal() {
        double total = 0;

        if(pizza != null) {
            total += pizza.getValor() * qtdPizza;
        }

        for (int i = 0; i < adicionais.size(); i++) {
            total += adicionais.get(i).getValor() * qtdAdicionais.get(i);
        }

        return total;
     }

     public Pedido finalizarPedido() {
        List<Adicional> listaFinal = new ArrayList<>();

        for (int i = 0; i < adicionais.size(); i++) {
            for (int j = 0; j < qtdAdicionais.get(i); j++) {
                listaFinal.add(adicionais.get(i));
            }
        }

        return new Pedido(cliente, pizza, listaFinal, qtdPizza);
     }
}