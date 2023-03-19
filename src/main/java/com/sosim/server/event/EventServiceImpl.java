package com.sosim.server.event;

import com.sosim.server.config.exception.CustomException;
import com.sosim.server.event.dto.info.EventInfo;
import com.sosim.server.event.dto.info.EventSingleInfo;
import com.sosim.server.event.dto.info.MonthInfo;
import com.sosim.server.event.dto.req.EventCreateReq;
import com.sosim.server.event.dto.req.EventListReq;
import com.sosim.server.event.dto.req.EventModifyReq;
import com.sosim.server.event.dto.req.PaymentTypeReq;
import com.sosim.server.group.Group;
import com.sosim.server.group.GroupRepository;
import com.sosim.server.participant.Participant;
import com.sosim.server.participant.ParticipantRepository;
import com.sosim.server.type.CodeType;
import com.sosim.server.type.EventType;
import com.sosim.server.type.PaymentType;
import com.sosim.server.type.StatusType;
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

        EventSingleInfo eventSingleInfo = new EventSingleInfo();
        Event event = eventRepository.findByIdAndStatusType(id, StatusType.USING)
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_EVENT));
        Participant participant = participantRepository.findByUser(event.getUser())
            .orElseThrow(() -> new CustomException(CodeType.INVALID_USER));

        // 해당 이벤트의, 그룹의 admin id와 이 사람의 id를 비교
        if ( event.getGroup().getAdminId().equals(event.getUser().getId())) {
            eventSingleInfo.setAdminYn("true");
        } else {
            eventSingleInfo.setAdminYn("false");
        }

        eventSingleInfo.setUserName(participant.getNickname());
        eventSingleInfo.setPayment(event.getPayment());
        eventSingleInfo.setGroundsDate(event.getGroundsDate());
        eventSingleInfo.setPaymentType(event.getPaymentType().getParam());
        eventSingleInfo.setGrounds(event.getGrounds());
        return eventSingleInfo;
    }

    @Override
    public Long createEvent(EventCreateReq eventCreateReq) {

        Optional<Participant> byNickName = participantRepository.findByNickname(eventCreateReq.getUserName());
        Participant participant = byNickName.orElseThrow(() -> new CustomException(CodeType.INVALID_USER));
        Long userId = participant.getUser().getId();
        Long groupId = participant.getGroup().getId();

        LocalDateTime groundsDate = eventCreateReq.getGroundsDate();
        Long payment = eventCreateReq.getPayment();
        String grounds = eventCreateReq.getGrounds();
        PaymentType paymentType = PaymentType.getType(eventCreateReq.getPaymentType());
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
        StatusType statusType = StatusType.USING;
        EventType eventType = EventType.DUES_PAYMENT;

        Event event = Event.builder().groundsDate(groundsDate).payment(payment).grounds(grounds).paymentType(paymentType)
            .group(group).user(user).statusType(statusType).eventType(eventType).build();
        eventRepository.save(event);

        return event.getId();
    }


    @Override
    public EventInfo updateEvent(long id, EventModifyReq eventModifyReq) {

        if (eventModifyReq.getUserName() == null && eventModifyReq.getGroundsDate() == null && ObjectUtils.isEmpty(eventModifyReq.getPayment())
            && eventModifyReq.getGrounds() == null && eventModifyReq.getPaymentType() == null) {
            throw new CustomException(CodeType.COMMON_BAD_REQUEST);
        }

        Event event = eventRepository.findByIdAndStatusType(id, StatusType.USING).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_EVENT));

        if (eventModifyReq.getUserName() != null) {
            Participant participant = participantRepository.findByNickname(eventModifyReq.getUserName())
                .orElseThrow(() -> new CustomException(CodeType.INVALID_USER));
            User user = userRepository.findById(participant.getUser().getId())
                .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_USER));
            eventModifyReq.setUser(user);
        }

        event.updateEvent(eventModifyReq);
        eventRepository.save(event);

        return EventInfo.builder().userName(eventModifyReq.getUserName()).payment(event.getPayment())
            .groundsDate(event.getGroundsDate()).paymentType(event.getPaymentType().getParam())
            .grounds(event.getGrounds()).build();
    }

    @Override
    public void deleteEvent(long id) {
        Event event = eventRepository.findByIdAndStatusType(id, StatusType.USING)
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_EVENT));
        event.deleteEvent();
        eventRepository.save(event);
    }

    @Override
    public EventInfo changePaymentType(long id, PaymentTypeReq paymentTypeReq) {

        Event event = eventRepository.findByIdAndStatusType(id, StatusType.USING)
            .orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_EVENT));
        Participant participant = participantRepository.findByUser(event.getUser())
            .orElseThrow(() -> new CustomException(CodeType.INVALID_USER));

        event.changePaymentType(paymentTypeReq);
        eventRepository.save(event);

        return EventInfo.builder().userName(participant.getNickname()).payment(event.getPayment())
            .groundsDate(event.getGroundsDate()).paymentType(event.getPaymentType().getParam())
            .grounds(event.getGrounds()).build();
    }

    @Override
    public List<EventInfo> getEventList(long groupId, EventListReq eventListReq) {
        return null;
    }

    @Override
    public List<MonthInfo> getMonthInfo(long groupId, int month) {
        List<MonthInfo> monthList = new ArrayList<>();
        // 필요한 데이터 : day가 몇일인지, 그리고 몇개인지
        // 1, 3
        // 2, 6

//        findByPaymentTypeAndStatusTypeAndCreateDateBetween();
//        List<Event> byPaymentTypeAndStatusType = eventRepository.findByPaymentTypeAndStatusType(
//            PaymentType.NON_PAYMENT, StatusType.USING);
//
//        List<Event> monthNonpaymentList = byPaymentTypeAndStatusType.stream()
//            .filter(event -> event.getCreateDate().getMonthValue() == month).collect(
//                Collectors.toList());


//        List<Map<Integer, Integer>> nonPaymentDayCountList = getPaymentList(PaymentType.NON_PAYMENT, month);
//        List<Map<Integer, Integer>> conPaymentDayCountList = getPaymentList(PaymentType.CONFIRMING, month);
//        List<Map<Integer, Integer>> fullPaymentDayCountList = getPaymentList(PaymentType.FULL_PAYMENT, month);

//        List<Map<Integer, Integer>> nonPaymentDayCountList1 = monthNonpaymentList.stream().map(event -> {
//            Map<Integer, Integer> dayCount = new HashMap<>();
//            int dayOfMonth = event.getCreateDate().getDayOfMonth();
//            int count = (int) monthNonpaymentList.stream().filter(y -> y.getCreateDate().getDayOfMonth() == dayOfMonth)
//                .count();
//            dayCount.put(dayOfMonth, count);
//            log.info("dayCount : {}", dayCount);
//            return dayCount;
//        }).collect(Collectors.toList());

//        MonthInfo nonMonthInfo = MonthInfo.builder().paymentType(PaymentType.NON_PAYMENT.getParam()).dayCountList(nonPaymentDayCountList).build();
//        MonthInfo conMonthInfo = MonthInfo.builder().paymentType(PaymentType.CONFIRMING.getParam()).dayCountList(conPaymentDayCountList).build();
//        MonthInfo fullMonthInfo = MonthInfo.builder().paymentType(PaymentType.FULL_PAYMENT.getParam()).dayCountList(fullPaymentDayCountList).build();
//        monthList.add(nonMonthInfo);
//        monthList.add(conMonthInfo);
//        monthList.add(fullMonthInfo);
//        monthList.add(getMonthInfo(PaymentType.NON_PAYMENT, nonPaymentDayCountList));
//        monthList.add(getMonthInfo(PaymentType.CONFIRMING, conPaymentDayCountList));
//        monthList.add(getMonthInfo(PaymentType.FULL_PAYMENT, fullPaymentDayCountList));

        List<PaymentType> paymentTypeList = List.of(PaymentType.values());
        monthList = paymentTypeList.stream().map(paymentType -> getMonthInfo(paymentType, getPaymentList(groupId, paymentType, month))).collect(Collectors.toList());
        return monthList;
    }

    private MonthInfo getMonthInfo(PaymentType paymentType, List<Map<Integer, Integer>> dayCountList) {
        return MonthInfo.builder().paymentType(paymentType.getParam()).dayCountList(dayCountList).build();
    }

    private List<Map<Integer, Integer>> getPaymentList(long groupId, PaymentType paymentType, int month) {

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new CustomException(CodeType.NOT_FOUND_GROUP));

        List<Event> byPaymentTypeAndStatusType = eventRepository.findByPaymentTypeAndStatusTypeAndGroup(
            paymentType, StatusType.USING, group);

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
