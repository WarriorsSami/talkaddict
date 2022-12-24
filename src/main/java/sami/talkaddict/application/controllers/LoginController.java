package sami.talkaddict.application.controllers;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import com.j256.ormlite.logger.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dev.kylesilver.result.Result;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sami.talkaddict.application.models.user.UserViewModel;
import sami.talkaddict.application.requests.commands.auth.LoginUser;
import sami.talkaddict.di.Config;
import sami.talkaddict.di.ProviderService;
import sami.talkaddict.domain.exceptions.ApplicationException;
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
        _logger.info("Login View Initialized");
    }

    private void bindViewModelToFields() {
        _userViewModel.emailProperty().bind(_emailField.textProperty());
        _userViewModel.passwordProperty().bind(_passwordField.textProperty());
    }

    @FXML
    private void onLoginUser(ActionEvent actionEvent) {
        // if password text field is active, update password field
        if (_passwordTextField.isVisible()) {
            _passwordField.setText(_passwordTextField.getText());
        }

        var loginUserTask = new Task<Result<Voidy, Exception>>() {
            @Override
            protected Result<Voidy, Exception> call() {
                return _mediator.send(new LoginUser.Command(_userViewModel));
            }
        };

        loginUserTask.setOnSucceeded(event -> {
            try {
                var response = loginUserTask.getValue();
                if (response.isOk()) {
                    _logger.info("User logged in successfully");
                    SceneFxManager.redirectTo(Config.Views.HOME_VIEW, _loginButton);
                } else {
                    _logger.error("User login failed");
                    throw response.err().orElseThrow();
                }
            } catch (ApplicationException ex) {
                //TODO: move javafx related operations to Main Thread somehow
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
        });

        var loginUserThread = new Thread(loginUserTask);
        loginUserThread.setDaemon(true);
        loginUserThread.start();
    }

    @FXML
    private void togglePassword(MouseEvent mouseEvent) {
        SceneFxManager.togglePasswordField(_passwordField, _passwordTextField, _passwordToggler);
    }
}
