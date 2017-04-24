package com.papaya.scotthanberg.papaya;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by scotthanberg on 4/19/17.
 */

public class NotificationClass extends AppCompatActivity{
    private String className, class_id, session_id, username;
    private static int counter = 0;
    public NotificationClass(String className, String class_id, String session_id, String username) {
        this.className = className;
        this.class_id = class_id;
        this.session_id = session_id;
        this.username = username;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotification(String title, String content) {

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(title)
                        .setContentText(content);
        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //When you issue multiple notifications about the same type of event, it’s best practice for your app to try to update an existing notification with this new information, rather than immediately creating a new notification. If you want to update this notification at a later date, you need to assign it an ID. You can then use this ID whenever you issue a subsequent notification. If the previous notification is still visible, the system will update this existing notification, rather than create a new one. In this example, the notification’s ID is 001//


        mNotificationManager.notify(001, mBuilder.build());
    }

    public String getClassName() {
        return className;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public String getUsername() {
        return username;
    }

    public boolean equals(NotificationClass test) {
        if (this.className != test.getClassName())
            return false;
        if (this.class_id != test.getClass_id())
            return false;

        return true;
    }

    public static int getCounter() {
        return counter;
    }

    public synchronized static void incrementCounter() {
        if (counter == Integer.MAX_VALUE)
            counter = 0;
        counter++;
    }
}
