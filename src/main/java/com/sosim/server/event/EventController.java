package com.sosim.server.event;

import static com.sosim.server.common.constant.MessageConstant.EVENT_CREATE_SUCCESS;
import static com.sosim.server.common.constant.MessageConstant.EVENT_DELETE_SUCCESS;
import static com.sosim.server.common.constant.MessageConstant.EVENT_INFO_SUCCESS;
import static com.sosim.server.common.constant.MessageConstant.EVENT_PAYMENT_TYPE_CHANGE_SUCCESS;
import static com.sosim.server.common.constant.MessageConstant.EVENT_UPDATE_SUCCESS;

import com.sosim.server.common.response.Response;
import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/event/penalty")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventCreateReq eventCreateReq) {
        Long eventId = this.eventService.createEvent(eventCreateReq);
        Response<?> response = Response.builder().message(EVENT_CREATE_SUCCESS).content(eventId).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable("eventId") long id) {
        Event event = this.eventService.getEvent(id);
        Response<?> response = Response.builder().message(EVENT_INFO_SUCCESS).content(event).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable("eventId") long id) {
        EventInfo eventInfo = this.eventService.updateEvent(id);
        Response<?> response = Response.builder().message(EVENT_UPDATE_SUCCESS).content(eventInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable("eventId") long id) {
        this.eventService.deleteEvent(id);
        Response<?> response = Response.builder().message(EVENT_DELETE_SUCCESS).content(null).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> changePaymentType(@PathVariable("eventId") long id) {
        EventInfo eventInfo = this.eventService.changePaymentType(id);
        Response<?> response = Response.builder().message(EVENT_PAYMENT_TYPE_CHANGE_SUCCESS).content(eventInfo).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getEventList(@Valid @RequestBody EventListReq eventListReq) {
        List<EventInfo> eventList = this.eventService.getEvent(eventListReq);
        Response<?> response = Response.builder().message(EVENT_INFO_SUCCESS).content(eventList).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
