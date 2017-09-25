package lzhou.spring.batch.batchutils.database.statementsetter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by lzhou on 9/21/2017.
 */
public class SingleValuePreparedStatementSetter<T> implements PreparedStatementSetter {
    private static final Logger logger = LoggerFactory.getLogger(SingleValuePreparedStatementSetter.class);
    private T obj;
    public SingleValuePreparedStatementSetter(T obj) {
        this.obj = obj;
    }

    @Override
    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        logger.info(obj.getClass().getCanonicalName());
        if (obj.getClass() == String.class) {
            preparedStatement.setString(1, (String) obj);
        } else if (obj.getClass() == Long.class) {
            preparedStatement.setLong(1, (Long) obj);
        } else if (obj.getClass() == BigDecimal.class) {
            preparedStatement.setBigDecimal(1, (BigDecimal) obj);
        } else {
            preparedStatement.setObject(1, obj);
        }
    }
}
