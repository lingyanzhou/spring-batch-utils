package lzhou.spring.batch.batchutils.database.partitioner;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzhou on 9/20/2017.
 */
public class TimestampColumnPartitioner implements Partitioner {
    private String sqlMin;
    private String sqlMax;

    private long defaultTime;
    private JdbcTemplate jdbcTemplate;

    public TimestampColumnPartitioner(String sqlMin, String sqlMax,DataSource dataSource) {
        this.sqlMin = sqlMin;
        this.sqlMax = sqlMax;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        defaultTime = 0;
    }

    public void setDefaultTime(long defaultTime) {
        this.defaultTime = defaultTime;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result
                = new HashMap<String, ExecutionContext>();

        final Timestamp minRangeObj = jdbcTemplate.queryForObject(sqlMin, Timestamp.class);
        final Timestamp maxRangeObj = jdbcTemplate.queryForObject(sqlMax, Timestamp.class);
        final long minRange = null == minRangeObj ? defaultTime: minRangeObj.getTime();
        final long maxRangeExcl = null == maxRangeObj ? defaultTime: maxRangeObj.getTime();
        final long range = (maxRangeExcl - minRange);
        final long step = (long)(range/gridSize);
        final long firstStep = range - (gridSize - 1) * step;

        for (int i=0; i<gridSize && i<1; ++i) { // simple the first one, if there is one
            final long thisMinRange = minRange;
            final long thisMaxRange = minRange + firstStep;
            ExecutionContext value = new ExecutionContext();
            value.putLong("minRange", thisMinRange);
            value.putLong("maxRange", thisMaxRange);
            result.put("partition"+ 0, value);
        }

        for (int i=1; i<gridSize; ++i) {
            final long thisMinRange = minRange + (i-1)*step+firstStep;
            final long thisMaxRange = minRange + firstStep + i * step;
            ExecutionContext value = new ExecutionContext();
            value.putLong("minRange", thisMinRange);
            value.putLong("maxRange", thisMaxRange);
            result.put("partition"+ i, value);
        }

        return result;
    }
}

