package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Estoque;
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

public class EstoqueAdicionaisController implements Initializable {

    @FXML private TextField campoNome;
    @FXML private TextField campoValor;
    @FXML private TextField campoQuantidade;
    @FXML private Label labelErroCadastro;

    @FXML private ListView<Estoque> listaEstoque;
    @FXML private Label labelErroLista;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaEstoque.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Estoque estoque, boolean vazio) {
                super.updateItem(estoque, vazio);
                if (vazio || estoque == null) {
                    setGraphic(null);
                    return;
                }
                Label info = new Label(
                        estoque.getAdicional().getNome() + "   Qtd: " + estoque.getQuantidade() +
                        "   R$ " + String.format("%.2f", estoque.getAdicional().getValor())
                );
                Region espaco = new Region();
                HBox.setHgrow(espaco, Priority.ALWAYS);

                TextField campoIncremento = new TextField();
                campoIncremento.setPromptText("+ qtd");
                campoIncremento.setPrefWidth(70);

                Button botaoRepor = new Button("Repor");
                botaoRepor.setOnAction(e -> reporEstoque(estoque, campoIncremento.getText()));

                Button botaoExcluir = new Button("X");
                botaoExcluir.setStyle("-fx-background-color: #e24b4a; -fx-text-fill: white; -fx-background-radius: 50%;");
                botaoExcluir.setOnAction(e -> excluirAdicional(estoque));

                HBox linha = new HBox(8, info, espaco, campoIncremento, botaoRepor, botaoExcluir);
                linha.setAlignment(Pos.CENTER_LEFT);
                setGraphic(linha);
            }
        });
        carregarLista();
    }

    private void carregarLista() {
        listaEstoque.getItems().setAll(NavigationManager.getInstance().getFacade().listarEstoque());
    }

    private void reporEstoque(Estoque estoque, String texto) {
        labelErroLista.setText("");
        try {
            int qtd = Integer.parseInt(texto);
            NavigationManager.getInstance().getFacade().atualizarEstoque(estoque.getAdicional(), qtd);
            carregarLista();
        } catch (NumberFormatException e) {
            labelErroLista.setText("Informe uma quantidade numérica válida pra repor.");
        } catch (RuntimeException e) {
            labelErroLista.setText(e.getMessage());
        }
    }

    private void excluirAdicional(Estoque estoque) {
        labelErroLista.setText("");
        try {
            // bloqueado pelo AdicionalService se esse adicional já foi usado em algum pedido
            NavigationManager.getInstance().getFacade().removerAdicional(estoque.getAdicional());
            carregarLista();
        } catch (RuntimeException e) {
            labelErroLista.setText(e.getMessage());
        }
    }

    @FXML
    private void confirmarCadastro() {
        labelErroCadastro.setText("");
        try {
            double valor = Double.parseDouble(campoValor.getText().replace(",", "."));
            int quantidade = Integer.parseInt(campoQuantidade.getText());
            NavigationManager.getInstance().getFacade()
                    .cadastrarAdicionalComEstoque(campoNome.getText(), valor, quantidade);
            campoNome.clear();
            campoValor.clear();
            campoQuantidade.clear();
            carregarLista();
        } catch (NumberFormatException e) {
            labelErroCadastro.setText("Valor e quantidade precisam ser números válidos.");
        } catch (RuntimeException e) {
            labelErroCadastro.setText(e.getMessage());
        }
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
