package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import java.util.stream.Collectors;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Carrinho;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.FormaPagamento;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.ItemPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.ModoEntrega;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;

public class ConfirmarPedidoController {

    @FXML private ListView<String> listaResumo;
    @FXML private Label labelSubtotal;
    @FXML private Label labelCliente;
    @FXML private RadioButton radioPix;
    @FXML private RadioButton radioCartao;
    @FXML private RadioButton radioRetirada;
    @FXML private RadioButton radioEntrega;
    @FXML private Label labelErro;

    private Carrinho carrinho;

    //chamado pelo NavigationManager depois de carregar a tela, já que o carrinho/cliente vêm
    //de telas anteriores e não dá pra injetar via FXML
    public void atualizarResumo() {
        carrinho = NavigationManager.getInstance().getCarrinhoAtual();

        listaResumo.getItems().setAll(
                carrinho.getItens().stream().map(this::descreverItem).collect(Collectors.toList())
        );
        labelSubtotal.setText("Subtotal: R$ " + String.format("%.2f", carrinho.calcularTotal()));

        Cliente cliente = NavigationManager.getInstance().getClienteDoPedido();
        if (cliente != null) {
            labelCliente.setText("Nome: " + cliente.getNome() + "   CPF: " + cliente.getCpf() +
                    "\nEndereço: " + cliente.getEndereco());
        } else {
            labelCliente.setText("Nenhum cliente selecionado — clique em \"Cliente\" para escolher ou cadastrar.");
        }
    }

    private String descreverItem(ItemPedido item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getQuantidade()).append("x Pizza de ").append(item.getPizza().getTipo())
          .append(" (").append(item.getTamanho()).append(")");
        if (!item.getAdicionais().isEmpty()) {
            sb.append(" — ").append(
                    item.getAdicionais().stream().map(a -> a.getNome()).distinct().collect(Collectors.joining(", "))
            );
        }
        sb.append("   R$ ").append(String.format("%.2f", item.calcularTotal()));
        return sb.toString();
    }

    @FXML
    private void escolherCliente() {
        NavigationManager.getInstance().irParaClientes(true);
    }

    @FXML
    private void confirmar() {
        labelErro.setText("");
        Cliente cliente = NavigationManager.getInstance().getClienteDoPedido();
        if (cliente == null) {
            labelErro.setText("Escolha um cliente antes de confirmar o pedido.");
            return;
        }

        FormaPagamento formaPagamento = radioPix.isSelected() ? FormaPagamento.PIX : FormaPagamento.CARTAO;
        ModoEntrega modoEntrega = radioRetirada.isSelected() ? ModoEntrega.RETIRADA : ModoEntrega.ENTREGA;

        try {
            carrinho.setCliente(cliente);
            NavigationManager.getInstance().getFacade().fecharPedido(carrinho, modoEntrega, formaPagamento);
            NavigationManager.getInstance().irParaHome();
        } catch (RuntimeException e) {
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
