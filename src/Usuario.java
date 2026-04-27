public class Usuario {
    private String nome;
    private String senha;
    private String tipo;

    //construtor
    public Usuario(String nome, String senha, String tipo){
        setNome(nome);
        setSenha(senha);
        setTipo(tipo);
    }

    //getters
    public String getNome(){
        return nome;
    }
    public String getSenha(){
        return senha;
    }
    public String getTipo(){
        return tipo;
    }

    //setters
    public void setNome(String nome){
        if(nome == null || nome.isEmpty()){
            this.nome = "Nome não informado";
        }else{
            this.nome = nome;
        }
    }
    public void setSenha(String senha){
        if(senha == null || senha.isEmpty()){
            this.senha = "SenhaGenerica123";
        }else{
            this.senha = senha;
        }
    }
    public void setTipo(String tipo){
        if(tipo == null || tipo.isEmpty()){
            this.tipo = "Funcionario";
        }else{
            this.tipo = tipo;
        }
    }
}
