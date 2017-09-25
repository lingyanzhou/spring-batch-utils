package lzhou.spring.batch.batchutils.job.listener;

import lzhou.spring.batch.batchutils.util.JobExecutionUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.List;

/**
 * Created by lzhou on 9/17/2017.
 */
public class PerformanceJobExecutionListener implements JobExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceJobExecutionListener.class);

    private DateTime startTime = null;
    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = DateTime.now();
        logger.info("Job "+jobExecution.getJobInstance().getJobName()+" (ID "+jobExecution.getJobInstance().getInstanceId()+") starting at "+startTime);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        DateTime endTime = DateTime.now();
        String jobName = jobExecution.getJobInstance().getJobName();
        logger.info("Job "+jobName+" started at "+startTime+", ending at "+endTime+", took "+ (endTime.getMillis()-startTime.getMillis()+"-ms"));


        if (jobExecution.getStatus().isUnsuccessful()) {
            logger.info("The job (" + jobName + ") job execute failed");
            logger.info(JobExecutionUtil.getSummaryInfo(jobExecution).toString());
            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
            logger.info(" Job failed with following with "+exceptionList.size() + " excepton(s) \n");
            StringBuilder errors = new StringBuilder();
            for (Throwable t: exceptionList) {
                errors.append(ExceptionUtils.getStackTrace(t));
            }
            logger.info(errors.toString());

        } else {
            logger.info("The job (" + jobName + ") completed successfully");
            logger.info(JobExecutionUtil.getSummaryInfo(jobExecution).toString());
        }
    }
}
