package sami.talkaddict.infrastructure.utils.managers;

import java.util.prefs.Preferences;

public class PreferencesManager {
    public static void setKey(String key, String value) {
        Preferences.userRoot().put(key, value);
    }

    public static String getKey(String key) {
        return Preferences.userRoot().get(key, null);
    }

    public static void removeKey(String key) {
        Preferences.userRoot().remove(key);
    }
}
