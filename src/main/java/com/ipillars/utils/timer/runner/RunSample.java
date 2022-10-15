package com.ipillars.utils.timer.runner;

import com.ipillars.utils.timer.entity.JobInfo;
import com.ipillars.utils.timer.entity.TimerInfo;
import com.ipillars.utils.timer.entity.TriggerInfo;
import com.ipillars.utils.timer.jobs.APIGetter;
import com.ipillars.utils.timer.jobs.HelloWorld;
import com.ipillars.utils.timer.service.SchedulingService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class RunSample {

    private final SchedulingService schedulingService;

    public RunSample(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    public void runHelloWorld() {

        Map <String, Object> data = new HashMap<>();
        // data.put("name", "Shriram");
        data.put("passedInName", "Shriram");
        data.put("passedInDesc", "Mani");
        data.put("passedInDate", (new Date()).toString());

        JobInfo jobInfo = new JobInfo();
        jobInfo.setName("HelloWorld");
        jobInfo.setGroupName("someGroup");
        jobInfo.setJobClass(HelloWorld.class);
//        jobInfo.setData(data);        Works well

        TimerInfo timerInfo = new TimerInfo();
        timerInfo.setInitialDelayMs(3000);
        timerInfo.setRefireCount(5);
        timerInfo.setRepeatForever(false);
        timerInfo.setIntervalMs(2000);

        TriggerInfo triggerInfo = new TriggerInfo();
        triggerInfo.setTriggerType(TriggerInfo.TriggerTypeEnum.SIMPLE);
        triggerInfo.setTimer(timerInfo);
        triggerInfo.setName("HelloWorldTrigger");
        triggerInfo.setGroupName("someGroup");
        triggerInfo.setData(data);

        schedulingService.schedule(jobInfo, triggerInfo);
    }

    public void apiCall() {

        JobInfo jobInfo = new JobInfo();
        jobInfo.setName("ApiCall");
        jobInfo.setGroupName("someGroup");
        jobInfo.setJobClass(APIGetter.class);

        TimerInfo timerInfo = new TimerInfo();
        timerInfo.setInitialDelayMs(3000);
        timerInfo.setRefireCount(5);
        timerInfo.setRepeatForever(false);
        timerInfo.setIntervalMs(2000);

        TriggerInfo triggerInfo = new TriggerInfo();
        /*
        triggerInfo.setTriggerType(TriggerInfo.TriggerTypeEnum.SIMPLE);
        triggerInfo.setTimer(timerInfo);
        triggerInfo.setName("APICallTrigger");
        triggerInfo.setGroupName("someGroup");
        */

        triggerInfo.setTriggerType(TriggerInfo.TriggerTypeEnum.CRON);
        triggerInfo.setCronExpression("* /1 * * * *");
        triggerInfo.setName("APICallTrigger");
        triggerInfo.setGroupName("someGroup");

        schedulingService.schedule(jobInfo, triggerInfo);
    }
/*
    public List<TimerInfo> getTimers() {
        return timerService.getAllRunningTimer();
    }

    public TimerInfo getTimer(String timerId) {
        return timerService.getTimer(timerId);
    }
*/
}

