public class Estoque {
    private Adicional adicional;
    private int quantidade;

    public Estoque(Adicional adicional, int quantidade) {
        this.adicional = adicional;
        this.quantidade = quantidade;
    }

    public void adicionar(int qtd) {
        quantidade += qtd;
    }

    public void remover(int qtd) {
        if (quantidade >= qtd) {
            quantidade -= qtd;
        } else { 
            System.out.println("estoque insuficiente para " + adicional.getnome());
        }
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Adicional getAdicional() {
        return adicional;
    }
}