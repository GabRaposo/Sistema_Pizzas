package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Dono;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML private Label labelTipoUsuario;
    @FXML private Button botaoPizzasRegistradas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Usuario usuario = NavigationManager.getInstance().getFacade().usuarioLogado();
        boolean isDono = usuario instanceof Dono;

        labelTipoUsuario.setText(isDono ? "Dono" : "Funcionário");

        //só o Dono pode cadastrar/editar/excluir tipos de pizza
        botaoPizzasRegistradas.setVisible(isDono);
        botaoPizzasRegistradas.setManaged(isDono);
    }

    @FXML
    private void irParaRegistrarPedido() {
        NavigationManager.getInstance().irParaRegistrarPedido();
    }

    @FXML
    private void irParaPizzas() {
        NavigationManager.getInstance().irParaCadastrarPizza();
    }

    @FXML
    private void irParaClientes() {
        NavigationManager.getInstance().irParaClientes(false);
    }

    @FXML
    private void irParaEstoque() {
        NavigationManager.getInstance().irParaEstoqueAdicionais();
    }

    @FXML
    private void irParaHistorico() {
        NavigationManager.getInstance().irParaAcompanharPedidos();
    }

    @FXML
    private void irParaRelatorio() {
        NavigationManager.getInstance().irParaRelatorio();
    }

    @FXML
    private void irParaBusca() {
        NavigationManager.getInstance().irParaBusca();
    }
}
