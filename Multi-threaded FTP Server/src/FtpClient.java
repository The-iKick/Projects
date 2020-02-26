// A Java program for a Client
import java.io.*;
import java.nio.channels.FileLock;
import java.util.Scanner;
import java.util.function.BiFunction;

/**
 * Lightweight FTP Server Client
 * @author Shawn Holman
 */
public class FtpClient {
    private SyncMap commandIds = new SyncMap();

    private FtpClientSocket nPortSocket;
    private FtpClientSocket tPortSocket;

    private Scanner scn = new Scanner(System.in);
    private String address;
    private int nPort;

    private final String PROMPT = "mytftp> ";

    /**
     * Represents modes of traffic for a command
     */
    enum COMMAND_TRAFFIC {
        CANCELLED,
        CONTINUED,
        NEW_THREAD
    }

    /**
     * Constructor
     * @param address Address of the server which is running the FtpServer
     * @param nPort Port number that the NFtpServer is running on
     * @param tPort Port number that the TFtpServer is running on
     * @throws IOException
     */
    public FtpClient(String address, int nPort, int tPort) {
        this.address = address;
        this.nPort = nPort;

        try {
            nPortSocket = new FtpClientSocket(address, nPort);
            tPortSocket = new FtpClientSocket(address, tPort);

            // the following loop performs the exchange of
            // information between client and client handlerqu
            System.out.println("Connected to " + address + ":" + nPort + " and " + address + ":" + tPort);
            System.out.print(PROMPT);
            while (true) {
                String toSend = Utility.convertSlashes(scn.nextLine().trim());
                String[] sections = Utility.stitch(toSend.split("\\s+"));

                COMMAND_TRAFFIC status = handleCommandTraffic(sections, nPortSocket, (nPortSocketUsed, onSeperateThread) -> {
                    try {
                        // if the user does a get request then we need to first check if the file exists
                        if (toSend.startsWith("get")) {
                            nPortSocketUsed.write("exists " + sections[1]);
                            String response = nPortSocketUsed.read();
                            if (!response.equals("ok")) {
                                print(response, onSeperateThread);
                                return COMMAND_TRAFFIC.CONTINUED;
                            }

                            nPortSocketUsed.write(toSend); // send of the request

                            // setup command id locally
                            String commandId = nPortSocketUsed.read();
                            commandIds.put(commandId, false);
                            print("Your Command-ID: " + commandId + "\n", onSeperateThread);

                            print(nPortSocketUsed.read(), onSeperateThread); // confirm the get...file is being sent
                            receiveFile(nPortSocketUsed, commandId, sections[1]); // receive the file
                            print(nPortSocketUsed.read(), onSeperateThread); // finished
                        } else if (toSend.startsWith("put")) {
                            String fileCheck = Utility.checkFile(sections[1]);

                            if (!fileCheck.equals("ok")) {
                                print(fileCheck + "\n", onSeperateThread);
                                return COMMAND_TRAFFIC.CONTINUED;
                            }

                            nPortSocketUsed.write(toSend); // put the file onto the server

                            // setup command id locally
                            String commandId = nPortSocketUsed.read();
                            commandIds.put(commandId, false);
                            print("Your Command-ID: " + commandId + "\n", onSeperateThread);


                            print(nPortSocketUsed.read(), onSeperateThread); // confirm the file... the file is being transfered
                            sendFile(nPortSocketUsed, commandId, sections[1]); // send the bytes over
                            print(nPortSocketUsed.read(), onSeperateThread); // finished
                        } else if (toSend.startsWith("terminate")) {
                            tPortSocket.write(toSend);

                            switch(FtpServerTPort.RESPONSE.valueOf(tPortSocket.read())) {
                                case INVALID:
                                    print("Invalid input.\n", onSeperateThread);
                                    break;
                                case NOT_FOUND:
                                    print("Command-ID not found.\n", onSeperateThread);
                                    break;
                                case ALREADY_TERMINATED:
                                    print("This operation is already terminated.\n", onSeperateThread);
                                    break;
                                case SUCCESS:
                                    commandIds.replace(sections[1], true);
                            }
                        } else {
                            nPortSocketUsed.write(toSend);
                            // If client sends exit,close this connection
                            // and then break from the while loop
                            if (toSend.equalsIgnoreCase("quit")) {
                                nPortSocketUsed.closeSocket();
                                return COMMAND_TRAFFIC.CANCELLED;
                            }

                            print(nPortSocketUsed.read(), onSeperateThread);
                        }
                    } catch(IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return COMMAND_TRAFFIC.CONTINUED;
                });

                if (status != COMMAND_TRAFFIC.NEW_THREAD) { // if we did not start a new thread
                    if (status == COMMAND_TRAFFIC.CANCELLED) {
                        break;
                    }
                    // then we need to print the prompt, because we only make the prompt available with incoming
                    // messages from a new thread in order to keep the CLI looking cleanls
                    System.out.print(PROMPT);
                }
            }

            // closing resources
            scn.close();
            nPortSocket.close();
            tPortSocket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Special print utility which first moves the carriage back to the beginnging and optionally appends the prompt.
     * Appending the prompt is nececarry for multithreaded command operations are input will be enabling during the
     * entire duration of the multithreaded operation
     * @param text text to print
     * @param appendPrompt whether the prompt should be appended to the end of the text
     */
    private void print(String text, boolean appendPrompt) {
        System.out.print("\r" + text + (appendPrompt ? PROMPT : ""));
    }


    /**
     * Handle the traffic of command. This method will determine whether to defer the command to a new thread or not
     * @param sections The seperated commands
     * @param socket The long term executing parent socket. This will be used on normal execution of commands
     * @param handler The execution code to handle the I/O of each socket.
     * @return A status code is return based on the handler passed in. If the handlers execution returns back 1, that
     * means that everything ran successfully. If 0 is returned, then the socket was/should be closed. Status code 2 is
     * return if action was deferred to a new socket.
     * @todo Make sure that the new thread for the client starts us out in the current pwd of the client
     */
    private COMMAND_TRAFFIC handleCommandTraffic(String sections[], FtpClientSocket socket, BiFunction<FtpClientSocket, Boolean, COMMAND_TRAFFIC> handler) throws IOException {
        socket.write("pwd");
        String currentDirectory  = socket.read();

        // If the command is appended with & then you should be running the command in a new thread
        if ((sections.length == 2 && sections[1].equals("&")) || (sections.length == 3 && sections[2].equals("&"))) {
            Thread nPortThread = new Thread(){
                public void run(){
                    try {
                        // create a new socket into the server as the same client
                        FtpClientSocket socket = socket = new FtpClientSocket(address, nPort);

                        // make sure that the replicated client has the same directory as the original
                        socket.write("cd " + currentDirectory);
                        socket.read();
                        // run the handler which should apply the current sections
                        COMMAND_TRAFFIC status = handler.apply(socket, true);
                        if (status == COMMAND_TRAFFIC.CONTINUED) {
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            nPortThread.start();

            return COMMAND_TRAFFIC.NEW_THREAD;
        } else {
            return handler.apply(socket, false);
        }
    }

    /**
     * Sends a file to the output stream
     * @param path relative path of the file
     * @throws IOException
     */
    private void sendFile(FtpClientSocket socket, String commandId, String path) throws IOException {
        DataOutputStream nPortOut = socket.getOutputStream();
        String absolutePath = path;
        File file = new File(path);

        if (!file.exists()) {
            throw new IOException("stat " + absolutePath + ": No such file or directory");
        }

        if (file.isDirectory()) {
            throw new IOException(absolutePath + "/ is not a regular file");
        }

        boolean sent = Utility.sendFile(file, nPortOut, commandId, commandIds);

        if (sent) {
            commandIds.remove(commandId);
        }
    }

    /**
     * Receives a file from the input stream
     * @param socket Socket to receive from
     * @param commandId Command id associated with the transaction
     * @param path Path to receive to
     * @throws IOException
     * @throws InterruptedException
     */
    private void receiveFile(FtpClientSocket socket, String commandId, String path) throws IOException, InterruptedException {
        DataInputStream nPortIn = socket.getInputStream();
        OutputStream out = null;
        File f = null;

        try {
            f = new File(path);
            out = new FileOutputStream(f);

            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        }

        try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
            FileLock lock = null;
            while ((lock = raf.getChannel().tryLock()) == null) { // wait for the loop to be
                Thread.sleep(50);
            }

            // lock acquired
            boolean received = Utility.receiveFile(out, nPortIn, commandId, commandIds);

            if (received) {
                commandIds.remove(commandId);
            }
            lock.release();
        }
    }

    /**
     * Entry point into the program. Example usage:
     * FtpClient 127.0.0.1 5000
     * @param args 2 arguments are required which will contain the address and port number
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        String host = args[0];
        int nPort = Integer.parseInt(args[1]);
        int tPort = Integer.parseInt(args[2]);

        FtpClient client = new FtpClient(host, nPort, tPort);
    }
} 