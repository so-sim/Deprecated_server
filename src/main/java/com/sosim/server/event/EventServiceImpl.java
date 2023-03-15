package com.sosim.server.event;

import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService{

    @Override
    public Event getEvent(long id) {
        //TODO 관리자인지 아닌지 비교하는 로직 필요
        return null;
    }

    @Override
    public Long createEvent(EventCreateReq eventCreateReq) {
        //TODO 팀원 이름을 받을건데, participant 테이블에서 이름 조회하고 해당 userId로 찾아서?
        // 이거랑 다른 방식 하나 중 둘 중 하나 선택
        return null;
    }


    @Override
    public EventInfo updateEvent(long id) {
        return null;
    }

    @Override
    public void deleteEvent(long id) {

    }

    @Override
    public EventInfo changePaymentType(long id) {
        return null;
    }

    @Override
    public List<EventInfo> getEvent(EventListReq eventListReq) {
        return null;
    }

}
