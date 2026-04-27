public class Cliente {
    private String nome;
    private String cpf;
    private String endereco;

    public Cliente(String nome, String cpf, String endereco){
        setNomeCliente(nome);
        setCpf(cpf);
        setEndereco(endereco);
    }

    //getters
    public String getNome(){
        return nome;
    }
    public String getSenha(){
        return cpf;
    }
    public String getTipo(){
        return endereco;
    }

    //setters
    public void setNomeCliente(String nome){
        if (nome == null || nome.isEmpty()){
            this.nome = "Nome desconhecido";
        }else{
            this.nome = nome;
        }
    }
    public void setCpf(String cpf){
        if (cpf == null || cpf.isEmpty()){
            this.cpf = "Nome desconhecido";
        }else{
            this.cpf = cpf;
        }
    }
    public void setEndereco(String endereco){
        if (nome == null || nome.isEmpty()){
            this.nome = "Nome desconhecido";
        }else{
            this.nome = nome;
        }
    }

}