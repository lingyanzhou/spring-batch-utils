package lzhou.spring.batch.batchutils.file.tasklet;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Created by lzhou on 9/22/2017.
 */
public class SftpUploadTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(SftpUploadTasklet.class);

    private String filePathName;

    private String url;

    private String user;

    private String password;

    private String remotePath;

    public SftpUploadTasklet(String filePathName, String url, String user, String password, String remotePath) {
        this.filePathName = filePathName;
        this.url = url;
        this.user = user;
        this.password = password;
        if (remotePath.endsWith("/")) {
            this.remotePath = remotePath;
        } else {
            this.remotePath = remotePath + "/";
        }
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Session session = null;
        ChannelSftp channelSftp = null;
        logger.debug("SftpUploadTasklet starts.");

        try {
            JSch jsch = new JSch();

            session = jsch.getSession(user, url, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(remotePath);

            Path localPath = Paths.get(filePathName);

            logger.error("SftpUploadTasklet uploading: {}.", remotePath + localPath.getFileName());
            channelSftp.put(localPath.toAbsolutePath().toString(), remotePath + localPath.getFileName());

            contribution.incrementReadCount();
            contribution.incrementWriteCount(1);
        } catch(Exception e){
            logger.error("SftpUploadTasklet failed. The detail: {}.", ExceptionUtils.getStackTrace(e));
            throw e;
        } finally {
            if (channelSftp!=null) {
                channelSftp.disconnect();
            }
            if (session!=null) {
                session.disconnect();
            }
        }
        logger.debug("SftpUploadTasklet ends.");

        return RepeatStatus.FINISHED;
    }

}
