package com.citypulse.application.mapper;

import com.citypulse.application.dto.response.DashboardResponse;
import org.springframework.stereotype.Component;

import java.util.function.BinaryOperator;
import java.util.function.Function;

@Component
public class DashboardMapper {

    private static final Function<Long, Long> identity = Function.identity();
    private static final BinaryOperator<Long> sum = Long::sum;

    public DashboardResponse toResponse(long total, long active, long resolved, long critical) {
        return new DashboardResponse(total, active, resolved, critical);
    }
}
