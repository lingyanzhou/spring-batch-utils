package lzhou.spring.batch.batchutils.file.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by lzhou on 9/23/2017.
 */
public class DeleteFileTasklet implements Tasklet {
    private boolean assertFileExists;
    private boolean assertSuccessfulDeletion;
    private String filePathName;

    public DeleteFileTasklet(String filePathName) {
        this.filePathName = filePathName;
        assertFileExists = true;
        assertSuccessfulDeletion = true;
    }

    public void setAssertSuccessfulDeletion(boolean assertSuccessfulDeletion) {
        this.assertSuccessfulDeletion = assertSuccessfulDeletion;
    }

    public void setAssertFileExists(boolean assertFileExists) {
        this.assertFileExists = assertFileExists;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        File f = new File(filePathName);
        if (f.exists()) {
            if (f.isFile()) {
                boolean deleted = f.delete();
                if (!deleted) {
                    if (assertSuccessfulDeletion) {
                        throw new IOException(String.format("File deletion failed: %s", filePathName));
                    }
                }
            } else {
                throw new IOException(String.format("Not a file: %s", filePathName));
            }
        } else { // not f.exists()
            if (assertFileExists) {
                throw new FileNotFoundException(String.format("File not found: %s", filePathName));
            }
        }
        return RepeatStatus.FINISHED;
    }
}
