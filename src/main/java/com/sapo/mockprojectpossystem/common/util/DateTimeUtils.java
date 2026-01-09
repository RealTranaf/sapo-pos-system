package com.sapo.mockprojectpossystem.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class DateTimeUtils {
    public static Instant startOfDayUtc(LocalDate date) {
        return date
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
    }

    public static Instant endOfDayUtc(LocalDate date) {
        return date
                .plusDays(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .minusMillis(1);
    }
}
