package io.anymobi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

/***
 * 단위 테스트
 */

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder() {

        Event event = Event.builder()
                .name("Spring Rest Api")
                .description("Rest Api developtment with Spring")
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

    private Object[] parametersForTestFree() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100,0, false},
                new Object[]{0,100,false},
                new Object[]{100,100,false}
        };
    }

    @Test
    @Parameters/*(method="parametersForTestFree")*/
    public void testFree(int basePrice, int maxPrice, boolean isTrue) {

        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // When

        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isTrue);

    }

    private Object[] parametersForTestOffline() {
        return new Object[]{
                new Object[]{"애니모비", true},
                new Object[]{null, false},
                new Object[]{"", false},
        };
    }

    @Test
    @Parameters/*(method="parametersForTestOffline")*/
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();
        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

}