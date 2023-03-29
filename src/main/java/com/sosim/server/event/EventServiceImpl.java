package com.sosim.server.event;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.event.dto.info.DayInfo;
import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.info.EventListInfo;
import com.sosim.server.event.dto.info.EventSingleInfo;
import com.sosim.server.event.dto.info.ListInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import com.sosim.server.event.dto.req.EventModifyReq;
import com.sosim.server.event.dto.req.MonthlyDayPaymentTypeReq;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate localDate = LocalDate.parse(eventCreateReq.getGroundsDate(), dateTimeFormatter);
        LocalDateTime groundsDatetime = LocalDateTime.of(localDate, LocalTime.of(0,0,0));
        Long payment = eventCreateReq.getPayment();
        String grounds = eventCreateReq.getGrounds();
        PaymentType paymentType = PaymentType.getType(eventCreateReq.getPaymentType());
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
        StatusType statusType = StatusType.ACTIVE;
        EventType eventType = EventType.DUES_PAYMENT;

        Event event = Event.builder().groundsDate(groundsDatetime).payment(payment).grounds(grounds).paymentType(paymentType)
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
            if (event.getUser().getId().equals(Long.parseLong(authUser.getId()))) {
                if (!event.getPaymentType().equals(PaymentType.NON_PAYMENT) ||
                    !paymentTypeReq.getPaymentType().equals("con")) {
                    throw new CustomException(CodeType.PAYMENT_TYPE_MUST_BE_NON);
                }
            } else {
                throw new CustomException(CodeType.INVALID_PAYMENT_TYPE_CHANGER);
            }
        }

        Participant participant = participantRepository.findByUserAndGroupAndStatusType(event.getUser(), event.getGroup(), StatusType.ACTIVE)
            .orElseThrow(() -> new CustomException(CodeType.INVALID_USER));

        event.changePaymentType(paymentTypeReq);
        eventRepository.save(event);

        EventInfo eventInfo = EventInfo.from(event);
        eventInfo.setUserName(participant.getNickname());

        return eventInfo;
    }

    @Override
    public ListInfo<EventListInfo> getEventList(long groupId, EventListReq eventListReq) {

        if (eventListReq.getPage() == null) {
            throw new CustomException(CodeType.INPUT_PAGE_DATA);
        }

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));

        long totalCount = 0;
        List<Event> eventList = null;
        List<EventListInfo> eventInfoList = null;
        PageRequest pageRequest = PageRequest.of(eventListReq.getPage(), 16, Sort.by(Direction.ASC, "groundsDate"));
        Page<Event> page = null;
        LocalDateTime startDatetime = null;
        LocalDateTime endDatetime = null;

        if (eventListReq.getYear() == null && eventListReq.getUserId() == null
            && eventListReq.getPaymentType() == null && eventListReq.getToday() == null) {
            page = eventRepository.findByGroupAndStatusType(group, StatusType.ACTIVE, pageRequest);
            eventInfoList = getEventInfoList(group, page);
            totalCount = eventRepository.countByGroupAndStatusType(group, StatusType.ACTIVE);
        }

        if (eventListReq.getYear() != null && eventListReq.getMonth() != null && eventListReq.getWeek() == null) {
            startDatetime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), 1, 0, 0);
            boolean isLeapYear = startDatetime.toLocalDate().isLeapYear();
            int endDay = getEndDayOfMonth(isLeapYear, eventListReq.getMonth());
            endDatetime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), endDay, 23, 59);
            page = eventRepository.findByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime, pageRequest);
            eventInfoList = getEventInfoList(group, page);
            totalCount = eventRepository.countByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime);
        }

        if (eventListReq.getYear() != null && eventListReq.getMonth() != null && eventListReq.getWeek() != null) {
            LocalDateTime localDateTime = null;
            switch (eventListReq.getWeek()) {
                case 1:
                    localDateTime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), 1, 0, 0);
                break;
                case 2:
                    localDateTime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), 8, 0, 0);
                break;
                case 3:
                    localDateTime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), 15, 0, 0);
                    break;
                case 4:
                    localDateTime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), 23, 0, 0);
                    break;
                case 5:
                    boolean isLeapYear = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), 28, 0, 0).toLocalDate().isLeapYear();
                    int endDay = getEndDayOfMonth(isLeapYear, eventListReq.getMonth());
                    localDateTime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), endDay, 0, 0);
                    break;

            }
            if (eventListReq.getWeek().equals(5)) {
                int plusDay = 6 - localDateTime.getDayOfWeek().getValue();
                endDatetime = localDateTime.plusDays(plusDay);
                startDatetime = endDatetime.minusDays(6);
                log.info("startDateTime:{}", startDatetime);
                log.info("endDateTime:{}", endDatetime);
            } else {
                int minusDay = localDateTime.getDayOfWeek().getValue();
                startDatetime = localDateTime.minusDays(minusDay);
                endDatetime = startDatetime.plusDays(6);
                log.info("startDateTime:{}", startDatetime);
                log.info("endDateTime:{}", endDatetime);
            }
            page = eventRepository.findByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime, pageRequest);
            eventInfoList = getEventInfoList(group, page);
            totalCount = eventRepository.countByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime);
        }

        if (eventListReq.getYear() != null && eventListReq.getMonth() != null && eventListReq.getDay() != null) {
            startDatetime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), eventListReq.getDay(), 0, 0, 0);
            endDatetime = LocalDateTime.of(eventListReq.getYear(), eventListReq.getMonth(), eventListReq.getDay(),23,59, 59);
            pageRequest = PageRequest.of(eventListReq.getPage(), 16, Sort.by(Direction.DESC, "createDate"));
            page = eventRepository.findByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime, pageRequest);
            eventInfoList = getEventInfoList(group, page);
            totalCount = eventRepository.countByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime);
        }

        if (eventListReq.getUserId() != null) {
            User user = userRepository.findByIdAndUserType(eventListReq.getUserId(), UserType.ACTIVE).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
            Participant participant = participantRepository.findByUserAndGroupAndStatusType(user, group, StatusType.ACTIVE).orElseThrow(() -> new CustomException(CodeType.INVALID_USER));
            page = eventRepository.findByGroupAndUserAndStatusType(group, user, StatusType.ACTIVE, pageRequest);
            eventList = page.getContent();
            eventInfoList = eventList.stream().map(x -> {
                EventListInfo eventlistInfo = EventListInfo.from(x);
                eventlistInfo.setUserName(participant.getNickname());
                return eventlistInfo;
            }).collect(Collectors.toList());
            totalCount = eventRepository.countByGroupAndUserAndStatusType(group, user, StatusType.ACTIVE);
        }

        if (eventListReq.getToday() != null && eventListReq.getToday().equals("true")) {
            startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
            endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
            pageRequest = PageRequest.of(eventListReq.getPage(), 16, Sort.by(Direction.DESC, "createDate"));
            page = eventRepository.findByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime, pageRequest);
            eventInfoList = getEventInfoList(group, page);
            totalCount = eventRepository.countByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime);
        }

        if (eventListReq.getPaymentType() != null) {
            page = eventRepository.findByGroupAndPaymentTypeAndStatusType(group, PaymentType.getType(eventListReq.getPaymentType()), StatusType.ACTIVE, pageRequest);
            eventInfoList = getEventInfoList(group, page);
            totalCount = eventRepository.countByGroupAndPaymentTypeAndStatusType(group, PaymentType.getType(eventListReq.getPaymentType()), StatusType.ACTIVE);
        }

        return ListInfo.from(totalCount, eventInfoList);
    }


    private List<EventListInfo> getEventInfoList(Group group, Page<Event> page) {
        List<Event> eventList;
        List<EventListInfo> eventInfoList;
        eventList = page.getContent();
        eventInfoList = eventList.stream().map(x -> {
            EventListInfo eventlistInfo = EventListInfo.from(x);
            Participant participant = participantRepository.findByUserAndGroupAndStatusType(x.getUser(), group, StatusType.ACTIVE).orElseThrow(() -> new CustomException(CodeType.INVALID_USER));
            eventlistInfo.setUserName(participant.getNickname());
            return eventlistInfo;
        }).collect(Collectors.toList());
        return eventInfoList;
    }

    int getEndDayOfMonth(boolean isLeapYear, int month) {
        int endDay = 0;
        if (isLeapYear) {
            switch (month) {
                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                    endDay = 31;
                    break;
                case 4: case 6: case 9: case 11:
                    endDay = 30;
                    break;
                case 2:
                    endDay = 29;
                    break;
            }
        } else {
                switch (month) {
                    case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                        endDay = 31;
                        break;
                    case 4: case 6: case 9: case 11:
                        endDay = 30;
                        break;
                    case 2:
                        endDay = 28;
                        break;
            }
        }
        return endDay;
    }

    @Override
    public List<DayInfo> getMonthlyDayPaymentType(long groupId, MonthlyDayPaymentTypeReq mdpTreq) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));
        LocalDateTime startDatetime = LocalDateTime.of(mdpTreq.getYear(), mdpTreq.getMonth(), 1, 0, 0);
        boolean isLeapYear = startDatetime.toLocalDate().isLeapYear();
        int endDay = getEndDayOfMonth(isLeapYear, mdpTreq.getMonth());
        LocalDateTime endDatetime = LocalDateTime.of(mdpTreq.getYear(), mdpTreq.getMonth(), endDay, 23, 59);
        List<Event> eventList = eventRepository.findByGroupAndStatusTypeAndGroundsDateBetween(group, StatusType.ACTIVE, startDatetime, endDatetime);

        List<DayInfo> monthlyDayInfoList = eventList.stream().map(x -> {
            Map<String, Integer> paymentTypeCountMap = new HashMap<>();
            int dayOfMonth = x.getGroundsDate().getDayOfMonth();
            int nonCount = (int) eventList.stream().filter(y -> (y.getPaymentType().equals(PaymentType.NON_PAYMENT))
                && y.getGroundsDate().getDayOfMonth() == dayOfMonth).count();
            int conCount = (int) eventList.stream().filter(y -> (y.getPaymentType().equals(PaymentType.CONFIRMING))
                && y.getGroundsDate().getDayOfMonth() == dayOfMonth).count();
            int fullCount = (int) eventList.stream().filter(y -> (y.getPaymentType().equals(PaymentType.FULL_PAYMENT))
                && y.getGroundsDate().getDayOfMonth() == dayOfMonth).count();
            paymentTypeCountMap.put("non", nonCount);
            paymentTypeCountMap.put("con", conCount);
            paymentTypeCountMap.put("full", fullCount);
            return DayInfo.builder().day(dayOfMonth).paymentTypeCountMap(paymentTypeCountMap).build();
        }).filter(distinctByKey(m -> m.getDay())).sorted( new Comparator<DayInfo>() {
            public int compare(DayInfo o1, DayInfo o2) {
                return Long.compare(o1.getDay(), o2.getDay());
                }
        }).collect(Collectors.toList());
        monthlyDayInfoList.stream().filter(distinctByKey(m -> m.getDay())).collect(Collectors.toList());
        return monthlyDayInfoList;
    }
    public static <T> Predicate<T> distinctByKey( Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private Event getActiveEvent(long id) {
        return eventRepository.findByIdAndStatusType(id, StatusType.ACTIVE)
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_EVENT));
    }

//    private DayInfo getMonthInfo(PaymentType paymentType, List<Map<Integer, Integer>> dayCountList) {
//        return DayInfo.builder().paymentType(paymentType.getParam()).dayCountList(dayCountList).build();
//    }

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
