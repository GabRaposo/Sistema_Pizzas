package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

public class Cliente {
    private Long id;   //pro banco de dados
    private String nome;
    private String cpf;
    private String endereco;

    public Cliente(String nome, String cpf, String endereco) {
        setNome(nome);
        setCpf(cpf);
        setEndereco(endereco);
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereco() {
        return endereco;
    }

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
            this.nome = "Nome desconhecido";
        } else {
            this.nome = nome;
        }
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            this.cpf = "CPF desconhecido";
        } else {
            this.cpf = cpf;
        }
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.isEmpty()) {
            this.endereco = "Endereço desconhecido";
        } else {
            this.endereco = endereco;
        }
    }
}
