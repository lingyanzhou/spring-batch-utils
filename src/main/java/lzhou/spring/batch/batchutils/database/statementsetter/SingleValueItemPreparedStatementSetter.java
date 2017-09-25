package lzhou.spring.batch.batchutils.database.statementsetter;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by lzhou on 9/21/2017.
 */
public class SingleValueItemPreparedStatementSetter<T> implements ItemPreparedStatementSetter<T> {
    private Class<T> requiredType;
    public SingleValueItemPreparedStatementSetter(Class<T> requiredType) {
        this.requiredType = requiredType;
    }

    @Override
    public void setValues(T t, PreparedStatement preparedStatement) throws SQLException {
        if (requiredType == String.class) {
            preparedStatement.setString(1, (String) t);
        } else if (requiredType == Long.class) {
            preparedStatement.setLong(1, (Long) t);
        } else {
            preparedStatement.setObject(1, t);
        }
    }
}
