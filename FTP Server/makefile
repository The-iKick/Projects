compile:
	mkdir bin;
	javac -d bin src/FtpServer.java src/FtpClient.java

runServer:
	java -cp bin FtpServer 5000

runClient:
	java -cp bin FtpClient 127.0.0.1 5000