package sami.talkaddict.infrastructure.utils.managers;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvManager {
    private static final Dotenv _dotenv = Dotenv.configure().load();

    public static String get(String key) {
        return _dotenv.get(key);
    }
}
