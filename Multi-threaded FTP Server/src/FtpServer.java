// A Java program for a Server
import java.io.*;

/**
 * Lightweight Multithreaded FTP Server
 * @author Shawn Holman
 */
public class FtpServer {
    // Map<String, Boolean> commandIds = new HashMap<>();
    //  SyncMap map = new SyncMap();
    // Map commandIds = map.getMap();
    private SyncMap commandIds = new SyncMap();

    /**
     * Constructor
     * @param nPort
     * @param tPort
     * @throws IOException
     */
    public FtpServer(int nPort, int tPort) {

        Thread nPortThread = new Thread(){
            public void run(){
                try {
                    FtpServerNPort nPortServer = new FtpServerNPort(nPort, commandIds);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread tPortThread = new Thread(){
            public void run(){
                try {
                    FtpServerTPort tPortServer = new FtpServerTPort(tPort, commandIds);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        nPortThread.start();
        tPortThread.start();
    }

    /**
     * Entry point into the program. Example usage:
     * FtpServer 5000 50001
     * @param args 2 arguments are required which will contain the port numbers for the normal and termination ports
     * @throws IOException
     */
    public static void main(String args[]) {
        int nPort = Integer.parseInt(args[0]);
        int tPort = Integer.parseInt(args[1]);

        FtpServer server = new FtpServer(nPort, tPort);
    }
} 