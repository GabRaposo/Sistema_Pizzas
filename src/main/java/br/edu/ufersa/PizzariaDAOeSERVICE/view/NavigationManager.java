package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.io.IOException;

import br.edu.ufersa.PizzariaDAOeSERVICE.facade.PizzariaFacade;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Carrinho;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class NavigationManager {

    private static final NavigationManager INSTANCE = new NavigationManager();

    private Stage primaryStage;
    private final PizzariaFacade facade = new PizzariaFacade();

    // estado de sessão que precisa atravessar telas
    private Carrinho carrinhoAtual;
    private Cliente clienteDoPedido;
    private boolean modoSelecionarCliente = false;

    private NavigationManager() {
    }

    public static NavigationManager getInstance() {
        return INSTANCE;
    }

    public void iniciar(Stage primaryStage) {
        this.primaryStage = primaryStage;
        irParaLogin();
        primaryStage.show();
    }

    public PizzariaFacade getFacade() {
        return facade;
    }

    // ---------------- carrega um FXML e troca a cena ----------------

    @SuppressWarnings("unchecked")
    private <T> T mostrar(String fxml, double largura, double altura) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/PizzariaDAOeSERVICE/view/" + fxml));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, largura, altura));
            return (T) loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar a tela: " + fxml, e);
        }
    }


    public void irParaLogin() {
        mostrar("login.fxml", 1024, 640);
    }

    public void irParaCadastro() {
        mostrar("cadastro.fxml", 1024, 640);
    }

    public void irParaHome() {
        mostrar("home.fxml", 1024, 640);
    }

    public void irParaRegistrarPedido() {
        carrinhoAtual = new Carrinho(null); // cliente só é definido na tela de Confirmar Pedido
        mostrar("registrar_pedido.fxml", 1100, 700);
    }

    public void irParaConfirmarPedido() {
        ConfirmarPedidoController c = mostrar("confirmar_pedido.fxml", 1024, 640);
        c.atualizarResumo();
    }

    public void irParaClientes(boolean modoSelecao) {
        this.modoSelecionarCliente = modoSelecao;
        mostrar("clientes.fxml", 1024, 640);
    }

    public void irParaEditarCliente(Cliente cliente) {
        ClienteEditController c = mostrar("cliente_editar.fxml", 700, 500);
        c.carregar(cliente);
    }

    public void irParaCadastrarPizza() {
        mostrar("cadastrar_pizza.fxml", 1024, 640);
    }

    public void irParaEstoqueAdicionais() {
        mostrar("estoque_adicionais.fxml", 1024, 700);
    }

    public void irParaAcompanharPedidos() {
        mostrar("acompanhar_pedidos.fxml", 1024, 700);
    }

    public void irParaRelatorio() {
        mostrar("relatorio.fxml", 1024, 700);
    }

    public void irParaBusca() {
        mostrar("busca.fxml", 1024, 700);
    }

    //estado sessão
    public Carrinho getCarrinhoAtual() {
        return carrinhoAtual;
    }

    public boolean isModoSelecionarCliente() {
        return modoSelecionarCliente;
    }

    public void selecionarClienteParaPedido(Cliente cliente) {
        this.clienteDoPedido = cliente;
        this.modoSelecionarCliente = false;
        irParaConfirmarPedido();
    }

    public Cliente getClienteDoPedido() {
        return clienteDoPedido;
    }
}
