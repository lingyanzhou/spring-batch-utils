package lzhou.spring.batch.batchutils.database.partitioner;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzhou on 9/20/2017.
 */
public class IdColumnRangePartitioner implements Partitioner {
    private String sqlMin;
    private String sqlMax;
    private long defaultMin;
    private long defaultMax;
    private JdbcTemplate jdbcTemplate;

    public IdColumnRangePartitioner(String sqlMin, String sqlMax, long defaultMin, long defaultMax, DataSource dataSource) {
        this.sqlMin = sqlMin;
        this.sqlMax = sqlMax;
        this.defaultMin = defaultMin;
        this.defaultMax = defaultMax;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result
                = new HashMap<String, ExecutionContext>();

        final Long minRangeObj = jdbcTemplate.queryForObject(sqlMin, Long.class);
        final Long maxRangeObj = jdbcTemplate.queryForObject(sqlMax, Long.class);
        final long minRange = null==minRangeObj ? defaultMin: minRangeObj.longValue();
        final long maxRange = null==maxRangeObj ? defaultMax: maxRangeObj.longValue();
        final long range = (maxRange - minRange);
        final long step = range/gridSize;
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

