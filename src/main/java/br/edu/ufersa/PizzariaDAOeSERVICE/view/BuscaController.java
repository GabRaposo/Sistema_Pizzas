package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import br.edu.ufersa.PizzariaDAOeSERVICE.facade.PizzariaFacade;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Adicional;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.EstadoPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class BuscaController implements Initializable {

    private static final String PIZZA_POR_TIPO      = "Pizza — por Tipo";
    private static final String PIZZA_POR_CLIENTE   = "Pizza — por Cliente (CPF)";
    private static final String ADICIONAL_POR_NOME  = "Adicional — por Nome";
    private static final String PEDIDO_POR_CLIENTE  = "Pedido — por Cliente (CPF)";
    private static final String PEDIDO_POR_PIZZA    = "Pedido — por Pizza (tipo)";
    private static final String PEDIDO_POR_ESTADO   = "Pedido — por Estado";

    @FXML private ComboBox<String> comboBusca;

    @FXML private Label      labelFiltro;
    @FXML private TextField  campoFiltro;
    @FXML private ComboBox<String> comboFiltroEstado;
    @FXML private ComboBox<String> comboFiltroPizza;

    @FXML private Label      labelErro;
    @FXML private Label      labelResultado;
    @FXML private ListView<String> listaResultado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBusca.getItems().addAll(
                PIZZA_POR_TIPO,
                PIZZA_POR_CLIENTE,
                ADICIONAL_POR_NOME,
                PEDIDO_POR_CLIENTE,
                PEDIDO_POR_PIZZA,
                PEDIDO_POR_ESTADO
        );
        comboBusca.setValue(PIZZA_POR_TIPO);

        // preenche combos secundários
        for (EstadoPedido e : EstadoPedido.values()) {
            comboFiltroEstado.getItems().add(e.name());
        }
        if (!comboFiltroEstado.getItems().isEmpty()) {
            comboFiltroEstado.setValue(comboFiltroEstado.getItems().get(0));
        }

        PizzariaFacade facade = NavigationManager.getInstance().getFacade();
        facade.listarPizzas().forEach(p -> comboFiltroPizza.getItems().add(p.getTipo()));
        if (!comboFiltroPizza.getItems().isEmpty()) {
            comboFiltroPizza.setValue(comboFiltroPizza.getItems().get(0));
        }

        atualizarFormulario();
    }

    @FXML
    private void atualizarFormulario() {
        String sel = comboBusca.getValue();
        if (sel == null) return;

        // reset visibility
        campoFiltro.setVisible(true);
        campoFiltro.setManaged(true);
        comboFiltroEstado.setVisible(false);
        comboFiltroEstado.setManaged(false);
        comboFiltroPizza.setVisible(false);
        comboFiltroPizza.setManaged(false);
        campoFiltro.clear();
        listaResultado.getItems().clear();
        labelErro.setText("");

        switch (sel) {
            case PIZZA_POR_TIPO:
                labelFiltro.setText("Tipo:");
                campoFiltro.setPromptText("ex: Margherita");
                break;
            case PIZZA_POR_CLIENTE:
                labelFiltro.setText("CPF do cliente:");
                campoFiltro.setPromptText("xxx.xxx.xxx-xx");
                break;
            case ADICIONAL_POR_NOME:
                labelFiltro.setText("Nome:");
                campoFiltro.setPromptText("ex: Queijo");
                break;
            case PEDIDO_POR_CLIENTE:
                labelFiltro.setText("CPF do cliente:");
                campoFiltro.setPromptText("xxx.xxx.xxx-xx");
                break;
            case PEDIDO_POR_PIZZA:
                labelFiltro.setText("Tipo de Pizza:");
                campoFiltro.setVisible(false);
                campoFiltro.setManaged(false);
                comboFiltroPizza.setVisible(true);
                comboFiltroPizza.setManaged(true);
                break;
            case PEDIDO_POR_ESTADO:
                labelFiltro.setText("Estado:");
                campoFiltro.setVisible(false);
                campoFiltro.setManaged(false);
                comboFiltroEstado.setVisible(true);
                comboFiltroEstado.setManaged(true);
                break;
        }
    }

    @FXML
    private void executarBusca() {
        labelErro.setText("");
        listaResultado.getItems().clear();

        String sel = comboBusca.getValue();
        PizzariaFacade facade = NavigationManager.getInstance().getFacade();

        try {
            switch (sel) {

                case PIZZA_POR_TIPO: {
                    String tipo = campoFiltro.getText().trim();
                    if (tipo.isEmpty()) { labelErro.setText("Informe o tipo da pizza."); return; }
                    List<Pizza> resultado = facade.buscarPizzasPorTipo(tipo);
                    if (resultado.isEmpty()) {
                        listaResultado.getItems().add("Nenhuma pizza encontrada para o tipo: " + tipo);
                    } else {
                        resultado.forEach(p -> listaResultado.getItems()
                                .add("🍕 " + p.getTipo() + " — R$ " + String.format("%.2f", p.getValor()) + " (G)"));
                    }
                    labelResultado.setText("Pizzas encontradas (" + resultado.size() + "):");
                    break;
                }

                case PIZZA_POR_CLIENTE: {
                    String cpf = campoFiltro.getText().trim();
                    if (cpf.isEmpty()) { labelErro.setText("Informe o CPF do cliente."); return; }
                    List<Pizza> resultado = facade.buscarPizzasPorCliente(cpf);
                    if (resultado.isEmpty()) {
                        listaResultado.getItems().add("Nenhuma pizza encontrada para o CPF: " + cpf);
                    } else {
                        resultado.forEach(p -> listaResultado.getItems()
                                .add("🍕 " + p.getTipo() + " — R$ " + String.format("%.2f", p.getValor()) + " (G)"));
                    }
                    labelResultado.setText("Pizzas pedidas pelo cliente (" + resultado.size() + "):");
                    break;
                }

                case ADICIONAL_POR_NOME: {
                    String nome = campoFiltro.getText().trim();
                    if (nome.isEmpty()) { labelErro.setText("Informe o nome do adicional."); return; }
                    List<Adicional> resultado = facade.buscarAdicionaisPorNome(nome);
                    if (resultado.isEmpty()) {
                        listaResultado.getItems().add("Nenhum adicional encontrado para: " + nome);
                    } else {
                        resultado.forEach(a -> listaResultado.getItems()
                                .add("➕ " + a.getNome() + " — R$ " + String.format("%.2f", a.getValor())));
                    }
                    labelResultado.setText("Adicionais encontrados (" + resultado.size() + "):");
                    break;
                }

                case PEDIDO_POR_CLIENTE: {
                    String cpf = campoFiltro.getText().trim();
                    if (cpf.isEmpty()) { labelErro.setText("Informe o CPF do cliente."); return; }
                    List<Pedido> resultado = facade.buscarPedidosPorCliente(cpf);
                    exibirPedidos(resultado, "Pedidos do cliente (" + resultado.size() + "):");
                    break;
                }

                case PEDIDO_POR_PIZZA: {
                    String tipo = comboFiltroPizza.getValue();
                    if (tipo == null) { labelErro.setText("Selecione uma pizza."); return; }
                    List<Pizza> pizzas = facade.buscarPizzasPorTipo(tipo);
                    if (pizzas.isEmpty()) { labelErro.setText("Pizza não encontrada: " + tipo); return; }
                    List<Pedido> resultado = facade.buscarPedidosPorPizza(pizzas.get(0));
                    exibirPedidos(resultado, "Pedidos com a pizza '" + tipo + "' (" + resultado.size() + "):");
                    break;
                }

                case PEDIDO_POR_ESTADO: {
                    String estadoStr = comboFiltroEstado.getValue();
                    if (estadoStr == null) { labelErro.setText("Selecione um estado."); return; }
                    EstadoPedido estado = EstadoPedido.valueOf(estadoStr);
                    List<Pedido> resultado = facade.buscarPedidosPorEstado(estado);
                    exibirPedidos(resultado, "Pedidos com estado '" + estadoStr + "' (" + resultado.size() + "):");
                    break;
                }
            }
        } catch (Exception e) {
            labelErro.setText("Erro: " + e.getMessage());
        }
    }

    private void exibirPedidos(List<Pedido> pedidos, String tituloResultado) {
        labelResultado.setText(tituloResultado);
        if (pedidos.isEmpty()) {
            listaResultado.getItems().add("Nenhum pedido encontrado.");
            return;
        }
        pedidos.forEach(p -> {
            String clienteNome = p.getCliente() != null ? p.getCliente().getNome() : "—";
            String linha = String.format("Pedido #%d — %s | Estado: %s | R$ %.2f",
                    p.getId(), clienteNome, p.getEstado().name(), p.calcularTotal());
            listaResultado.getItems().add(linha);
        });
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
