package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.EstadoPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pedido;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AcompanharPedidosController implements Initializable {

    @FXML private ListView<Pedido> listaPedidos;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaPedidos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Pedido pedido, boolean vazio) {
                super.updateItem(pedido, vazio);
                if (vazio || pedido == null) {
                    setGraphic(null);
                    return;
                }

                Label titulo = new Label("Pedido Nº " + pedido.getId() + " — " + pedido.getCliente().getNome());
                titulo.setStyle("-fx-font-size: 14px;");

                Label status = new Label("Status de Pedido - " + descreverEstado(pedido));
                status.setStyle("-fx-text-fill: #6b8e3d; -fx-font-size: 13px;");

                Label detalhes = new Label(
                        "Subtotal - R$ " + String.format("%.2f", pedido.calcularTotal()) +
                        "   Data - " + pedido.getDataHora().format(FORMATO_DATA) +
                        "   " + pedido.getModoEntrega() + " · " + pedido.getFormaPagamento()
                );
                detalhes.setStyle("-fx-font-size: 12px; -fx-text-fill: #5f5e5a;");

                VBox texto = new VBox(4, titulo, status, detalhes);

                Region espaco = new Region();
                HBox.setHgrow(espaco, Priority.ALWAYS);

                Button botaoAvancar = new Button("Avançar status");
                botaoAvancar.setStyle("-fx-background-color: #9b7a72; -fx-text-fill: white;");
                botaoAvancar.setDisable(pedido.getEstado() == EstadoPedido.ENTREGUE
                        || pedido.getEstado() == EstadoPedido.PAGO
                        || pedido.getEstado() == EstadoPedido.CANCELADO);
                botaoAvancar.setOnAction(e -> avancarStatus(pedido));

                HBox linha = new HBox(14, texto, espaco, botaoAvancar);
                linha.setAlignment(Pos.CENTER_LEFT);
                linha.setStyle("-fx-padding: 10; -fx-background-color: #f1efe8; -fx-background-radius: 8;");
                setGraphic(linha);
            }
        });
        carregarLista();
    }


    private String descreverEstado(Pedido pedido) {
        if (pedido.getEstado() == EstadoPedido.ENTREGUE) {
            return pedido.getModoEntrega().name().equals("RETIRADA")
                    ? "Retirada concluída" : "Entrega realizada com sucesso";
        }
        return pedido.getEstado().name();
    }

    private void carregarLista() {
        listaPedidos.getItems().setAll(NavigationManager.getInstance().getFacade().listarPedidos());
    }

    private void avancarStatus(Pedido pedido) {
        try {
            NavigationManager.getInstance().getFacade().avancarEstadoPedido(pedido);
            listaPedidos.refresh();
        } catch (RuntimeException e) {
            // ex: estoque insuficiente ao tentar avançar de PENDENTE -> EM_PREPARO
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
