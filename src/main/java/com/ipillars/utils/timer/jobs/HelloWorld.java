package com.ipillars.utils.timer.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloWorld implements Job {

    private final static String jobName = "HelloWorldJob";

    private String passedInName;
    private String passedInDesc;
    private String passedInDate;

    @Override
    public void execute(JobExecutionContext context) {

        log.info("Hello Shriram");


        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getMergedJobDataMap();
        log.info("Name is: {}, {}, {}", getPassedInName(), getPassedInDesc(), getPassedInDate());

        dataMap.forEach((k, v) -> System.out.println(k + ":" + v));

/*
        TimerInfo object = (TimerInfo) context
                .getJobDetail()
                .getJobDataMap()
                .get(HelloWorld.class.getSimpleName());

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        if (jobDataMap != null) {
            System.out.println("Name is:" + jobDataMap.get("name"));
        }
*/
    }

    public static String getJobName() {
        return jobName;
    }

    public String getPassedInName() {
        return passedInName;
    }

    public void setPassedInName(String passedInName) {
        this.passedInName = passedInName;
    }

    public String getPassedInDesc() {
        return passedInDesc;
    }

    public void setPassedInDesc(String passedInDesc) {
        this.passedInDesc = passedInDesc;
    }

    public String getPassedInDate() {
        return passedInDate;
    }

    public void setPassedInDate(String passedInDate) {
        this.passedInDate = passedInDate;
    }
}
