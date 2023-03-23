package com.sosim.server.event;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.info.EventSingleInfo;
import com.sosim.server.event.dto.info.ListInfo;
import com.sosim.server.event.dto.info.MonthInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import com.sosim.server.event.dto.req.EventModifyReq;
import com.sosim.server.event.dto.req.PaymentTypeReq;
import com.sosim.server.group.Group;
import com.sosim.server.group.GroupRepository;
import com.sosim.server.participant.Participant;
import com.sosim.server.participant.ParticipantRepository;
import com.sosim.server.security.AuthUser;
import com.sosim.server.type.CodeType;
import com.sosim.server.type.EventType;
import com.sosim.server.type.PaymentType;
import com.sosim.server.type.StatusType;
import com.sosim.server.type.UserType;
import com.sosim.server.user.User;
import com.sosim.server.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    @Override
    public EventSingleInfo getEvent(long id) {

        Event event = eventRepository.findByIdAndStatusType(id, StatusType.ACTIVE)
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_EVENT));
        Participant participant = participantRepository.findByUser(event.getUser())
            .orElseThrow(() -> new CustomException(CodeType.INVALID_USER));

        EventSingleInfo eventSingleInfo = EventSingleInfo.from(event);

        if (event.getGroup().getAdminId().equals(event.getUser().getId())) {
            eventSingleInfo.setAdminYn("true");
        } else {
            eventSingleInfo.setAdminYn("false");
        }
        eventSingleInfo.setUserName(participant.getNickname());
        return eventSingleInfo;
    }

    @Override
    public Long createEvent(AuthUser authUser, EventCreateReq eventCreateReq) {

        Optional<Participant> byNickName = participantRepository.findByNickname(eventCreateReq.getUserName());
        Participant participant = byNickName.orElseThrow(() -> new CustomException(CodeType.INVALID_USER));
        Long userId = participant.getUser().getId();
        Long groupId = participant.getGroup().getId();

        if (!participant.getGroup().getAdminId().equals(Long.parseLong(authUser.getId()))) {
            throw new CustomException(CodeType.INVALID_EVENT_CREATER);
        }

        LocalDateTime groundsDate = eventCreateReq.getGroundsDate();
        Long payment = eventCreateReq.getPayment();
        String grounds = eventCreateReq.getGrounds();
        PaymentType paymentType = PaymentType.getType(eventCreateReq.getPaymentType());
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
        StatusType statusType = StatusType.ACTIVE;
        EventType eventType = EventType.DUES_PAYMENT;

        Event event = Event.builder().groundsDate(groundsDate).payment(payment).grounds(grounds).paymentType(paymentType)
            .group(group).user(user).statusType(statusType).eventType(eventType).build();
        eventRepository.save(event);

        return event.getId();
    }


    @Override
    public EventInfo updateEvent(AuthUser authUser, long id, EventModifyReq eventModifyReq) {

        if (eventModifyReq.getUserName() == null && eventModifyReq.getGroundsDate() == null && ObjectUtils.isEmpty(eventModifyReq.getPayment())
            && eventModifyReq.getGrounds() == null && eventModifyReq.getPaymentType() == null) {
            throw new CustomException(CodeType.INPUT_ANY_DATA);
        }

        Event event = getActiveEvent(id);

        if (!event.getGroup().getAdminId().equals(Long.parseLong(authUser.getId()))) {
            throw new CustomException(CodeType.INVALID_EVENT_CREATER);
        }

        Participant participant = participantRepository.findByUser(event.getUser())
            .orElseThrow(() -> new CustomException(CodeType.INVALID_USER));

        if (eventModifyReq.getUserName() != null) {
            Participant participantforName = participantRepository.findByNickname(eventModifyReq.getUserName())
                .orElseThrow(() -> new CustomException(CodeType.INVALID_USER));
            User user = userRepository.findById(participantforName.getUser().getId())
                .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
            eventModifyReq.setUser(user);
        }

        event.updateEvent(eventModifyReq);
        eventRepository.save(event);

        EventInfo eventInfo = EventInfo.from(event);
        eventInfo.setUserName(participant.getNickname());

        return eventInfo;
    }

    @Override
    public void deleteEvent(AuthUser authUser, long id) {

        Event event = getActiveEvent(id);

        if (!event.getGroup().getAdminId().equals(Long.parseLong(authUser.getId()))) {
            throw new CustomException(CodeType.INVALID_EVENT_CREATER);
        }
        event.deleteEvent();
        eventRepository.save(event);
    }

    @Override
    public EventInfo changePaymentType(AuthUser authUser, long id, PaymentTypeReq paymentTypeReq) {

        Event event = getActiveEvent(id);

        if (!event.getGroup().getAdminId().equals(Long.parseLong(authUser.getId()))) {
            throw new CustomException(CodeType.INVALID_EVENT_CREATER);
        }

        Participant participant = participantRepository.findByUser(event.getUser())
            .orElseThrow(() -> new CustomException(CodeType.INVALID_USER));

        event.changePaymentType(paymentTypeReq);
        eventRepository.save(event);

        EventInfo eventInfo = EventInfo.from(event);
        eventInfo.setUserName(participant.getNickname());

        return eventInfo;
    }

    @Override
    public ListInfo<EventInfo> getEventList(long groupId, EventListReq eventListReq) {

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));

        if (eventListReq.getUserId() != null) {
            User user = userRepository.findByIdAndUserType(eventListReq.getUserId(), UserType.ACTIVE).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
            PageRequest pageRequest = PageRequest.of(0, 16, Sort.by(Direction.DESC, "update_date"));
            Page<Event> page = eventRepository.findByGroupAndUserAndStatusType(group, user,
                StatusType.ACTIVE, pageRequest);
            page.getContent();
        }
        return null;
    }

    @Override
    public List<MonthInfo> getMonthInfo(long groupId, int month) {
        List<MonthInfo> monthList = new ArrayList<>();
        // 필요한 데이터 : day가 몇일인지, 그리고 몇개인지
        // 1, 3
        // 2, 6


        List<PaymentType> paymentTypeList = List.of(PaymentType.values());
        monthList = paymentTypeList.stream().map(paymentType -> getMonthInfo(paymentType, getPaymentList(groupId, paymentType, month))).collect(Collectors.toList());
        return monthList;
    }

    private Event getActiveEvent(long id) {
        return eventRepository.findByIdAndStatusType(id, StatusType.ACTIVE)
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_EVENT));
    }

    private MonthInfo getMonthInfo(PaymentType paymentType, List<Map<Integer, Integer>> dayCountList) {
        return MonthInfo.builder().paymentType(paymentType.getParam()).dayCountList(dayCountList).build();
    }

    private List<Map<Integer, Integer>> getPaymentList(long groupId, PaymentType paymentType, int month) {

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));

        List<Event> byPaymentTypeAndStatusType = eventRepository.findByPaymentTypeAndStatusTypeAndGroup(
            paymentType, StatusType.ACTIVE, group);

        List<Event> paymentList = byPaymentTypeAndStatusType.stream()
            .filter(event -> event.getCreateDate().getMonthValue() == month).collect(
                Collectors.toList());

        List<Map<Integer, Integer>> paymentDayCountList = paymentList.stream().map(event -> {
            Map<Integer, Integer> dayCount = new HashMap<>();
            int dayOfMonth = event.getCreateDate().getDayOfMonth();
            int count = (int) paymentList.stream().filter(y -> y.getCreateDate().getDayOfMonth() == dayOfMonth)
                .count();
            dayCount.put(dayOfMonth, count);
            log.info("dayCount : {}", dayCount);
            return dayCount;
        }).collect(Collectors.toList());
        return paymentDayCountList;
    }

}
