// A Java program for a Server 
import java.net.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Lightweight Multithreaded FTP Server
 * @author Shawn Holman
 */
public class FtpServer {
    /**
     * The constructor
     * @param port The port number to run the server on within your machine
     * @throws IOException
     */
    public FtpServer(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        // starts server and waits for a connection

        System.out.println("Server is running on localhost:" + port);
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
     * Entry point into the program. Example usage:
     * FtpServer 5000
     * @param args 1 argument is required which will contain the port number
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        int port = Integer.parseInt(args[0]);

        FtpServer server = new FtpServer(port);
    }

    /**
     * Thread for a client to run on the FTPServer
     * @author Shawn Holman
     */
    class ClientHandler extends Thread {
        final DataInputStream dis;
        final DataOutputStream dos;
        final Socket socket;
        String pwd = Paths.get(".").toAbsolutePath().normalize().toString();

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
            String toReturn;

            while (running) {
                try {
                    // receive the answer from client
                    //System.out.println(dis.readUTF());
                    received = dis.readUTF().split(" ");

                    String command = received[0];

                    // write on output stream based on the
                    // answer from the client
                    switch (command.toLowerCase()) {
                        case "get":
                            sendFile(received[1]);
                            respond("");
                            break;
                        case "put":
                            receiveFile(received[1]);
                            respond("");
                            break;
                        case "delete":
                            delete(received[1]);
                            respond("");
                            break;
                        case "mkdir":
                            mkdir(received[1]);
                            respond("");
                            break;
                        // build up cases
                        case "cd":
                            String path = received[1];

                            if (path.equals("/") || path.equals(".")) {
                                break;
                            }

                            cd(path);
                            respond("");
                            break;
                        case "ls":
                            ls();
                            break;
                        case "pwd":
                            respond(pwd + "\n");
                            break;
                        case "exit":
                            exit();
                            break;
                        default:
                            respond("Invalid input\n");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
         * Receives a file from the input stream
         * @param name name of the file
         * @throws IOException
         */
        private void receiveFile(String name) throws IOException {
            InputStream in = socket.getInputStream();
            OutputStream out = null;
            try {
                File f = new File(pwd + "/" + name);
                out = new FileOutputStream(f);

                if (!f.exists()) {
                    f.createNewFile();
                }
            } catch (FileNotFoundException ex) {
                System.out.println("File not found. ");
            }

            byte[] bytes = new byte[16*1024];
            int count;
            while (in.available() > 0) {
                count = in.read(bytes);
                out.write(bytes, 0, count);
            }

            out.close();
        }

        /**
         * Sends a file to the output stream
         * @param path relative path of the file
         * @throws IOException
         */
        private void sendFile(String path) throws IOException {
            File file = new File(pwd + "/" + path);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // output to send to
            OutputStream out = socket.getOutputStream();
            // Get the size of the file
            long length = file.length();
            byte[] byteArray = new byte[(int) file.length() + 1];

            bis.read(byteArray, 0, byteArray.length);

            out.write(byteArray, 0, byteArray.length);
            out.flush();
        }

        private void respond(String response) throws IOException {
            dos.writeUTF(response);
        }

        private void delete(String fileName) throws IOException {
            File file = new File(pwd + "/" + fileName);

            if(!file.delete()) {
               respond("Failed to delete the file\n");
            }
        }

        private void mkdir(String name) throws IOException {
            new File(pwd + "/" + name).mkdirs();
        }

        private void cd(String path) throws IOException {
            String newPwd;
            if (path.equals("..")) {
                String[] paths = pwd.split("/");
                newPwd = String.join("/", Arrays.copyOf(paths, paths.length - 1));

                if (newPwd.equals("")) {
                    newPwd = "/";
                }
            } else if (path.startsWith("/")) {
                newPwd = path;
            } else {
                newPwd = pwd + "/" + path;
            }

            File cdPath = new File(newPwd);
            if(cdPath.isDirectory()) {
                pwd = newPwd;
            } else {
                respond("Parent is not a directory\n");
            }
        }

        private void ls() throws IOException {
            File parent = new File(pwd);
            if(parent.isDirectory()) {
                File[] listFiles = parent.listFiles();
                StringBuffer list = new StringBuffer();
                for (int i = 0; i < listFiles.length; i++) {
                    list.append(listFiles[i].getName() + "\n");
                }
                respond(list.toString());
            }
        }

        /**
         * Close the connection of a client
         */
        private void exit() {
            System.out.println("Client " + this.socket + " sends exit...");
            System.out.println("Closing this connection.");
            try {
                this.socket.close();
                System.out.println("This connection closed.");
            } catch (IOException e){
                e.printStackTrace();
            }

            running = false;
        }
    }
} 