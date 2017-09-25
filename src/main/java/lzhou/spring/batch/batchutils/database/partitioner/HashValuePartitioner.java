package lzhou.spring.batch.batchutils.database.partitioner;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzhou on 9/21/2017.
 */
public class HashValuePartitioner implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result
                = new HashMap<String, ExecutionContext>();
        for (int i=0; i<gridSize; ++i) {
            ExecutionContext value = new ExecutionContext();
            value.putLong("gridSize", gridSize);
            value.putLong("gridId", i);
            result.put("partition"+ i, value);
        }

        return result;
    }
}
