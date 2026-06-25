package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.EstadoPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Relatorio;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class RelatorioController implements Initializable {

    @FXML private DatePicker campoDataInicio;
    @FXML private DatePicker campoDataFim;
    @FXML private ComboBox<String> comboEstado;
    @FXML private ComboBox<String> comboPizza;
    @FXML private TextField campoCpf;
    @FXML private Label labelErro;

    @FXML private Label labelTotalPedidos;
    @FXML private Label labelTotalVendido;
    @FXML private Label labelTicketMedio;
    @FXML private ListView<Pedido> listaPedidos;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        campoDataInicio.setValue(LocalDate.now().minusDays(30));
        campoDataFim.setValue(LocalDate.now());

        comboEstado.getItems().add("Todos");
        for (EstadoPedido estado : EstadoPedido.values()) {
            comboEstado.getItems().add(estado.name());
        }
        comboEstado.setValue("Todos");

        comboPizza.getItems().add("Todas");
        NavigationManager.getInstance().getFacade().listarPizzas()
                .forEach(p -> comboPizza.getItems().add(p.getTipo()));
        comboPizza.setValue("Todas");

        listaPedidos.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Pedido pedido, boolean vazio) {
                super.updateItem(pedido, vazio);
                if (vazio || pedido == null) {
                    setText(null);
                } else {
                    setText("Pedido #" + pedido.getId() + " — " + pedido.getCliente().getNome() +
                            "   " + pedido.getDataHora().format(FORMATO_DATA) +
                            "   " + pedido.getModoEntrega() + " · " + pedido.getFormaPagamento() +
                            "   [" + pedido.getEstado() + "]" +
                            "   R$ " + String.format("%.2f", pedido.calcularTotal()));
                }
            }
        });
    }

    @FXML
    private void gerarRelatorio() {
        labelErro.setText("");

        LocalDate inicio = campoDataInicio.getValue();
        LocalDate fim = campoDataFim.getValue();
        if (inicio == null || fim == null) {
            labelErro.setText("Informe data início e data fim.");
            return;
        }

        try {
            Relatorio relatorio = NavigationManager.getInstance().getFacade().gerarRelatorio(inicio, fim);

            // a entidade Relatorio expõe um filtro por critério (filtrarPorEstado,
            // filtrarPorPizza, filtrarPorCliente), mas cada um deles parte sempre do
            // período inteiro — combinar vários filtros ao mesmo tempo (como esta tela
            // permite) é feito aqui na View, em cima da lista já filtrada por período.
            List<Pedido> resultado = relatorio.filtrarPorPeriodo();

            String estadoSelecionado = comboEstado.getValue();
            if (estadoSelecionado != null && !estadoSelecionado.equals("Todos")) {
                EstadoPedido estado = EstadoPedido.valueOf(estadoSelecionado);
                resultado = resultado.stream().filter(p -> p.getEstado() == estado).collect(Collectors.toList());
            }

            String pizzaSelecionada = comboPizza.getValue();
            if (pizzaSelecionada != null && !pizzaSelecionada.equals("Todas")) {
                resultado = resultado.stream()
                        .filter(p -> p.getItens().stream()
                                .anyMatch(item -> item.getPizza().getTipo().equals(pizzaSelecionada)))
                        .collect(Collectors.toList());
            }

            String cpf = campoCpf.getText();
            if (cpf != null && !cpf.isBlank()) {
                resultado = resultado.stream()
                        .filter(p -> p.getCliente().getCpf().equals(cpf))
                        .collect(Collectors.toList());
            }

            listaPedidos.getItems().setAll(resultado);

            double totalVendido = resultado.stream().mapToDouble(Pedido::calcularTotal).sum();
            labelTotalPedidos.setText(String.valueOf(resultado.size()));
            labelTotalVendido.setText("R$ " + String.format("%.2f", totalVendido));
            labelTicketMedio.setText("R$ " + String.format("%.2f",
                    resultado.isEmpty() ? 0.0 : totalVendido / resultado.size()));
        } catch (RuntimeException e) {
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
