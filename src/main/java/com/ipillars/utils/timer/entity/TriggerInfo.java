package com.ipillars.utils.timer.entity;

import lombok.Data;

import java.util.Map;

@Data
public class TriggerInfo {

    public enum TriggerTypeEnum {SIMPLE, CRON}

    private TriggerTypeEnum triggerType;
    private String name;
    private String groupName;
    private String cronExpression;
    private TimerInfo timer;
    private Map<String, Object> data;
}
