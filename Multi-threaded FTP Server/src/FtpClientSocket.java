import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Represents a ClientSocket, it really just encapsulates a socket with the data input streams included
 */
public class FtpClientSocket {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    FtpClientSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void write(String str) throws IOException {
        out.writeUTF(str);
    }

    public String read() throws IOException {
        return in.readUTF();
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOutputStream() {
        return out;
    }

    public DataInputStream getInputStream() {
        return in;
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public void close() throws IOException {
        in.close();
        out.close();
    }
}