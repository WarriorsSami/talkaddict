package sami.talkaddict.infrastructure.utils;

public class Config {
    public static class Database {
        public static final String JDBC_URL = "JDBC_URL";
        public static final String APPLY_DB_MIGRATIONS = "APPLY_DB_MIGRATIONS";
    }

    public static class Preferences {
        public static final String LOGGED_IN_USER_ID_KEY = "LOGGED_IN_USER_ID_KEY";
    }

    public static class Views {
        public static final String MAIN_VIEW = "layouts/main-view.fxml";
        public static final String LOGIN_VIEW = "layouts/login-view.fxml";
        public static final String REGISTER_VIEW = "layouts/register-view.fxml";
        public static final String HOME_VIEW = "layouts/home-view.fxml";
    }

    public static class Settings {
        public static final String APP_TITLE = "Talkaddict";
    }
}
