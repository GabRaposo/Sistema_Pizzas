package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ClienteListController implements Initializable {

    @FXML private TextField campoNome;
    @FXML private TextField campoCpf;
    @FXML private TextField campoEndereco;
    @FXML private Label labelErro;
    @FXML private Label labelTituloLista;
    @FXML private ListView<Cliente> listaClientes;

    private boolean modoSelecao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modoSelecao = NavigationManager.getInstance().isModoSelecionarCliente();
        labelTituloLista.setText(modoSelecao ? "Escolha um cliente para o pedido:" : "Clientes");

        listaClientes.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente cliente, boolean vazio) {
                super.updateItem(cliente, vazio);
                if (vazio || cliente == null) {
                    setGraphic(null);
                    return;
                }
                Label info = new Label(
                        "Nome: " + cliente.getNome() + "   cpf: " + cliente.getCpf() +
                        "\nEndereço: " + cliente.getEndereco()
                );
                Region espaco = new Region();
                HBox.setHgrow(espaco, Priority.ALWAYS);

                Button botaoAcao = new Button(modoSelecao ? "Selecionar" : "Editar");
                botaoAcao.setOnAction(e -> {
                    if (modoSelecao) {
                        NavigationManager.getInstance().selecionarClienteParaPedido(cliente);
                    } else {
                        NavigationManager.getInstance().irParaEditarCliente(cliente);
                    }
                });

                HBox linha = new HBox(10, info, espaco, botaoAcao);
                linha.setAlignment(Pos.CENTER_LEFT);
                setGraphic(linha);
            }
        });

        carregarLista();
    }

    private void carregarLista() {
        listaClientes.getItems().setAll(NavigationManager.getInstance().getFacade().listarClientes());
    }

    @FXML
    private void cadastrar() {
        labelErro.setText("");
        try {
            NavigationManager.getInstance().getFacade().cadastrarCliente(
                    campoNome.getText(), campoCpf.getText(), campoEndereco.getText());
            campoNome.clear();
            campoCpf.clear();
            campoEndereco.clear();
            carregarLista();
        } catch (RuntimeException e) {
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
