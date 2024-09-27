package io.jmix.sample.view.user;

import com.vaadin.flow.component.AttachEvent;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.sample.entity.User;
import io.jmix.sample.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "users", layout = MainView.class)
@ViewController("User.list")
@ViewDescriptor("user-list-view.xml")
@LookupComponent("usersDataGrid")
@DialogMode(width = "64em")
public class UserListView extends StandardListView<User> {
    @Autowired
    private UiEventPublisher uiEventPublisher;

    @Subscribe
    public void onAttachEvent(final AttachEvent event) {
        uiEventPublisher.publishEventForCurrentUI(new UserListViewOpenedEvent(this));
    }
}