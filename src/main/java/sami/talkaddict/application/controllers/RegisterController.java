package sami.talkaddict.application.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.AuthenticationManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private TextField _usernameField;
    @FXML
    private TextField _emailField;
    @FXML
    private PasswordField _passwordField;

    private UserViewModel _userViewModel;
    private Validator _validator;

    @FXML
    private Button _registerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _userViewModel = new UserViewModel();
        _validator = new Validator();
        bindViewModelToFields();
        bindValidatorToFields();
    }

    private void bindValidatorToFields() {
        _validator.createCheck()
                .dependsOn(Config.AuthTweaks.USERNAME_FIELD_REGISTER_KEY, _usernameField.textProperty())
                .withMethod(_userViewModel::isUsernameValid)
                .decorates(_usernameField)
                .immediate();

        _validator.createCheck()
                .dependsOn(Config.AuthTweaks.EMAIL_FIELD_REGISTER_KEY, _emailField.textProperty())
                .withMethod(_userViewModel::isEmailValid)
                .decorates(_emailField)
                .immediate();

        _validator.createCheck()
                .dependsOn(Config.AuthTweaks.EMAIL_FIELD_REGISTER_KEY, _emailField.textProperty())
                .withMethod(_userViewModel::isEmailUnique)
                .decorates(_emailField);

        _validator.createCheck()
                .dependsOn(Config.AuthTweaks.PASSWORD_FIELD_REGISTER_KEY, _passwordField.textProperty())
                .withMethod(_userViewModel::isPasswordValid)
                .decorates(_passwordField)
                .immediate();
    }

    private void bindViewModelToFields() {
        _userViewModel.usernameProperty().bind(_usernameField.textProperty());
        _userViewModel.emailProperty().bind(_emailField.textProperty());
        _userViewModel.passwordProperty().bind(_passwordField.textProperty());
    }

    @FXML
    private void onRegisterUser(ActionEvent actionEvent) {
        try {
            if (_validator.validate()) {
                AuthenticationManager.register(_userViewModel);
                SceneFxManager.redirectTo(Config.Views.HOME_VIEW, _registerButton);
            } else {
                SceneFxManager.showAlertDialog(
                        "Invalid data",
                        Bindings.concat("Cannot register:\n", _validator.createStringBinding()).getValue(),
                        Alert.AlertType.WARNING
                );
            }
        } catch (Exception ex) {
            SceneFxManager.showAlertDialog("Register Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
