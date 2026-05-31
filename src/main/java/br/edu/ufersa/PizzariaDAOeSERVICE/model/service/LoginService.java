package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;

public class LoginService {
    private UsuarioService usuarioService = new UsuarioService();
    private Usuario usuarioLogado;

    //metodo de autenticação
    public boolean autenticar (String nome, String senha){
        Usuario u = usuarioService.autenticar(nome, senha);
        if(u != null){
            this.usuarioLogado = u;
            return true;
        }
        return false;
    }

    //logout do sistema
    public void logout(){
        this.usuarioLogado = null;
    }

    //verificando se há alguem logado
    public boolean estaLogado(){
        return usuarioLogado != null;
    }

    //retornando usuario logado agora
    public Usuario getUsuarioLogado(){
        return usuarioLogado;
    }
}
