package com.ipillars.utils.timer.service;

import com.ipillars.utils.timer.entity.TimerInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuartzHookup {

    private final Scheduler scheduler;

    public QuartzHookup(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(JobDetail jobDetail, Trigger jobTrigger) {
        try {
            scheduler.scheduleJob(jobDetail, jobTrigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<TimerInfo> getAllRunningTimer() {

        List<TimerInfo> retval = Collections.emptyList();
        try {
            retval = scheduler.getJobKeys(GroupMatcher.anyGroup())
                    .stream()
                    .map(jobKey -> {
                        try {
                            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            return (TimerInfo) jobDetail.getJobDataMap().get(jobKey.getName());
                        } catch (SchedulerException e) {
                            log.error(e.getMessage(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
        return retval;
    }

    public TimerInfo getTimer(String timerId) {
        try {
        final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
        return (TimerInfo) jobDetail.getJobDataMap().get(timerId);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @PostConstruct
    public void startup() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
}
