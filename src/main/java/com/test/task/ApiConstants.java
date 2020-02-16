package com.test.task;

public interface ApiConstants {

    String FILES = "files";
    String PATH = "path";

    interface URL {
        String REQUEST_MAPPING = "/service";
        String ADD_FILES = "/add";
        String ALL_FTP_FILES = "/files/ftp";
        String LOCAL_FILES = "/files/local";
        String BY_PATH = "/{" + PATH + "}";
    }

    interface OK {
        String CLOSED_SUCCESSFULLY = "connection closed successfully";
        String CONNECTION_ESTABLISHED ="connection to server is established";

    }

    interface Error {
        String FILE_NOT_EXISTS = "file does not exist";
        String READ_ERROR = "read error";
        String FAILED_OPEN_CONNECTION = "Exception in connecting to FTP Server";
        String FAILED_SEND_FILE = "failed to send file on ftp server";
        String CLOSE_CONNECTION_FAILED = "failed to close the connection";
        String LOGON_ERROR = "Logon error";
        String ERROR_GET_FILES = "could not get a list of available files";
    }

}
