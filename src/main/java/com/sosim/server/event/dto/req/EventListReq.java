package com.sosim.server.event.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventListReq {

    // 기간 필터링
    private Integer year;
	private Integer month;
    private Integer week;

    // 일별 필터링
    private Integer day;

    // 팀원별 필터링
    private String nickname;

    // 납부 여부 필터링
    private String paymentType;

    // 오늘 날짜 여부
//    private Boolean today;
    private String today;

    // 현재 페이지
    private Integer page;

}
