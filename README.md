# Jmix Integration Test: Custom Event Publishing

This example project demonstrates how to perform integration tests in a Jmix application that involve publishing custom UI events. Specifically, it shows how to capture and validate that a custom event, `UserListViewOpenedEvent`, is published when a specific view (`UserListView`) is opened.

## Overview

The project uses the Jmix framework with Spring Boot and Vaadin for UI. We extend Jmix’s `UiEventPublisher` to track published events during test execution. This is done using a custom test utility `TestUiEventPublisher`.

The project includes:
- A production `UserListView` class that publishes a custom event when the view is attached.
- A custom event publisher (`TestUiEventPublisher`) that records all UI events during test execution.
- A test configuration class to register this custom publisher.
- An integration test (`UserUiTest`) that verifies if the `UserListViewOpenedEvent` is published when the user navigates to the `UserListView`.

## Key Classes

### 1. `UserListView`
This is the production class that defines the user list view. It uses Vaadin's `@Route` to define the URL path and publishes a `UserListViewOpenedEvent` when the view is attached to the UI.
```java
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
```

- **Key method**: `onAttachEvent(AttachEvent event)` triggers when the view is attached to the UI and publishes the `UserListViewOpenedEvent` for the current UI.

### 2. `TestUiEventPublisher`
This class extends Jmix's `UiEventPublisher` to capture all published events during testing. It overrides the `publish` method to store each event in a list for later verification.

```java
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
```
- **Key method**: `getPublishedEvents()` returns the list of events published during the test.

### 3. `TestUiEventPublisherConfiguration`
A Spring test configuration that overrides the default `UiEventPublisher` with `TestUiEventPublisher` for test purposes.

```java
@TestConfiguration
public class TestUiEventPublisherConfiguration {

    @Bean("flowui_UiEventPublisher")
    public UiEventPublisher uiEventPublisher(SystemAuthenticator systemAuthenticator, ClusterApplicationEventPublisher clusterApplicationEventPublisher, SessionHolder sessionHolder, CurrentAuthentication currentAuthentication) {
        return new TestUiEventPublisher(systemAuthenticator, clusterApplicationEventPublisher, sessionHolder, currentAuthentication);
    }
}
```

### 4. `UserUiTest`
An integration test that simulates the navigation to the `UserListView` and verifies that the `UserListViewOpenedEvent` is fired.

#### Important Test Configuration Notes:
1. **Bean Definition Override**: It's important to set the property `spring.main.allow-bean-definition-overriding=true` in the test configuration. This allows your test configuration to override the standard `UiEventPublisher` with the `TestUiEventPublisher`.

2. **Test Event Publisher Configuration**: Ensure that `TestUiEventPublisherConfiguration` is included in the test classes configuration. This replaces the default publisher with the test-specific version, allowing event tracking during tests.

3. **Inject the `TestUiEventPublisher`**: Instead of injecting the regular `UiEventPublisher`, make sure to inject `TestUiEventPublisher` in the test class to gain access to the `getPublishedEvents()` method, which is crucial for validating that events have been published.

```java
@UiTest
@SpringBootTest(
        properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {
                JmixSampleUiIntegrationTestEventsApplication.class,
                FlowuiTestAssistConfiguration.class,
                TestUiEventPublisherConfiguration.class  // Ensure Test configuration is included
        })
public class UserUiTest {

    @Autowired
    DataManager dataManager;

    @Autowired
    ViewNavigators viewNavigators;

    @Autowired
    private TestUiEventPublisher testUiEventPublisher; // Inject the test event publisher

    @Test
    void when_openListView_then_UserListViewOpenedEventFired() {

        // when:
        viewNavigators.view(UiTestUtils.getCurrentView(), UserListView.class).navigate();

        // then:
        assertThat(testUiEventPublisher.getPublishedEvents())  // Use TestUiEventPublisher
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
```

## Running the Tests

To run the integration tests, use the following command:

```bash
./gradlew test
```