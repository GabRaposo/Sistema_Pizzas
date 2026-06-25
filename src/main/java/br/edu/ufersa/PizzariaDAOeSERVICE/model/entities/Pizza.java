package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

public class Pizza {
    private Long id; // adicionado para operações no banco
    private String tipo;
    private double valor;

    public Pizza(String tipo, double valor) {
        setTipo(tipo);
        setValor(valor);
    }

    // getters
    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public double getValor() { return valor; } // valor base = preço da pizza Grande

    // preço efetivo de acordo com o tamanho escolhido (Pequena/Média/Grande).
    // Resolve a precificação por tamanho sem precisar mudar a tela de
    // cadastro de pizza (que só tem um campo "Valor").
    public double getValorPorTamanho(TamanhoPizza tamanho) {
        if (tamanho == null) {
            throw new RuntimeException("Tamanho não pode ser nulo!");
        }
        return this.valor * tamanho.getMultiplicador();
    }

    // setters
    public void setId(Long id) {
        if (id > 0) {
            this.id = id;
        } else {
            throw new RuntimeException("Id inválido!");
        }
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
