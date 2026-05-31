package br.edu.ufersa.PizzariaDAOeSERVICE.model.Entities;
public class Pizza {
    private String tipo;
    private double valor;

    public Pizza(String tipo, double valor) {
        setTipo(tipo);
        setValor(valor);
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isEmpty()) {
            this.tipo = "Tipo não informado";
        } else {
            this.tipo = tipo;
        }
    }

    public void setValor(double valor) {
        if (valor < 0) {
            throw new RuntimeException("Valor da pizza não pode ser negativo!");
        }
        this.valor = valor;
    }
}