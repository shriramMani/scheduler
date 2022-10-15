package com.ipillars.utils.timer.controller;

import com.ipillars.utils.timer.entity.TimerInfo;
import com.ipillars.utils.timer.runner.RunSample;
import com.ipillars.utils.timer.service.QuartzHookup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/run/job")
public class RunJobController {

    private final RunSample jobRunnerService;

    private final QuartzHookup quartzHookup;

    public RunJobController(RunSample jobRunnerService, QuartzHookup quartzHookup) {
        this.jobRunnerService = jobRunnerService;
        this.quartzHookup = quartzHookup;
    }

    @GetMapping
    public List<TimerInfo> getAllRunningTimers() {
        return quartzHookup.getAllRunningTimer();
    }

    @GetMapping("/timerId")
    public TimerInfo getTimerById(@PathVariable String timerId) {
        return quartzHookup.getTimer(timerId);
    }

    @PostMapping("/helloWorld")
    public void runHelloWord() {
        jobRunnerService.runHelloWorld();
    }

    @PostMapping("/apiCall")
    public void apiCall() {
        jobRunnerService.apiCall();
    }
}
