package com.test.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class FtpFileService {
    private static Logger logger = LoggerFactory.getLogger(FtpFileService.class);
    @Autowired
    private LocalFileService localFileService;
    @Autowired
    private FtpClient ftpClient;
    @Autowired
    private Controller controller;
    @Value("${ftp.path}")
    private String rootFtpPath;
    @Value("${server.path}")
    private String localPath;
    private Map<String, File> nameFileMap;

    public FtpFileService() {
    }

    public ExecResult addFilesOnFtpServer(List<String> fileList) {
        nameFileMap = new HashMap<>();
        for(String fileName : fileList){
            File file = localFileService.getFile(localPath +  fileName);
            localFileService.printFile(file, 1);
            nameFileMap.put(fileName,file);
        }

        if(nameFileMap.containsValue((File) null)){
            if(logger.isDebugEnabled()) {
                logger.debug("error : {} , file info: {}", ApiConstants.Error.FILE_NOT_EXISTS , nameFileMap.keySet().toString());
            }
            return ExecResult.error(ApiConstants.Error.FILE_NOT_EXISTS, nameFileMap);
        } else {
                ExecResult open = ftpClient.open();
                if (open.isSuccess()) {
                    try {
                        for (Map.Entry<String, File> map : nameFileMap.entrySet()) {
                            ExecResult result = ftpClient.putFileToPath(map.getValue(), rootFtpPath);
                            if (result.isSuccess()) {
                                logger.info("file : {} write on ftp server ", map.getKey());
                            } else {
                                result.setData(map.getKey());
                                return result;
                            }
                        }
                    }finally {
                       ftpClient.close();
                    }
                } else {
                    logger.warn("error : {} , message: {}", ApiConstants.Error.FAILED_OPEN_CONNECTION, open.getData());
                    return ExecResult.error(ApiConstants.Error.FAILED_OPEN_CONNECTION);
                }
        }
        return ExecResult.success(nameFileMap.keySet());
    }

    public ExecResult getActualFilesOnFtp(String path) {
        if(path == null) {
            path = "";
        }
        Collection<String> files = new ArrayList<>();
        ExecResult open = ftpClient.open();
        if(open.isSuccess()) {
            try{
                try {
                    files = ftpClient.listFiles(localPath + path);
                } catch (Exception e) {
                    logger.warn("error : {} , ftpClient : {} message: {}", ApiConstants.Error.ERROR_GET_FILES, ftpClient.toString(), e.getMessage(), e);
                    return ExecResult.error(ApiConstants.Error.ERROR_GET_FILES);
                }
            }finally {
                ftpClient.close();
            }
        } else {
                logger.warn("error : {} , message: {}", ApiConstants.Error.FAILED_OPEN_CONNECTION, open.getData());
                return ExecResult.error(ApiConstants.Error.FAILED_OPEN_CONNECTION);
        }
            return ExecResult.success(files);
    }

    public ExecResult getAllLocalFiles(String addPath){
        if(addPath == null){
            addPath = "";
        }
        List<String> fileNameList =  new ArrayList<>();
        try {
            List<File> files = localFileService.getFiles(localPath + addPath);
            for(File file : files){
                fileNameList.add(file.getName());
            }
        } catch (Exception e) {
            logger.warn("error : {}", ApiConstants.Error.ERROR_GET_FILES);
            return ExecResult.error(ApiConstants.Error.ERROR_GET_FILES);
        }

        return ExecResult.success(fileNameList);
    }

    public String getRootFtpPath() {
        return rootFtpPath;
    }

    public void setRootFtpPath(String rootFtpPath) {
        this.rootFtpPath = rootFtpPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }


}
