package lzhou.spring.batch.batchutils.database.tasklet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class SqlQueryTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(SqlQueryTasklet.class);

    private JdbcTemplate jdbcTemplate;

    private String sql;
    private Object[] args;
    private int[] argTypes;


    public SqlQueryTasklet(DataSource dataSource, String sql) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sql = sql;
        args = null;
        argTypes = null;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setArgTypes(int[] argTypes) {
        this.argTypes = argTypes;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            if (null == args) {
                Map<String, Object> result = jdbcTemplate.queryForMap(sql);
                if (null!=result) {
                    for (Map.Entry<String, Object> kv: result.entrySet()) {
                        logger.info("Setting key in step context {}.",kv.getKey());
                        chunkContext.getStepContext().getStepExecution().getExecutionContext().put(kv.getKey(), kv.getValue());
                    }
                }
            } else {
                Map<String, Object> result = jdbcTemplate.queryForMap(sql, args, argTypes);

                if (null!=result) {
                    for (Map.Entry<String, Object> kv : result.entrySet()) {
                        logger.info("Setting key in step context {}.",kv.getKey());
                        chunkContext.getStepContext().getStepExecution().getExecutionContext().put(kv.getKey(), kv.getValue());
                    }
                }
            }
        } catch (Exception e) {
            logger.info("SqlExecuteTasklet failed.");
            logger.info("\tThe query: {}.", sql);
            logger.info("\tThe exceptions: {}.", ExceptionUtils.getStackTrace(e));
            throw e;
        }

        logger.info("SimpleSqlTasklet END.");

        return RepeatStatus.FINISHED;
    }
}
