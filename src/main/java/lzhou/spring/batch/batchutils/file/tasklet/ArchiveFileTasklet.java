package lzhou.spring.batch.batchutils.file.tasklet;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
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
public class ArchiveFileTasklet implements Tasklet {
    private boolean assertFileExists;
    private boolean copyOnly;
    private String filePathName;
    private String archivePath;
    private String destFileBaseName;
    private String destFileExtension;

    public ArchiveFileTasklet(String filePathName, String archivePath, String destFileBaseName, String destFileExtension) {
        this.filePathName = filePathName;
        assertFileExists = true;
        copyOnly = true;
        if (archivePath.endsWith("/")) {
            this.archivePath = archivePath;
        } else {
            this.archivePath = archivePath + "/";
        }
        this.destFileBaseName = destFileBaseName;
        if (destFileExtension.startsWith(".")) {
            this.destFileExtension = destFileExtension;
        } else {
            this.destFileExtension = "."+destFileExtension;
        }
    }

    public void setAssertFileExists(boolean assertFileExists) {
        this.assertFileExists = assertFileExists;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        File src = new File(filePathName);
        File dest = new File(archivePath+destFileBaseName+ DateTime.now().toString("yyyy-MM-dd'T'HH-mm-ss-z")+destFileExtension);
        FileUtils.forceMkdirParent(dest);
        if (src.exists()) {
            if (src.isFile()) {
                if (copyOnly) {
                    FileUtils.copyFile(src, dest);
                } else {
                    FileUtils.moveFile(src, dest);
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
