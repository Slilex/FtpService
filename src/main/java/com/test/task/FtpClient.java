package com.test.task;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class FtpClient {

    private Logger logger = LoggerFactory.getLogger(FTPClient.class);
    @Value("${ftp.server}")
    private String server;
    @Value("${ftp.port}")
    private int port;
    @Value("${ftp.user}")
    private String user;
    @Value("${ftp.password}")
    private String password;
    private FTPClient ftp;

    public FtpClient() {
    }

    public FtpClient(String server, int port, String user, String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public ExecResult open() {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        try {
            ftp.connect(server, port);

        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();

        }
        } catch (IOException e) {
            logger.warn("error : {}, ftpClient : {} ",ApiConstants.Error.FAILED_OPEN_CONNECTION, this.toString() );
            return ExecResult.error(ApiConstants.Error.FAILED_OPEN_CONNECTION, e);
        }
        try {
            ftp.login(user, password);
        } catch (IOException e) {
            logger.warn("error : {}, ftpClient : {} ",ApiConstants.Error.FAILED_OPEN_CONNECTION, this.toString() );
            return ExecResult.error(ApiConstants.Error.LOGON_ERROR, e);
        }
        logger.info(ApiConstants.OK.CONNECTION_ESTABLISHED);
        return ExecResult.success();
    }

    public ExecResult close(){
        try {
            ftp.disconnect();
            logger.info(ApiConstants.OK.CLOSED_SUCCESSFULLY);
        } catch (IOException e) {
            logger.warn("error : {}, ftpClient : {} ",ApiConstants.Error.CLOSE_CONNECTION_FAILED, this.toString(), e);
            return ExecResult.error(ApiConstants.Error.CLOSE_CONNECTION_FAILED);
        }
        return ExecResult.success();
    }

    public ExecResult putFileToPath(File file, String path) {
        try {
            ftp.storeFile(path, new FileInputStream(file));
        } catch (IOException e) {
            logger.warn("error : {}, ftpClient : {} ",ApiConstants.Error.FAILED_SEND_FILE, this.toString() );
            return ExecResult.error(ApiConstants.Error.FAILED_SEND_FILE, e);
        }
        return ExecResult.success();
    }

    public Collection<String> listFiles(String path) throws IOException {
        FTPFile[]files = ftp.listFiles(path);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }

    public void downloadFile(String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        ftp.retrieveFile(source, out);
    }



    @Override
    public String toString() {
        return "FtpClient{" +
                "server='" + server + '\'' +
                ", port=" + port +
                ", user='" + user + '\'' +
                '}';
    }
}
