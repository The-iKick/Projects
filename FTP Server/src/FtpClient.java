// A Java program for a Client
import java.net.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Lightweight FTP Server Client
 * @author Shawn Holman
 */
public class FtpClient {

    Socket socket;
    /**
     * Constructor
     * @param address Address of the server which is running the FtpServer
     * @param port Port number that the FtpServer is running on
     * @throws IOException
     */
    public FtpClient(String address, int port) {
        try {
            Scanner scn = new Scanner(System.in);
            // establish the connection with server port 5056
            socket = new Socket(address, port);
            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // the following loop performs the exchange of
            // information between client and client handler
            System.out.println("Connected to " + address + ":" + port);
            while (true) {
                System.out.print("sftp> ");

                String toSend = scn.nextLine();

                // If client sends exit,close this connection
                // and then break from the while loop
                if(toSend.equalsIgnoreCase("exit")) {
                    socket.close();
                    break;
                }

                dos.writeUTF(toSend);

                if (toSend.startsWith("put")) {
                    String[] sections = toSend.split(" ");
                    sendFile(sections[1]);
                }

                if (toSend.startsWith("get")) {
                    String[] sections = toSend.split(" ");
                    receiveFile(sections[1]);
                }

                System.out.print(dis.readUTF());
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        } catch (Exception e){
            e.printStackTrace();
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
        int port = Integer.parseInt(args[1]);

        FtpClient client = new FtpClient(host, port);
    }

    /**
     * Sends a file to the output stream
     * @param path relative path of the file
     * @throws IOException
     */
    private void sendFile(String path) throws IOException {
        File file = new File(path);
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

    /**
     * Receives a file from the input stream
     * @param name name of the file
     * @throws IOException
     */
    private void receiveFile(String name) throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = null;
        try {
            File f = new File(name);
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
} 