package org.example.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class ErrorNotification extends Notification {
    public ErrorNotification(String message) {
        super(message);
        addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    public static Notification show(String message){
        ErrorNotification notification = new ErrorNotification(message);
        notification.open();
        return notification;
    }
}
