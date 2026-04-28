public class Adicional {
    private String nome;
    private double valor;

    public Adicional(String nome, double valor) {
        setNome(nome);
        setValor(valor);
    }

    // Para impedir nome vazio e valor negativo
    public void setNome(String nome) {
        this.nome = (nome == null || nome.isEmpty()) ? "Adicional Comum" : nome;
    }

    public void setValor(double valor) {
        this.valor = (valor < 0) ? 0.0 : valor;
    }

    public String getNome() { return nome; }
    public double getValor() { return valor; }
}