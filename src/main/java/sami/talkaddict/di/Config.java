package sami.talkaddict.di;

public class Config {
    public static class Database {
        public static final String JDBC_URL = "JDBC_URL";
        public static final String APPLY_DB_MIGRATIONS = "APPLY_DB_MIGRATIONS";
        public static final String POPULATE_DB_DIRECTORY = "POPULATE_DB_DIRECTORY";

        public static final String USER_DATA_FILENAME = "/user.csv";

        public static final String USERNAME_COLUMN_NAME = "name";
        public static final String EMAIL_COLUMN_NAME = "email";
        public static final String RECEIVER_ID_COLUMN_NAME = "receiver_id";
        public static final String SENDER_ID_COLUMN_NAME = "sender_id";
        public static final String USER_ID_COLUMN_NAME = "id";
    }

    public static class Preferences {
        public static final String LOGGED_IN_USER_ID_KEY = "LOGGED_IN_USER_ID_KEY";
    }

    public static class Views {
        public static final String MAIN_VIEW = "layouts/splash-view.fxml";
        public static final String HOME_VIEW = "layouts/home-view.fxml";
        public static final String LOGIN_PANE = "layouts/components/login-pane.fxml";
        public static final String REGISTER_PANE = "layouts/components/register-pane.fxml";
        public static final String PROFILE_PANE = "layouts/components/profile-pane.fxml";
        public static final String CHAT_PANE = "layouts/components/chat-pane.fxml";
    }

    public static class AppSettings {
        public static final String APP_TITLE = "Talkaddict";
        public static final Boolean RESIZABLE = false;
        public static final Double PROFILE_REFRESH_RATE = 30.0;
    }

    public static class ValidationTweaks {
        public static final Integer MIN_USERNAME_LENGTH = 3;
        public static final Integer MAX_USERNAME_LENGTH = 20;
        public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]*$";
        public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        public static final Integer MIN_PASSWORD_LENGTH = 8;
        public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        public static final Integer MAX_DESCRIPTION_LENGTH = 100;

        public static final String USERNAME_FIELD_REGISTER_KEY = "usernameFieldRegister";
        public static final String EMAIL_FIELD_REGISTER_KEY = "emailFieldRegister";
        public static final String PASSWORD_FIELD_REGISTER_KEY = "passwordFieldRegister";
        public static final String USERNAME_FIELD_PROFILE_KEY = "usernameFieldProfile";
        public static final String DESCRIPTION_FIELD_PROFILE_KEY = "descriptionFieldProfile";

        public static final String DEFAULT_USER_DESCRIPTION = "I'm a new user!";
        public static final String DEFAULT_AVATAR_DIRECTORY_PATH = "AVATAR_DIRECTORY";
    }

    public static class FxmlSettings {
        public static final String EYE_GLYPH = "EYE";
        public static final String EYE_SLASH_GLYPH = "EYE_SLASH";

        public static final Double AVATAR_CLIP_ARC_WIDTH = 360.0;
        public static final Double AVATAR_CLIP_ARC_HEIGHT = 360.0;
        public static final Double AVATAR_FIT_WIDTH = 50.0;
        public static final Double AVATAR_FIT_HEIGHT = 50.0;

        public static final Double MAIN_SLIDER_POSITION = 1200.0 - 580.0;
        public static final Long SEARCH_DELAY = 2000L;

        public static final String AVATAR_STATUS_ONLINE_STYLE_CLASS = "online-status";
        public static final String AVATAR_STATUS_OFFLINE_STYLE_CLASS = "offline-status";
        public static final String AVATAR_STATUS_BUSY_STYLE_CLASS = "busy-status";
        public static final String AVATAR_STATUS_AWAY_STYLE_CLASS = "away-status";
        public static final double MESSAGE_IMAGE_PREVIEW_WIDTH = 100.0;
        public static final double MESSAGE_IMAGE_PREVIEW_HEIGHT = 50.0;
        public static final double MESSAGE_IMAGE_WIDTH = 200.0;
        public static final double MESSAGE_IMAGE_HEIGHT = 100.0;
        public static final double USERS_LIST_CELL_SIZE = 60.0;
        public static final double CHAT_LIST_CELL_SIZE = 200.0;
    }
}
