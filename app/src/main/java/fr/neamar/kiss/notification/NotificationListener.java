package fr.neamar.kiss.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {
    public static final String NOTIFICATION_PREFERENCES_NAME = "notifications";

    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getBaseContext().getSharedPreferences(NOTIFICATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();

        // Build a map of notifications currently displayed,
        // ordered per package
        StatusBarNotification[] sbns = getActiveNotifications();
        Map<String, Set<String>> notificationsByPackage = new HashMap<>();
        for (StatusBarNotification sbn : sbns) {
            String packageName = sbn.getPackageName();
            if (!notificationsByPackage.containsKey(packageName)) {
                notificationsByPackage.put(packageName, new HashSet<String>());
            }

            notificationsByPackage.get(packageName).add(Integer.toString(sbn.getId()));
        }

        // And synchronise this map with our SharedPreferences
        // (an easier option would have been to .clear() the SharedPreferences,
        // but then the listeners on SharedPreferences are not properly triggered)
        SharedPreferences.Editor editor = prefs.edit();
        // allKeys contains all the package names either in preferences or in the current notifications
        Set<String> allKeys = prefs.getAll().keySet();
        allKeys.addAll(notificationsByPackage.keySet());
        for (String packageName : allKeys) {
            if (notificationsByPackage.containsKey(packageName)) {
                editor.putStringSet(packageName, notificationsByPackage.get(packageName));
            } else {
                editor.remove(packageName);
            }
        }

        editor.apply();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Set<String> currentNotifications = getCurrentNotificationsForPackage(sbn.getPackageName());

        currentNotifications.add(Integer.toString(sbn.getId()));
        prefs.edit().putStringSet(sbn.getPackageName(), currentNotifications).apply();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Set<String> currentNotifications = getCurrentNotificationsForPackage(sbn.getPackageName());

        currentNotifications.remove(Integer.toString(sbn.getId()));

        SharedPreferences.Editor editor = prefs.edit();
        if (currentNotifications.isEmpty()) {
            // Clean up!
            editor.remove(sbn.getPackageName());
        } else {
            editor.putStringSet(sbn.getPackageName(), currentNotifications);
        }
        editor.apply();
    }

    public Set<String> getCurrentNotificationsForPackage(String packageName) {
        Set<String> currentNotifications = prefs.getStringSet(packageName, null);
        if (currentNotifications == null) {
            return new HashSet<>();
        } else {
            // The set returned by getStringSet() should NOT be modified
            // see https://developer.android.com/reference/android/content/SharedPreferences.html#getStringSet(java.lang.String,%2520java.util.Set%3Cjava.lang.String%3E)
            return new HashSet<>(currentNotifications);
        }
    }
}
