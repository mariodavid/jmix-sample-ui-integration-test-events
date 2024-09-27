package io.jmix.sample.view.user;

import org.springframework.context.ApplicationEvent;

public class UserListViewOpenedEvent extends ApplicationEvent {

    public UserListViewOpenedEvent(Object source) {
        super(source);
    }
}