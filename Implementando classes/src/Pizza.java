public class Pizza {
    private String tipo;
    private double valor;

    public Pizza(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String setTipo(String tipo) {
        this.tipo = tipo;
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public void getValor(double valor) {
        this.valor = valor;
    }
}