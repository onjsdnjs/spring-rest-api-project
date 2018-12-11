package io.anymobi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.anymobi.common.RestDocsConfiguration;
import io.anymobi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/***
 *
 * 통합테스트
 */

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest Api")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,12,00,00))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,12,00,00))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,12,00,00))
                .endEventDateTime(LocalDateTime.of(2018,11,26,12,00,00))
                .basePrice(100)
                .maxPrice(200)

                .limitOfEnrollment(100)
                .location("애니모비 회의실")
                .build();

//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update event"),
                                linkWithRel("profile").description("link to profile")
                        ),

                        requestHeaders(

                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("contentType")
                        ),

                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event")

                        ),
                        responseHeaders(

                                headerWithName(HttpHeaders.LOCATION).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("contentType")
                        ),

                        responseFields(
                                fieldWithPath("id").description("Id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event"),
                                fieldWithPath("offline").description("Offline of new event"),
                                fieldWithPath("free").description("Free of new event"),
                                fieldWithPath("eventStatus").description("EventStatus of new event"),
                                fieldWithPath("_links.self.href").description("Links Self of new event"),
                                fieldWithPath("_links.query-events.href").description("Links Query Events of new event"),
                                fieldWithPath("_links.update-event.href").description("Links Update Events of new event"),
                                fieldWithPath("_links.profile.href").description("Links profile Event of new event")

                        )

                        )

                );



    }

    @Test
    @TestDescription("전송하면 안되는 속성을 전송했을 때 에러가 발생하는 테스트")
    public void createEventBadRequest() throws Exception {

        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("Rest Api")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,12,00,00))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,12,00,00))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,12,00,00))
                .endEventDateTime(LocalDateTime.of(2018,11,26,12,00,00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("애니모비 회의실")
                .free(true)
                .offline(true)
                .eventStatus(EventStatus.PUBLISHED)
                .build();


        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("_links.index").exists())
        ;

    }

    @Test
    @TestDescription("데이터를 빈값으로 입력했을 때 에러가 발생하는 테스트")
    public void createEventBadRequestEmptyInput() throws Exception {

        EventDto event = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("_links.index").exists());
    }


    @Test
    @TestDescription("틀린 데이터를 입력했을 때 에러가 발생하는 테스트")
    public void createEventBadRequestWrongInput() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest Api")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,24,12,00,00))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,23,12,00,00))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,12,00,00))
                .endEventDateTime(LocalDateTime.of(2018,11,26,12,00,00))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("애니모비 회의실")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                /*.andExpect(jsonPath("$[0].field").exists())*/ //FieldError 일경우 테스트
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
                /*.andExpect(jsonPath("$[0].rejectedValue").exists())*/ //FieldError 일경우 테스트

        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {

        IntStream.range(0,30).forEach(this::generateEvent);

        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,desc")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("page").exists())
                    .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                    .andExpect(jsonPath("_links.self").exists())
                    .andExpect(jsonPath("_links.profile").exists())
                    .andDo(document("query-events",
                            links(
                                    linkWithRel("first").description("link to first"),
                                    linkWithRel("prev").description("link to prev"),
                                    linkWithRel("self").description("link to self"),
                                    linkWithRel("next").description("link to next"),
                                    linkWithRel("last").description("link to last"),
                                    linkWithRel("profile").description("link to profile")
                            )
                            /*,

                            requestHeaders(

                                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("contentType")
                            ),

                            requestFields(
                                    fieldWithPath("page").description("현재 페이지 값"),
                                    fieldWithPath("size").description("게시물 갯수"),
                                    fieldWithPath("sort").description("정렬방식")

                            ),
                           responseHeaders(

                                    headerWithName(HttpHeaders.LOCATION).description("accept header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("contentType")
                            ),

                            responseFields(
                                    fieldWithPath("id").description("Id of new event"),
                                    fieldWithPath("name").description("Name of new event"),
                                    fieldWithPath("description").description("Description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                    fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                    fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                    fieldWithPath("location").description("Location of new event"),
                                    fieldWithPath("basePrice").description("BasePrice of new event"),
                                    fieldWithPath("maxPrice").description("MaxPrice of new event"),
                                    fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event"),
                                    fieldWithPath("offline").description("Offline of new event"),
                                    fieldWithPath("free").description("Free of new event"),
                                    fieldWithPath("eventStatus").description("EventStatus of new event"),
                                    fieldWithPath("_links.self.href").description("Links Self of new event"),
                                    fieldWithPath("_links.profile.href").description("Links profile Event of new event")

                            )*/
                            ))
        ;
    }

    @Test
    public void getEvent() throws Exception {
        // Given
        Event event = this.generateEvent(100);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        )
                        ))
        ;


    }

    @Test
    @TestDescription("없는 이벤트 요청했을 때 404 응답받기")
    public void getEvent404() throws Exception {

        // When & Then
        this.mockMvc.perform(get("/api/event/11111111"))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    private Event generateEvent(int index) {

        Event event = Event.builder()
                .name("event" + index)
                .description("test event")
                .build();
        return this.eventRepository.save(event);

    }

}
