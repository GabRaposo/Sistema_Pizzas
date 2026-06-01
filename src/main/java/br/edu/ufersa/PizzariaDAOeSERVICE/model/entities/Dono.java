package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;

public class Dono extends Usuario {

    public Dono(String nome, String senha) {
        super(nome, senha, "Dono");
    }

    public Pizza cadastrarPizza(String tipo, double valor) {
        Pizza novaPizza = new Pizza(tipo, valor);
        System.out.println("A pizza do tipo " + tipo + " foi cadastrada com o valor de R$ " + valor);
        return novaPizza;
    }
}
