package lzhou.spring.batch.batchutils.chunk.listener;

import lzhou.spring.batch.batchutils.job.listener.PerformanceJobExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * Created by lzhou on 9/21/2017.
 */
public class PerformanceChunkListener implements ChunkListener {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceJobExecutionListener.class);

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        logger.info("Chunk starts...");
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        StepExecution stepExec = chunkContext.getStepContext().getStepExecution();
        logger.info(String.format("Chunk ends normally. Total Step Contributions: [Read] %d, [Write] %d, [Skip] %d, [Filter] %d",
                stepExec.getReadCount(),
                stepExec.getWriteCount(),
                stepExec.getSkipCount(),
                stepExec.getFilterCount()
        ));
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        logger.info("Chunk ends with errors.");
    }
}
