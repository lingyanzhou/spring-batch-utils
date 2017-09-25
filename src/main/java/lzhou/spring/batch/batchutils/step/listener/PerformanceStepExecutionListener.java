package lzhou.spring.batch.batchutils.step.listener;

import lzhou.spring.batch.batchutils.util.JobExecutionUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Created by lzhou on 9/17/2017.
 */
public class PerformanceStepExecutionListener implements StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceStepExecutionListener.class);
    private DateTime startTime = null;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        startTime = DateTime.now();
        logger.info("Step "+stepExecution.getStepName()+" starting at "+startTime);

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        DateTime endTime = DateTime.now();
        logger.info("Job "+stepExecution.getStepName()+" started at "+startTime+", ending at "+endTime+", took "+ (endTime.getMillis()-startTime.getMillis()+"-ms"));

        StringBuilder sb =  JobExecutionUtil.getStepSummaryInfo(stepExecution);

        logger.info(sb.toString());

        return stepExecution.getExitStatus();
    }
}
