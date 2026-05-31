package br.edu.ufersa.PizzariaDAOeSERVICE.Exceptions;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}