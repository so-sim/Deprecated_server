package com.sosim.server.event.dto.req;

public class EventListReq {

    // 기간 필터링
	private int month;
    private int week;
    private int day;

    // 팀원별 필터링
    private long userId;

    // 납부 여부 필터링
    private int paymentType;

    // 오늘 날짜 여부
    private boolean today;

}
