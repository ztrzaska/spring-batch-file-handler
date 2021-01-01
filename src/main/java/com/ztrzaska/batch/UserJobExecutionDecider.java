package com.ztrzaska.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class UserJobExecutionDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String status = BatchStatus.FAILED.name();
        if (BatchStatus.COMPLETED == stepExecution.getStatus()) {
            status = BatchStatus.COMPLETED.name();
        }

        return new FlowExecutionStatus(status);
    }
}