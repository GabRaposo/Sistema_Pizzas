package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;


public class Adicional {
    private Long id; // adicionado para operações no banco
    private String nome;
    private double valor;

    public Adicional(String nome, double valor) {
        setNome(nome);
        setValor(valor);
    }

    // getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public double getValor() { return valor; }

    // setters
    public void setId(Long id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new RuntimeException("Id inválido!");
        }
    }

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            this.nome = "Nome não informado";
        } else {
            this.nome = nome;
        }
    }

    public void setValor(double valor) {
        if (valor < 0) {
            throw new RuntimeException("Valor do adicional não pode ser negativo!");
        }
        this.valor = valor;
    }
}
