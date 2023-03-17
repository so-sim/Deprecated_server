package com.sosim.server.event;

import com.sosim.server.common.response.Response;
import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import com.sosim.server.type.CodeType;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        return new ResponseEntity<>(Response.create(CodeType.EVENT_CREATE_SUCCESS, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable("eventId") long id) {
        Event event = this.eventService.getEvent(id);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_INFO_SUCCESS, event), HttpStatus.OK);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable("eventId") long id) {
        EventInfo eventInfo = this.eventService.updateEvent(id);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_UPDATE_SUCCESS, eventInfo), HttpStatus.OK);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable("eventId") long id) {
        return new ResponseEntity<>(Response.create(CodeType.EVENT_DELETE_SUCCESS, null), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> changePaymentType(@PathVariable("eventId") long id) {
        EventInfo eventInfo = this.eventService.changePaymentType(id);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_PAYMENT_TYPE_CHANGE_SUCCESS, eventInfo), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getEventList(@Valid @ModelAttribute EventListReq eventListReq) {
        List<EventInfo> eventList = this.eventService.getEvent(eventListReq);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_LIST_SUCCESS, eventList), HttpStatus.OK);
    }
}
