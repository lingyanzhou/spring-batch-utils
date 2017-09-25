package lzhou.spring.batch.batchutils.cmd;

import lombok.Data;

import java.util.List;

/**
 * Created by lzhou on 9/15/2017.
 */
@Data
public class BatchJobExecInfo {
    private String jobName;
    private String contextFile;
    private List<String> parameters;
}
