package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ClienteEditController {

    @FXML private TextField campoNome;
    @FXML private Label labelCpf;
    @FXML private TextField campoEndereco;
    @FXML private Label labelErro;

    private Cliente cliente;

    //chamado pelo NavigationManager assim que a tela é carregada
    public void carregar(Cliente cliente) {
        this.cliente = cliente;
        campoNome.setText(cliente.getNome());
        labelCpf.setText("CPF: " + cliente.getCpf()); // CPF é imutável (ver ClienteDAO.alterar)
        campoEndereco.setText(cliente.getEndereco());
    }

    @FXML
    private void confirmarMudanca() {
        labelErro.setText("");
        try {
            cliente.setNome(campoNome.getText());
            cliente.setEndereco(campoEndereco.getText());
            NavigationManager.getInstance().getFacade().alterarCliente(cliente);
            NavigationManager.getInstance().irParaClientes(false);
        } catch (RuntimeException e) {
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void excluirCliente() {
        labelErro.setText("");
        try {
            NavigationManager.getInstance().getFacade().removerCliente(cliente);
            NavigationManager.getInstance().irParaClientes(false);
        } catch (RuntimeException e) {
            // ex: cliente já tem pedidos registrados (ver ClienteService.removerCliente)
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        NavigationManager.getInstance().irParaClientes(false);
    }

    @FXML
    private void irParaHome() {
        NavigationManager.getInstance().irParaHome();
    }
}
