package com.ipillars.utils.timer.entity;

import lombok.Data;

@Data
public class TimerInfo {

    private int refireCount = 0;
    private boolean repeatForever;
    private long intervalMs;
    private long initialDelayMs;
}
