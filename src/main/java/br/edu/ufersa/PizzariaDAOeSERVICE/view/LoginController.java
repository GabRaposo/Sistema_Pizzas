package br.edu.ufersa.PizzariaDAOeSERVICE.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField campoNome;
    @FXML private PasswordField campoSenha;
    @FXML private Label labelErro;

    @FXML
    private void entrar() {
        labelErro.setText("");
        try {
            boolean ok = NavigationManager.getInstance().getFacade()
                    .login(campoNome.getText(), campoSenha.getText());
            if (ok) {
                NavigationManager.getInstance().irParaHome();
            } else {
                labelErro.setText("Nome ou senha incorretos.");
            }
        } catch (RuntimeException e) {
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void irParaCadastro() {
        NavigationManager.getInstance().irParaCadastro();
    }
}
