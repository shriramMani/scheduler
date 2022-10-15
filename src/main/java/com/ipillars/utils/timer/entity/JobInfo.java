package com.ipillars.utils.timer.entity;

import lombok.Data;

import java.util.Map;

@Data
public class JobInfo {

    private String name;
    private String groupName;
    private Class jobClass;
    private Map<String, Object> data;
}
