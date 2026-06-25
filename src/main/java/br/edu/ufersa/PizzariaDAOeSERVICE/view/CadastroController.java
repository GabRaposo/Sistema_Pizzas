package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class CadastroController {

    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private PasswordField campoConfirmarSenha;
    @FXML private RadioButton radioFuncionario;
    @FXML private RadioButton radioProprietario;
    @FXML private Label labelErro;

    @FXML
    private void criarConta() {
        labelErro.setText("");

        if (!campoSenha.getText().equals(campoConfirmarSenha.getText())) {
            labelErro.setText("As senhas não coincidem.");
            return;
        }

        try {
            if (radioProprietario.isSelected()) {
                NavigationManager.getInstance().getFacade()
                        .cadastrarDono(campoNome.getText(), campoEmail.getText(), campoSenha.getText());
            } else {
                NavigationManager.getInstance().getFacade()
                        .cadastrarFuncionario(campoNome.getText(), campoEmail.getText(), campoSenha.getText());
            }
            NavigationManager.getInstance().irParaLogin();
        } catch (RuntimeException e) {
            labelErro.setText(e.getMessage());
        }
    }
}
