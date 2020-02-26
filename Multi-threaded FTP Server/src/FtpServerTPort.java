// A Java program for a Server
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Lightweight Multithreaded FTP Server
 * @author Shawn Holman
 */
public class FtpServerTPort {
    SyncMap commandIds;

    enum RESPONSE {
        SUCCESS,
        NOT_FOUND,
        ALREADY_TERMINATED,
        INVALID
    }

    /**
     * The constructor
     * @param port The port number to run the server on within your machine
     * @throws IOException
     */
    public FtpServerTPort(int port, SyncMap commandIds) throws IOException {
        this.commandIds = commandIds;

        ServerSocket server = new ServerSocket(port);
        // starts server and waits for a connection

        System.out.println("Termination Server is running on localhost:" + port);
        while (true) {
            Socket socket = null;

            try {
                socket = server.accept();
                System.out.println("Client accepted");

                // create a new thread object
                Thread t = new ClientHandler(socket);

                // Start the thread
                t.start();
            } catch (IOException i) {
                socket.close();
                System.out.println(i);
            }
        }
    }

    /**
     * Thread for a client to run on the FTPServer
     * @author Shawn Holman
     */
    class ClientHandler extends Thread {
        final DataInputStream dis;
        final DataOutputStream dos;
        final Socket socket;

        boolean running = true;

        /**
         * Constructor
         * @param socket Accepted socket instance
         */
        ClientHandler(Socket socket) throws IOException {
            this.socket = socket;

            // obtaining input and out streams
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            String[] received;

            while (running) {
                try {
                    // receive the answer from client
                    String text = dis.readUTF().trim();
                    received = text.split("\\s+");

                    String command = received[0];

                    if (received.length == 1) {
                        //respond("Invalid input\n");
                        respond(RESPONSE.INVALID);
                        return;
                    }

                    // write on output stream based on the
                    // answer from the client
                    switch (command.toLowerCase()) {
                        case "terminate":
                            if (!commandIds.containsKey(received[1])) {
                                respond(RESPONSE.NOT_FOUND);
                            } else if (commandIds.get(received[1]) == true) {
                                respond(RESPONSE.ALREADY_TERMINATED);
                            } else {
                                commandIds.replace(received[1], true);
                                respond(RESPONSE.SUCCESS);
                            }
                            break;
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }

            try {
                // closing resources
                this.dis.close();
                this.dos.close();

            } catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * Respond to the client
         * @param response the response
         * @throws IOException
         */
        private void respond(RESPONSE response) throws IOException {
            dos.writeUTF(response.name());
        }
    }
}