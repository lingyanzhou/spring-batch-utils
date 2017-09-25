package lzhou.spring.batch.batchutils.database.statementsetter;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by lzhou on 9/21/2017.
 */
public class RangePartitionedPreparedStatementSetter implements PreparedStatementSetter {
    private long minRange;
    private long maxRange;

    public RangePartitionedPreparedStatementSetter(long minRange, long maxRange) {
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, minRange);
        preparedStatement.setLong(2, maxRange);
    }
}
