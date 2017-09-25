package lzhou.spring.batch.batchutils.database.tasklet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;

public class SqlExecuteTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(SqlExecuteTasklet.class);

    private JdbcTemplate jdbcTemplate;

    private String sql;

    private PreparedStatementSetter preparedStatementSetter;


    public SqlExecuteTasklet(DataSource dataSource, String sql) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sql = sql;
        preparedStatementSetter = null;
    }

    public void setPreparedStatementSetter(PreparedStatementSetter preparedStatementSetter) {
        this.preparedStatementSetter = preparedStatementSetter;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            if (null == preparedStatementSetter) {
                int affected = jdbcTemplate.update(sql);
                contribution.incrementWriteCount(affected);
            } else {
                int affected = jdbcTemplate.update(sql, preparedStatementSetter);
                contribution.incrementWriteCount(affected);
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
