# FtpService
  ### Будующий сервис для работы с FTP
  
  
application,properties :
```
server.port= порт приложения
server.path= директория с откуда берем файлы
ftp.path= директория на ftp сервере
ftp.password = password
ftp.user = login
ftp.server = сервер ftp
ftp.port = порт ftp
```
на данный момент реализовано : 

localhost:8080/service  - base_url

http://127.0.0.1:8080/service/add?files=file1.txt  - добавить на ftp один файл с именем file1.txt

http://127.0.0.1:8080/service/add?files=file1,file2 - добавить на ftp список файлов file1, file2 etc

response:
```{
  "success": false,
  "message": "file does not exist",
  "data": {
    "file2": null,
    "file1": null
  }
}
```
http://127.0.0.1:8080/service/files/local/   просмотреть актуальные файлы для передачи в директории server.path

http://127.0.0.1:8080/service/files/local/PATH  просмотреть актуальные файлы для передачи в директории server.path + PATH, где PATH это следующая дериктория

response 
```
{
  "success": false,
  "message": "could not get a list of available files",
  "data": null
}
```
response OK
```
{
  "success": true,
  "message": "",
  "data": [
    "application.properties"
  ]
}
```
http://127.0.0.1:8080/service/files/ftp/   просмотреть список фалов на ftp в ftp.path
http://127.0.0.1:8080/service/files/ftp/PATH  просмотреть список фалов на ftp в ftp.path + /PATH

response
```
{
  "success": false,
  "message": "Exception in connecting to FTP Server",
  "data": null
}

```
