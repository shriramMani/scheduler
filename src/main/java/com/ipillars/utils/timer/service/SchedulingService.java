package com.ipillars.utils.timer.service;

import com.ipillars.utils.timer.entity.JobInfo;
import com.ipillars.utils.timer.entity.TimerInfo;
import com.ipillars.utils.timer.entity.TriggerInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class SchedulingService {

    private final QuartzHookup quartzHookup;

    public SchedulingService(QuartzHookup quartzHookup) {
        this.quartzHookup = quartzHookup;
    }

    public void schedule(final JobInfo jobInfo, final TriggerInfo triggerInfo) {

        final JobDetail jobDetail = buildJobDetail(jobInfo);
        final Trigger jobTrigger = buildTrigger(triggerInfo);

        quartzHookup.schedule(jobDetail, jobTrigger);
    }

    public JobDetail buildJobDetail(final JobInfo jobInfo) {

        // Build the job and then add the parameters
        JobBuilder builder = JobBuilder
                .newJob(jobInfo.getJobClass())
                .withIdentity(jobInfo.getName(), jobInfo.getGroupName());

        // Add the parameters specific to this time (job)
        if (jobInfo.getData() != null && ! jobInfo.getData().isEmpty()) {
            JobDataMap jobDataMap = new JobDataMap();
            for (String key : jobInfo.getData().keySet()) {
                jobDataMap.put(key, jobInfo.getData().get(key));
            }
            builder.setJobData(jobDataMap);
        }

        return builder.build();
    }

    public Trigger buildTrigger(final TriggerInfo triggerInfo) {

        Trigger retval = null;

        // Build the trigger first and then add the parameters
        if (triggerInfo.getTriggerType().equals(TriggerInfo.TriggerTypeEnum.CRON)) {
            retval = buildCronTrigger(triggerInfo);
        } else if (triggerInfo.getTriggerType().equals(TriggerInfo.TriggerTypeEnum.SIMPLE)) {
            retval = buildSimpleTrigger(triggerInfo);
        }

        // Add the parameters specific to this time (trigger)
        if (retval != null &&
                triggerInfo.getData() != null &&
                ! triggerInfo.getData().isEmpty()) {
            JobDataMap triggerDataMap = retval.getJobDataMap();
            for (String key : triggerInfo.getData().keySet()) {
                triggerDataMap.put(key, triggerInfo.getData().get(key));
            }
        }

        return retval;
    }

    public Trigger buildSimpleTrigger(final TriggerInfo triggerInfo) {

        TimerInfo timerInfo = triggerInfo.getTimer();

        SimpleScheduleBuilder builder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInMilliseconds(timerInfo.getIntervalMs());

        if (timerInfo.isRepeatForever()) {
            builder.repeatForever();
        } else {
            builder.withRepeatCount(timerInfo.getRefireCount());
        }

        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerInfo.getName(), triggerInfo.getGroupName())
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis() + timerInfo.getInitialDelayMs()))
                .build();
    }

    public Trigger buildCronTrigger(final TriggerInfo triggerInfo) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerInfo.getName(), triggerInfo.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                // .startAt(new Date(System.currentTimeMillis() + timerInfo.getInitialDelayMs()))
                .build();
    }
}
