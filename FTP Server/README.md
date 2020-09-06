1) Create a bin folder.
2) Compile. 
       javac -d bin src/FtpServer.java src/FtpClient.java
3) Run server.
       java -cp bin FtpServer 5000
4) Connect 1 or more clients.
       java -cp bin FtpClient 127.0.0.1 5000