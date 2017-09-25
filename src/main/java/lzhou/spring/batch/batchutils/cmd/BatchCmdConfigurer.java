package lzhou.spring.batch.batchutils.cmd;

import java.util.Map;

/**
 * Created by lzhou on 9/14/2017.
 */
public interface BatchCmdConfigurer {
    enum Status {
        EXIT_REQUESTED,
        CMD_ERROR,
        PROCEED
    }
    Status parseArgs(String[] args) ;
    String getJobName();
    String getContextFile();
    Map<String, String> getParameters();
}