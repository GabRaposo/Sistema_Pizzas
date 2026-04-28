public class Dono {
    private String nome;
    private String senha;

    public Dono(String nome, String senha){
        setNomeDono(nome);
        setSenha(senha);
    }

    //getters
    public String getNome(){
        return nome;
    }
    public String getSenha(){
        return senha;
    }

    //setters
    public void setNomeDono(String nome){
        if (nome == null || nome.isEmpty()) {
            this.nome = "Sr. Michelangelo"; // Valor padrão pro dono
        } else {
            this.nome = nome;
        }
    }
    public void setSenha(String senha){
        if (senha == null || senha.isEmpty()) {
            this.senha = "SenhaGenerica123";
        } else {
            this.senha = senha;
        }
    }

    //Metodos do dono
    public Pizza cadastrarNovaPizza(String tipo, double valor){
        Pizza novaPizza = new Pizza(tipo, valor);

        System.out.println("A pizza do tipo " + tipo+ " foi cadastrada com o valor de R$ " +valor);

        return novaPizza;
    }
}
