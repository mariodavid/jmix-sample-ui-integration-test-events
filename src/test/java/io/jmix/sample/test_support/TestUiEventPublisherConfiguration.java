package io.jmix.sample.test_support;

import io.jmix.core.cluster.ClusterApplicationEventPublisher;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.sys.SessionHolder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestUiEventPublisherConfiguration {

    @Bean("flowui_UiEventPublisher")
    public UiEventPublisher uiEventPublisher(SystemAuthenticator systemAuthenticator, ClusterApplicationEventPublisher clusterApplicationEventPublisher, SessionHolder sessionHolder, CurrentAuthentication currentAuthentication) {
        return new TestUiEventPublisher(systemAuthenticator, clusterApplicationEventPublisher, sessionHolder, currentAuthentication);
    }
}
