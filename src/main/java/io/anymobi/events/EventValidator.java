package io.anymobi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {


    public void validate(EventDto eventDto, Errors errors) {

        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("basePrice", "wrong value", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrong value", "MaxPrice is wrong");
        }

        @NotNull LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
                || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())) {
            errors.rejectValue("endEventDateItem", "wrong value", "endEventDateTime is wrong");
        }

        // TODO BeginEventDateTime
        // TODO ClsoeEnrollmentTime


    }

}
