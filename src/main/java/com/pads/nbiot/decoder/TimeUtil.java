package com.pads.nbiot.decoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static String getTimeString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-");
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.format(dtf).replaceFirst("-","T")
                .replaceFirst("-","Z");
    }

}
