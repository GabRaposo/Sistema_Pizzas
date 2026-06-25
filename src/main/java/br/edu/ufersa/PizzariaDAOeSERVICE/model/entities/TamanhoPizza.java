package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;


public enum TamanhoPizza {
    PEQUENA(0.70),
    MEDIA(0.85),
    GRANDE(1.00);

    private final double multiplicador;

    TamanhoPizza(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    public double getMultiplicador() {
        return multiplicador;
    }
}
