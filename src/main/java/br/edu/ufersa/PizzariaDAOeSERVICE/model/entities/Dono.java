package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;

public class Dono extends Usuario {

    //Dono é sempre tipo dono então eu fixo
    public Dono(String nome, String senha) {
        super(nome, senha, "Dono");
    }

    //Lembrar de descomentar esse trecho antes de enviar pro git
    /*public Pizza cadastrarPizza(String tipo, double valor) {
        Pizza novaPizza = new Pizza(tipo, valor);
        System.out.println("A pizza do tipo " + tipo + " foi cadastrada com o valor de R$ " + valor);
        return novaPizza;
    }*/
}
