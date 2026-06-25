package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;
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

public class CadastrarPizzaController implements Initializable {

    @FXML private ListView<Pizza> listaPizzas;
    @FXML private TextField campoNome;
    @FXML private TextField campoValor;
    @FXML private Label labelErro;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaPizzas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Pizza pizza, boolean vazio) {
                super.updateItem(pizza, vazio);
                if (vazio || pizza == null) {
                    setGraphic(null);
                    return;
                }
                Label info = new Label(pizza.getTipo() + "   R$ " + String.format("%.2f", pizza.getValor()));
                Region espaco = new Region();
                HBox.setHgrow(espaco, Priority.ALWAYS);

                Button botaoExcluir = new Button("X");
                botaoExcluir.setStyle("-fx-background-color: #e24b4a; -fx-text-fill: white; -fx-background-radius: 50%;");
                botaoExcluir.setOnAction(e -> excluir(pizza));

                HBox linha = new HBox(10, info, espaco, botaoExcluir);
                linha.setAlignment(Pos.CENTER_LEFT);
                setGraphic(linha);
            }
        });
        carregarLista();
    }

    private void carregarLista() {
        //a Facade só expõe Pizza através do PizzaServiceProxy. Quem está logado aqui já é o
        // Dono (a tela só é acessível por ele), então a listagem
        //(operação de leitura) é liberada sem restrição
        listaPizzas.getItems().setAll(NavigationManager.getInstance().getFacade().listarPizzas());
    }

    private void excluir(Pizza pizza) {
        labelErro.setText("");
        try {
            //Proxy confirmando de novo que quem está logado é o Dono antes de delegar
            NavigationManager.getInstance().getFacade().removerPizza(pizza);
            carregarLista();
        } catch (RuntimeException e) {
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void confirmar() {
        labelErro.setText("");
        try {
            double valor = Double.parseDouble(campoValor.getText().replace(",", "."));
            NavigationManager.getInstance().getFacade().cadastrarPizza(campoNome.getText(), valor);
            campoNome.clear();
            campoValor.clear();
            carregarLista();
        } catch (NumberFormatException e) {
            labelErro.setText("Informe um valor numérico válido.");
        } catch (RuntimeException e) {
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
