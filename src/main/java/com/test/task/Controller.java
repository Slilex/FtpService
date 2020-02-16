package com.test.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.URL.REQUEST_MAPPING)
public class Controller {
    private Logger logger = LoggerFactory.getLogger(Controller.class);
    @Autowired
    private FtpFileService ftpFileService;

    @RequestMapping(ApiConstants.URL.ADD_FILES)
    @ResponseBody
    public ResponseEntity<ExecResult> add(@RequestParam(ApiConstants.FILES) List<String> files) {
        if (files.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("wrong request. requestParam (files) is empty");
            }
            return ResponseEntity.status(400).body(ExecResult.success());
        }
        ExecResult execResult = ftpFileService.addFilesOnFtpServer(files);
        if (execResult.isSuccess()) {
            return ResponseEntity.ok()
                    .body(execResult);
        } else {
          return ResponseEntity.status(404).body(execResult);
        }
    }

    @RequestMapping(value = {ApiConstants.URL.ALL_FTP_FILES,
            ApiConstants.URL.ALL_FTP_FILES + ApiConstants.URL.BY_PATH}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ExecResult> ftpFiles(@PathVariable(value = ApiConstants.PATH, required = false) String path){
        ExecResult actualFilesOnFtp = ftpFileService.getActualFilesOnFtp(path);
        if(actualFilesOnFtp.isSuccess()){
            return ResponseEntity.ok(actualFilesOnFtp);
        }else {
            return ResponseEntity.status(404).body(actualFilesOnFtp);
        }
    }

    @RequestMapping(value = {ApiConstants.URL.LOCAL_FILES, ApiConstants.URL.LOCAL_FILES + ApiConstants.URL.BY_PATH}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ExecResult> getLocalFiles(@PathVariable(value = ApiConstants.PATH, required = false) String path){
        ExecResult actualFilesOnFtp = ftpFileService.getAllLocalFiles(path);
        if(actualFilesOnFtp.isSuccess()){
            return ResponseEntity.ok(actualFilesOnFtp);
        }else {
            return ResponseEntity.status(404).body(actualFilesOnFtp);
        }
    }


}
