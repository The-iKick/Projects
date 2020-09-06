// A Java program for a Server
import java.net.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Lightweight Multithreaded FTP Server
 * @author Shawn Holman
 */
public class FtpServerNPort {
    int counter = 1;
    private SyncMap commandIds;

    public final String SLASH = File.separator;

    /**
     * The constructor
     * @param port The port number to run the server on within your machine
     * @throws IOException
     */
    public FtpServerNPort(int port, SyncMap commandIds) throws IOException {
        this.commandIds = commandIds;

        ServerSocket server = new ServerSocket(port);

        System.out.println("Normal Server is running on localhost:" + port);
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
     * Generate command id
     * @return
     */
    private String generateCommandId() {
        return (counter++) + "-" + java.util.UUID.randomUUID().toString().split("-")[0];
    }

    /**
     * Thread for a client to run on the FTPServer
     * @author Shawn Holman
     */
    class ClientHandler extends Thread {
        final DataInputStream dis;
        final DataOutputStream dos;
        final Socket socket;

        // Gets the pwd of the server
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
                    String text = Utility.convertSlashes(dis.readUTF().trim());
                    received = Utility.stitch(text.split("\\s+"));

                    String command = received[0];

                    // write on output stream based on the
                    // answer from the client
                    switch (command.toLowerCase()) {
                        case "exists":
                            String result = Utility.checkFile(pwd + SLASH + received[1]);
                            respond(result);
                            break;
                        case "get": {
                            String commandId = generateCommandId();
                            commandIds.put(commandId, false);
                            respond(commandId);
                            sendFile(commandId, received[1]);
                            break;
                        } case "put": {
                            String commandId = generateCommandId();
                            commandIds.put(commandId, false);
                            respond(commandId);
                            receiveFile(commandId, received[1]);
                            break;
                        } case "delete":
                            delete(received[1]);
                            break;
                        case "mkdir":
                            mkdir(received[1]);
                            break;
                        case "cd":
                            String path = received[1];

                            if (path.equals(SLASH) || path.equals(".")) {
                                respond("");
                                break;
                            }
                            cd(path);
                            break;
                        case "ls":
                            ls();
                            break;
                        case "pwd":
                            respond(pwd + "\n");
                            break;
                        case "quit":
                            quit();
                            break;
                        default:
                            respond("Invalid input.\n");
                    }
                } catch (IOException | InterruptedException e) {
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
         * Receives a file from the input stream
         * @param commandId Command id associated with the transaction
         * @param path relative path of the file
         * @throws IOException
         */
        private void receiveFile(String commandId, String path) throws IOException, InterruptedException {
            OutputStream out = null;
            File f = null;
            String absolutePath = pwd + SLASH + path;

            try {
                f = new File(absolutePath);
                out = new FileOutputStream(f);

                respond("Uploading " + path + " to " + absolutePath + "\n");

                if (!f.exists()) {
                    f.createNewFile();
                }
            } catch (FileNotFoundException ex) {
                System.out.println("File not found. ");
            }

            try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
                FileLock lock = null;
                while ((lock = raf.getChannel().tryLock()) == null) {
                    Thread.sleep(50);
                }

                //lock acquired
                boolean received = Utility.receiveFile(out, dis, commandId, commandIds);

                if (received) {
                    commandIds.remove(commandId);
                    respond("Uploaded " + path + " to " + absolutePath + "\n");
                } else {
                    if (f != null) {
                        f.delete();
                    }
                    respond("Terminated upload from " + path + " to " + absolutePath + "\n");
                }

                // release the locked channel & close the file channel
                lock.release();
            }
        }

        /**
         * Sends a file to the output stream
         * @param commandId Command id associated with the transaction
         * @param path relative path of the file
         * @throws IOException
         */
        private void sendFile(String commandId, String path) throws IOException {
            String absolutePath = pwd + SLASH + path;

            respond("Fetching " + absolutePath + " to " + path + "\n");

            File file = new File(absolutePath);

            boolean sent = Utility.sendFile(file, dos, commandId, commandIds);

            if (sent) {
                commandIds.remove(commandId);
                respond("Fetched " + absolutePath + " to " + path + "\n");
            } else {
                respond("Terminated fetching " + absolutePath + " to " + path + "\n");
            }
        }

        /**
         * Respond to the client
         * @param response the response
         * @throws IOException
         */
        private void respond(String response) throws IOException {
            dos.writeUTF(response);
        }

        /**
         * Delete a file
         * @param path relative path of the file
         * @throws IOException
         */
        private void delete(String path) throws IOException {
            File file = new File(pwd + SLASH + path);

            if (!file.exists()) {
                respond("Couldn't delete file: No such file or directory\n");
                return;
            }

            if(!file.delete()) {
                respond("Failed to delete the file\n");
            } else {
                respond("");
            }
        }

        /**
         * Creates a directory
         * @param path the relative path of the new directory
         * @throws IOException
         */
        private void mkdir(String path) throws IOException {
            new File(pwd + SLASH + path).mkdirs();
            respond("");
        }

        /**
         * Change directory
         * @param path path of the new directory (.. goes back one directory)
         * @throws IOException
         */
        private void cd(String path) throws IOException {
            String newPwd;
            if (path.equals("..")) {

                System.out.println(SLASH);
                System.out.println(pwd);

                String[] paths = pwd.split(Pattern.quote(SLASH));
                newPwd = String.join(SLASH, Arrays.copyOf(paths, paths.length - 1));

                if (newPwd.equals("")) {
                    newPwd = SLASH;
                }
            } else if (path.startsWith(SLASH)) {
                newPwd = path;
            } else {
                newPwd = pwd + SLASH + path;
            }

            File cdPath = new File(newPwd);
            if (!cdPath.exists()) {
                respond("Couldn't canonicalize: No such file or directory.\n");
                return;
            }

            if(cdPath.isDirectory()) {
                pwd = newPwd;
                respond("");
            } else {
                respond("Can't change directory: \"" + newPwd + "\" is not a directory\n");
            }
        }

        /**
         * List contents of the current pwd
         * @throws IOException
         */
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
        private void quit() {
            try {
                this.socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }

            running = false;
        }
    }
}