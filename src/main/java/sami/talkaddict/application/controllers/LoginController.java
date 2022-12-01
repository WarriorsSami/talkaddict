package sami.talkaddict.application.controllers;

import com.j256.ormlite.logger.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.infrastructure.utils.Config;
import sami.talkaddict.infrastructure.utils.managers.AuthenticationManager;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField _emailField;
    @FXML
    private PasswordField _passwordField;
    @FXML
    private Button _loginButton;

    private Logger _logger;
    private UserViewModel _userViewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _logger = ProviderService.provideLogger(LoginController.class);
        _userViewModel = new UserViewModel();
        bindViewModelToFields();
    }

    private void bindViewModelToFields() {
        _userViewModel.emailProperty().bind(_emailField.textProperty());
        _userViewModel.passwordProperty().bind(_passwordField.textProperty());
    }

    @FXML
    private void onLoginUser(ActionEvent actionEvent) {
        try {
            AuthenticationManager.login(_userViewModel);
            SceneFxManager.redirectTo(Config.Views.HOME_VIEW, _loginButton);
        } catch (ApplicationException ex) {
            SceneFxManager.showAlertDialog(
                    "Invalid credentials",
                    "Invalid email or password!",
                    Alert.AlertType.WARNING
            );
            _logger.error(ex, ex.getMessage(), ex.getStackTrace());
        } catch (Exception ex) {
            SceneFxManager.showAlertDialog(
                    "Login Error",
                    "Something went wrong!",
                    Alert.AlertType.ERROR
            );
            _logger.error(ex.toString(), ex.getStackTrace());
        }
    }}
