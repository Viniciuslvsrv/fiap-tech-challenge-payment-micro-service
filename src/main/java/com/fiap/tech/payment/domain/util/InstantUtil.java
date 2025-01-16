package com.fiap.tech.payment.domain.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class InstantUtil {

    private InstantUtil() {
    }

    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }
}
