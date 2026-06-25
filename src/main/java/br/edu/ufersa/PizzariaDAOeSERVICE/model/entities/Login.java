package br.edu.ufersa.PizzariaDAOeSERVICE.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Login {
    private List<Usuario> usuarios; // lista de Funcionarios (e outros Usuarios)
    private List<Dono> donos;
    private Usuario usuarioLogado;

    public Login() {
        this.usuarios = new ArrayList<>();
        this.donos = new ArrayList<>();
        this.usuarioLogado = null; // ninguém está logado ainda
    }

    // métodos de cadastro
    public void cadastrarUsuario(Usuario u) {
        if (u != null) {
            this.usuarios.add(u);
        }
    }

    public void cadastrarDono(Dono d) {
        if (d != null) {
            this.donos.add(d);
        }
    }

    public boolean autenticar(String nome, String senha) {
        // procurando na lista de usuarios (Funcionarios, etc.)
        for (Usuario u : usuarios) {
            if (u.getNome().equals(nome) && u.getSenha().equals(senha)) {
                this.usuarioLogado = u;
                return true;
            }
        }

        //procurando na lista de donos
        for (Dono d : donos) {
            if (d.getNome().equals(nome) && d.getSenha().equals(senha)) {
                this.usuarioLogado = d; // Dono É UM Usuario (herança)
                return true;
            }
        }

        return false;
    }

    // logout
    public void logout() {
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
