package io.anymobi.events;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Spring Rest Api")
                .description("Rest Api devleopment with Spring")
                .build();
        assertThat(event).isNotNull();

    }

    @Test
    public void javaBean(){

        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

}