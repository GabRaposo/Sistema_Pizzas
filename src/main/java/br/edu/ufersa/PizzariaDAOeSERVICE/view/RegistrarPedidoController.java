package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.net.URL;
import java.util.ResourceBundle;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Adicional;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Carrinho;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Estoque;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.ItemPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.TamanhoPizza;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class RegistrarPedidoController implements Initializable {

    @FXML private ListView<Pizza> listaPizzas;
    @FXML private RadioButton radioPequena;
    @FXML private RadioButton radioMedia;
    @FXML private RadioButton radioGrande;
    @FXML private Spinner<Integer> spinnerQuantidadePizza;
    @FXML private Label labelErroPizza;

    @FXML private ListView<ItemPedido> listaCarrinho;
    @FXML private Label labelItemSelecionado;
    @FXML private ListView<Adicional> listaAdicionais;
    @FXML private Spinner<Integer> spinnerQuantidadeAdicional;
    @FXML private Label labelErroAdicional;

    @FXML private Label labelTotal;

    private Carrinho carrinho;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carrinho = NavigationManager.getInstance().getCarrinhoAtual();

        spinnerQuantidadePizza.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
        spinnerQuantidadeAdicional.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));

        listaPizzas.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Pizza pizza, boolean vazio) {
                super.updateItem(pizza, vazio);
                setText(vazio || pizza == null ? null :
                        pizza.getTipo() + " — R$ " + String.format("%.2f", pizza.getValor()) + " (Grande)");
            }
        });
        listaPizzas.getItems().addAll(NavigationManager.getInstance().getFacade().listarPizzas());

        listaAdicionais.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Adicional adicional, boolean vazio) {
                super.updateItem(adicional, vazio);
                setText(vazio || adicional == null ? null :
                        adicional.getNome() + " — R$ " + String.format("%.2f", adicional.getValor()));
            }
        });
        listaAdicionais.getItems().addAll(
                NavigationManager.getInstance().getFacade().listarEstoque().stream()
                        .map(Estoque::getAdicional).toList()
        );

        listaCarrinho.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(ItemPedido item, boolean vazio) {
                super.updateItem(item, vazio);
                setText(vazio || item == null ? null : descreverItem(item));
            }
        });

        listaCarrinho.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            labelItemSelecionado.setText(novo == null
                    ? "Selecione uma pizza do carrinho para adicionar extras"
                    : "Adicionando extras em: " + descreverItem(novo));
        });
    }

    private String descreverItem(ItemPedido item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getQuantidade()).append("x ").append(item.getPizza().getTipo())
          .append(" (").append(item.getTamanho()).append(")");
        if (!item.getAdicionais().isEmpty()) {
            sb.append(" + ").append(item.getAdicionais().size()).append(" adicional(is)");
        }
        sb.append(" — R$ ").append(String.format("%.2f", item.calcularTotal()));
        return sb.toString();
    }

    @FXML
    private void adicionarPizzaAoCarrinho() {
        labelErroPizza.setText("");
        Pizza pizza = listaPizzas.getSelectionModel().getSelectedItem();
        if (pizza == null) {
            labelErroPizza.setText("Selecione uma pizza na lista.");
            return;
        }
        TamanhoPizza tamanho = radioPequena.isSelected() ? TamanhoPizza.PEQUENA
                : radioMedia.isSelected() ? TamanhoPizza.MEDIA
                : TamanhoPizza.GRANDE;
        int quantidade = spinnerQuantidadePizza.getValue();

        try {
            ItemPedido item = NavigationManager.getInstance().getFacade()
                    .adicionarPizzaAoCarrinho(carrinho, pizza, tamanho, quantidade);
            listaCarrinho.getItems().add(item);
            atualizarTotal();
        } catch (RuntimeException e) {
            labelErroPizza.setText(e.getMessage());
        }
    }

    @FXML
    private void adicionarAdicionalAoItem() {
        labelErroAdicional.setText("");
        ItemPedido item = listaCarrinho.getSelectionModel().getSelectedItem();
        Adicional adicional = listaAdicionais.getSelectionModel().getSelectedItem();

        if (item == null) {
            labelErroAdicional.setText("Selecione uma pizza do carrinho primeiro.");
            return;
        }
        if (adicional == null) {
            labelErroAdicional.setText("Selecione um adicional.");
            return;
        }

        try {
            NavigationManager.getInstance().getFacade()
                    .adicionarAdicionalAoCarrinho(carrinho, item, adicional, spinnerQuantidadeAdicional.getValue());
            listaCarrinho.refresh(); // o texto do item mudou (novo total, nova contagem de adicionais)
            atualizarTotal();
        } catch (RuntimeException e) {
            labelErroAdicional.setText(e.getMessage());
        }
    }

    private void atualizarTotal() {
        labelTotal.setText("Total: R$ " + String.format("%.2f", carrinho.calcularTotal()));
    }

    @FXML
    private void prosseguir() {
        if (carrinho.getItens().isEmpty()) {
            labelErroPizza.setText("Adicione ao menos uma pizza antes de prosseguir.");
            return;
        }
        NavigationManager.getInstance().irParaConfirmarPedido();
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
