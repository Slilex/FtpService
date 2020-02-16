package com.test.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class LocalFileService {
    private static Logger logger = LoggerFactory.getLogger(LocalFileService.class);

    public List<File> getFiles (String stringPath) throws IOException {
         return Files.walk(Paths.get(stringPath),1)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    public File getFile(String stringPath){
        Path path = Paths.get(stringPath);
        File file = path.toFile();
        if(file.exists()){
            return file;
        } else {
            return null;
        }
    }


    public void printFile(File file, int lines){
        if (file == null){
            logger.warn("error : {} , file == null" , ApiConstants.Error.FILE_NOT_EXISTS);
            return;
        }
        if(file.exists()){
            AtomicInteger countLines = new AtomicInteger(1);
            try {
                Files.lines(file.toPath(), StandardCharsets.UTF_8)
                        .limit(lines)
                        .forEach(s -> logger.info("fileName : '{}' string N {} : '{}'", file.getName(), countLines.getAndIncrement() , s ));
            } catch (IOException e) {
                logger.warn("error : {} , msg : {}" , ApiConstants.Error.READ_ERROR, e.getMessage(), e);
            }
        }else {
            logger.warn("error : {} , file : {}" , ApiConstants.Error.FILE_NOT_EXISTS, file.getName());
        }

    }
}
