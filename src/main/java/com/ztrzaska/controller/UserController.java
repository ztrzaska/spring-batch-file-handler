package com.ztrzaska.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping("/get")
    public BatchStatus get() throws JobExecutionException {
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
        return jobExecution.getStatus();
    }
}
