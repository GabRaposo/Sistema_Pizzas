package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

public class Usuario {
    private Long id;        //necessário para identificar o registro no banco
    private String nome;
    private String email;
    private String senha;
    private String tipo;

    // construtor
    public Usuario(String nome, String email, String senha, String tipo) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setTipo(tipo);
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipo() {
        return tipo;
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
            this.nome = "Nome não informado";
        } else {
            this.nome = nome;
        }
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new RuntimeException("E-mail inválido!");
        }
        this.email = email;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            this.senha = "SenhaGenerica123";
        } else {
            this.senha = senha;
        }
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isEmpty()) {
            this.tipo = "Funcionario";
        } else {
            this.tipo = tipo;
        }
    }
}
