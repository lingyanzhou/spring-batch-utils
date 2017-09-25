package lzhou.spring.batch.batchutils.database.statementsetter;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by lzhou on 9/21/2017.
 */
public class HashValuePartitionedPreparedStatementSetter implements PreparedStatementSetter {
    private int gridSize;
    private int gridId;

    public HashValuePartitionedPreparedStatementSetter(int gridSize, int gridId) {
        this.gridSize = gridSize;
        this.gridId = gridId;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, gridSize);
        preparedStatement.setInt(2, gridId);
    }
}
