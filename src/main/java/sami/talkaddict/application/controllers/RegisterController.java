package sami.talkaddict.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.AuthenticationManager;
import sami.talkaddict.infrastructure.utils.managers.SceneRedirectManagerFx;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private UserViewModel userViewModel;

    @FXML
    private Button registerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userViewModel = new UserViewModel();
        bindViewModelToFields();
    }

    private void bindViewModelToFields() {
        userViewModel.usernameProperty().bind(usernameField.textProperty());
        userViewModel.emailProperty().bind(emailField.textProperty());
        userViewModel.passwordProperty().bind(passwordField.textProperty());

        // TODO: add proper validation rules
        var validationRule = userViewModel.usernameProperty().isEmpty()
                .or(userViewModel.emailProperty().isEmpty())
                .or(userViewModel.passwordProperty().isEmpty());
        registerButton.disableProperty().bind(validationRule);
    }

    @FXML
    private void onRegisterUser(ActionEvent actionEvent) throws ApplicationException, SQLException, IOException {
        AuthenticationManager.register(userViewModel);
        SceneRedirectManagerFx.redirectTo(Config.HOME_VIEW, registerButton);
    }
}
