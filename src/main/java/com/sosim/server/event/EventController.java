package com.sosim.server.event;

import com.sosim.server.common.response.Response;
import com.sosim.server.event.dto.info.EventSingleInfo;
import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.info.MonthInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import com.sosim.server.event.dto.req.EventModifyReq;
import com.sosim.server.event.dto.req.PaymentTypeReq;
import com.sosim.server.security.AuthUser;
import com.sosim.server.type.CodeType;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/event/penalty")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody EventCreateReq eventCreateReq) {
        Long eventId = this.eventService.createEvent(authUser, eventCreateReq);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_CREATE_SUCCESS, eventId), CodeType.EVENT_CREATE_SUCCESS.getHttpStatus());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable("eventId") long id) {
        EventSingleInfo eventSingleInfo = this.eventService.getEvent(id);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_INFO_SUCCESS, eventSingleInfo), CodeType.EVENT_INFO_SUCCESS.getHttpStatus());
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@AuthenticationPrincipal AuthUser authUser, @PathVariable("eventId") long id, @RequestBody EventModifyReq eventModifyReq) {
        EventInfo eventInfo = this.eventService.updateEvent(authUser, id, eventModifyReq);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_UPDATE_SUCCESS, eventInfo), CodeType.EVENT_UPDATE_SUCCESS.getHttpStatus());
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@AuthenticationPrincipal AuthUser authUser, @PathVariable("eventId") long id) {
        this.eventService.deleteEvent(authUser, id);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_DELETE_SUCCESS, null), CodeType.EVENT_DELETE_SUCCESS.getHttpStatus());
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<?> changePaymentType(@AuthenticationPrincipal AuthUser authUser, @PathVariable("eventId") long id, @RequestBody PaymentTypeReq paymentTypeReq) {
        EventInfo eventInfo = this.eventService.changePaymentType(authUser, id, paymentTypeReq);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_PAYMENT_TYPE_CHANGE_SUCCESS, eventInfo), CodeType.EVENT_PAYMENT_TYPE_CHANGE_SUCCESS.getHttpStatus());
    }

    @GetMapping("/list/{groupId}")
    public ResponseEntity<?> getEventList(@PathVariable("groupId") long groupId, @Valid @ModelAttribute EventListReq eventListReq) {
        List<EventInfo> eventList = this.eventService.getEventList(groupId, eventListReq);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_LIST_SUCCESS, eventList), CodeType.EVENT_LIST_SUCCESS.getHttpStatus());
    }

    @GetMapping("/mstatus/{groupId}")
    public ResponseEntity<?> getMstatus(@PathVariable("groupId") long groupId, @Valid @RequestParam("month") int month) {
        List<MonthInfo> monthList = this.eventService.getMonthInfo(groupId, month);
        return new ResponseEntity<>(Response.create(CodeType.EVENT_MONTH_STATUS_SUCCESS, monthList), CodeType.EVENT_MONTH_STATUS_SUCCESS.getHttpStatus());
    }
}
