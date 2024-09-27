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

- **Key method**: `onAttachEvent(AttachEvent event)` triggers when the view is attached to the UI and publishes the `UserListViewOpenedEvent` for the current UI.

### 2. `TestUiEventPublisher`
This class extends Jmix's `UiEventPublisher` to capture all published events during testing. It overrides the `publish` method to store each event in a list for later verification.

- **Key method**: `getPublishedEvents()` returns the list of events published during the test.

### 3. `TestUiEventPublisherConfiguration`
A Spring test configuration that overrides the default `UiEventPublisher` with `TestUiEventPublisher` for test purposes.

### 4. `UserUiTest`
An integration test that simulates the navigation to the `UserListView` and verifies that the `UserListViewOpenedEvent` is fired.

- The test navigates to the `UserListView`.
- It checks if exactly one event is published.
- It ensures the published event is of type `UserListViewOpenedEvent`.

## Running the Tests

To run the integration tests, use the following command:

```bash
./gradlew test
```

The test will:

1. Open the UserListView.
2. Check if the UserListViewOpenedEvent is fired.
3. Clean up any test users created during the test.

