package sami.talkaddict.application.models.user;

public class LoginDto {
    public String Email;
    public String Password;

    public LoginDto(String email, String password) {
        Email = email;
        Password = password;
    }
}
