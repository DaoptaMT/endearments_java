package mt.endearments.batch.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class EndearmentJobCompletionNotificationListener implements JobExecutionListener {
    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        log.info("Import job is starting...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("import job completed successfully!");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("import job failed with status: {}", jobExecution.getStatus());
            log.error("Exception: {}", jobExecution.getAllFailureExceptions());
        }

        String duration = "N/A";
        if (jobExecution.getStartTime() != null && jobExecution.getEndTime() != null) {
            Duration jobDuration = Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime());
            duration = jobDuration.toMillis() + " ms";
        }

        log.info("Job ID: {}, Job Name: {}, Job Status: {}, Start Time: {}, End Time: {}, Duration: {}",
                jobExecution.getJobId(),
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(),
                jobExecution.getStartTime(),
                jobExecution.getEndTime(),
                duration);
    }
}
