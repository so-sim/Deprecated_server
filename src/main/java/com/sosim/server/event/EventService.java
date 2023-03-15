package com.sosim.server.event;

import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import java.util.List;

public interface EventService {

    Event getEvent(long id);
    Long createEvent(EventCreateReq eventCreateReq);

    EventInfo updateEvent(long id);

    void deleteEvent(long id);

    EventInfo changePaymentType(long id);

    List<EventInfo> getEvent(EventListReq eventListReq);
}
