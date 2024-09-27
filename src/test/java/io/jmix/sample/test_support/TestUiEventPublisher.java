package io.jmix.sample.test_support;

import com.vaadin.flow.component.UI;
import io.jmix.core.cluster.ClusterApplicationEventPublisher;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.sys.SessionHolder;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestUiEventPublisher extends UiEventPublisher {

    private final List<ApplicationEvent> publishedEvents = new ArrayList<>();

    public TestUiEventPublisher(SystemAuthenticator systemAuthenticator, ClusterApplicationEventPublisher clusterApplicationEventPublisher, SessionHolder sessionHolder, CurrentAuthentication currentAuthentication) {
        super(systemAuthenticator, clusterApplicationEventPublisher, sessionHolder, currentAuthentication);
    }

    public List<ApplicationEvent> getPublishedEvents() {
        return publishedEvents;
    }

    @Override
    protected void publish(Collection<UI> uis, ApplicationEvent event) {
        super.publish(uis, event);
        publishedEvents.add(event);
    }
}
