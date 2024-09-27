package io.jmix.sample.user;

import io.jmix.core.DataManager;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import io.jmix.flowui.testassist.UiTestUtils;
import io.jmix.sample.JmixSampleUiIntegrationTestEventsApplication;
import io.jmix.sample.entity.User;
import io.jmix.sample.test_support.TestUiEventPublisher;
import io.jmix.sample.test_support.TestUiEventPublisherConfiguration;
import io.jmix.sample.view.user.UserListView;
import io.jmix.sample.view.user.UserListViewOpenedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sample UI integration test for the User entity.
 */
@UiTest
@SpringBootTest(
        properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {
                JmixSampleUiIntegrationTestEventsApplication.class,
                FlowuiTestAssistConfiguration.class,
                TestUiEventPublisherConfiguration.class
        })
public class UserUiTest {

    @Autowired
    DataManager dataManager;

    @Autowired
    ViewNavigators viewNavigators;

    @Autowired
    private TestUiEventPublisher testUiEventPublisher;

    @Test
    void when_openListView_then_UserListViewOpenedEventFired() {

        // when:
        viewNavigators.view(UiTestUtils.getCurrentView(), UserListView.class).navigate();

        // then:
        assertThat(testUiEventPublisher.getPublishedEvents())
                .hasSize(1);

        // and:
        assertThat(testUiEventPublisher.getPublishedEvents())
                .anyMatch(applicationEvent -> applicationEvent instanceof UserListViewOpenedEvent);
    }

    @AfterEach
    void tearDown() {
        dataManager.load(User.class)
                .query("e.username like ?1", "test-user-%")
                .list()
                .forEach(u -> dataManager.remove(u));
    }
}
