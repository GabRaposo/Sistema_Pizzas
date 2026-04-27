import java.util.ArrayList;
import java.util.List;

public class Login {
    private List<Usuario> usuarios;
    private List<Dono> donos;
    private Usuario usuarioLogado;

    public Login(){
        this.usuarios = new ArrayList<>();
        this.donos = new ArrayList<>();
        this.usuarioLogado = null; //ninguem esta logado ainda
    }

    //metodo para cadastro
    public void cadastrarUsuario(Usuario u){
        if (u != null){
            this.usuarios.add(u);
        }
    }
    public void cadastrarDono(Dono d){
        if (d != null){
            this.donos.add(d);
        }
    }

    public boolean autenticar(String nome, String senha){
        //procurando na lista de funcionarios
        for (Usuario u : usuarios){
            if (u.getNome().equals(nome) && u.getSenha().equals(senha)){
                this.usuarioLogado = u;
                return true;
            }
        }
        //se nao estiver na lista de funcionarios vamos procurar na lista de donos
        for (Dono d: donos){
            if (d.getNome().equals(nome) && d.getSenha().equals(senha)){
                Usuario uTemp = new Usuario(d.getNome(), d.getSenha(), "Dono");
                this.usuarioLogado = uTemp;
                return true;
            }
        }

        return false; //so vai chegar aq se os laços falharem
    }

    //logout
    public void logout(){
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}