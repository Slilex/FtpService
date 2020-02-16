package com.test.task;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class LocalFileServiceTest {

    private String rootPath = "/home/alexey/IdeaProjects/task/src/main/resources/";
    private LocalFileService localFileService;

    @Before
    public void init(){
        localFileService = new LocalFileService();
    }

    @Test
    @Ignore
    public void getFiles() throws IOException {
        List<File> files = localFileService.getFiles(rootPath);
        assertTrue(files.size() == 1);
    }

    @Test
    @Ignore
    public void getFile() throws IOException {
        File file = localFileService.getFile(rootPath + "application.properties");
        System.out.println(file.exists());
        System.out.println(Files.lines(file.toPath()));
        Stream<String> lines = Files.lines(file.toPath());
        String s = lines.findFirst().toString();
        System.out.println(s);
    }

    @Test
    @Ignore
    public void getPrintToLog() {
        File file = localFileService.getFile(rootPath + "application.properties");
        localFileService.printFile(file, 5);
    }
}