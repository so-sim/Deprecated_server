package com.sosim.server.event.dto.info;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthInfo {

    private String paymentType;
    private List<Map<Integer, Integer>> dayCountList;
}
