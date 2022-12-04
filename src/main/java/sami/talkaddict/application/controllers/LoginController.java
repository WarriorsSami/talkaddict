package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import com.j256.ormlite.logger.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sami.talkaddict.application.requests.commands.auth.LoginUser;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.exceptions.ApplicationException;
import sami.talkaddict.di.Config;
import sami.talkaddict.infrastructure.utils.managers.SceneFxManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField _emailField;
    @FXML
    private PasswordField _passwordField;
    @FXML
    private TextField _passwordTextField;
    @FXML
    private FontAwesomeIconView _passwordToggler;
    @FXML
    private Button _loginButton;

    private Logger _logger;
    private Pipeline _mediator;
    private UserViewModel _userViewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _logger = ProviderService.provideLogger(LoginController.class);
        _mediator = ProviderService.provideMediator();
        _userViewModel = new UserViewModel();
        bindViewModelToFields();
        _passwordTextField.setVisible(false);
    }

    private void bindViewModelToFields() {
        _userViewModel.emailProperty().bind(_emailField.textProperty());
        _userViewModel.passwordProperty().bind(_passwordField.textProperty());
    }

    @FXML
    private void onLoginUser(ActionEvent actionEvent) {
        try {
            var response = _mediator.send(new LoginUser.Command(_userViewModel));
                if (response.isOk()) {
                    _logger.info("User logged in successfully");
                    SceneFxManager.redirectTo(Config.Views.HOME_VIEW, _loginButton);
                } else {
                    _logger.error("User login failed");
                    throw response.err().orElseThrow();
                }
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
    }

    @FXML
    private void togglePassword(MouseEvent mouseEvent) {
        SceneFxManager.togglePasswordField(_passwordField, _passwordTextField, _passwordToggler);
    }
}
