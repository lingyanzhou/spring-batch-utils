package lzhou.spring.batch.batchutils.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import java.util.Collection;
import java.util.List;

/**
 * Created by lzhou on 8/24/2017.
 */
public class JobExecutionUtil {
    public static StringBuilder getSummaryInfo(JobExecution jobExecution) {
        StringBuilder textBuilder = new StringBuilder();

        textBuilder.append("Job Summary for: ");
        textBuilder.append(jobExecution.getJobInstance().getJobName());

        textBuilder.append("\n Job Instance ID: ");
        textBuilder.append(jobExecution.getJobInstance().getInstanceId());

        textBuilder.append("\n Job Status: ");
        textBuilder.append(jobExecution.getStatus()); //jobExecution.getStatus().name()

        textBuilder.append("\n Job Exit Code: ");
        textBuilder.append(jobExecution.getExitStatus().getExitCode());

        textBuilder.append("\n Job Exit Description: ");
        textBuilder.append(jobExecution.getExitStatus().getExitDescription());

        textBuilder.append("\n Job Start Time: ");
        textBuilder.append(jobExecution.getCreateTime().toString());
        textBuilder.append("\n Job End Time: ");
        textBuilder.append(jobExecution.getEndTime().toString());

        List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
        textBuilder.append(" Job failed with following with "+exceptionList.size() + " excepton(s) \n");
        for (Throwable t: exceptionList) {
            textBuilder.append(ExceptionUtils.getStackTrace(t));
        }

        textBuilder.append("\n Detailed Step Status: ");
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        for (StepExecution stepExec : stepExecutions) {
            textBuilder.append(getStepSummaryInfo(stepExec));
        }
        return textBuilder;
    }

    public static StringBuilder getStepSummaryInfo(StepExecution stepExecution) {
        StringBuilder textBuilder = new StringBuilder();

        textBuilder.append("Step Summary for: ");
        textBuilder.append(stepExecution.getStepName());

        textBuilder.append("\n Step Exit Status: ");
        textBuilder.append(stepExecution.getExitStatus().getExitCode());

        textBuilder.append("\n Step Exit Description: ");
        textBuilder.append(stepExecution.getExitStatus().getExitDescription());

        textBuilder.append("\n Step Read Counts: ");
        textBuilder.append(stepExecution.getReadCount());
        textBuilder.append("\n Step Write Counts: ");
        textBuilder.append(stepExecution.getWriteCount());
        textBuilder.append("\n Step Filtered Counts: ");
        textBuilder.append(stepExecution.getFilterCount());
        textBuilder.append("\n Step Skipped Counts: ");
        textBuilder.append(stepExecution.getSkipCount());

        textBuilder.append("\n Detailed Step Status: ");
        List<Throwable> failures = stepExecution.getFailureExceptions();
        if (failures!=null && failures.size()>0) {
            textBuilder.append("Failure exceptions: "+failures.size());
            for (Throwable f : failures) {
                textBuilder.append(ExceptionUtils.getStackTrace(f));
            }
        }
        return textBuilder;
    }
}
