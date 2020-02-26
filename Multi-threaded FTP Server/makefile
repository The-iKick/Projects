compile:
	mkdir bin;
	javac -d bin src/*.java

runServer:
	cd server;
	java -cp ../bin FtpServer 5000 5001

runClient:
	cd client;
	java -cp ../bin FtpClient 127.0.0.1 5000 5001

clean:
	rm -rf bin
