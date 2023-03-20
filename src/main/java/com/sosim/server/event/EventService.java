package com.sosim.server.event;

import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.info.EventSingleInfo;
import com.sosim.server.event.dto.info.MonthInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import com.sosim.server.event.dto.req.EventModifyReq;
import com.sosim.server.event.dto.req.PaymentTypeReq;
import com.sosim.server.security.AuthUser;
import java.util.List;

public interface EventService {

    EventSingleInfo getEvent(long id);
    Long createEvent(AuthUser authUser, EventCreateReq eventCreateReq);

    EventInfo updateEvent(AuthUser authUser, long id, EventModifyReq eventModifyReq);

    void deleteEvent(AuthUser authUser, long id);

    EventInfo changePaymentType(AuthUser authUser, long id, PaymentTypeReq paymentTypeReq);

    List<EventInfo> getEventList(long groupId, EventListReq eventListReq);

    List<MonthInfo> getMonthInfo(long groupId, int month);
}
