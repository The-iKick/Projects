import java.io.*;

/**
 * A few utility methods that are shared about the Server and Client code
 */
public class Utility {
    /**
     * Stitch back an array of Strings so that the array is a maximum of 3 strings in length
     * @param parts
     * @return
     */
    public static String[] stitch(String [] parts) {
        int isNewThread = parts[parts.length - 1].equals("&") ? 1 : 0;
        int numberOfSections = Math.min(3 - (1 - isNewThread), parts.length);
        String[] stitchedArray = new String[numberOfSections];

        stitchedArray[0] = parts[0];

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < parts.length - isNewThread; i++) {
            builder.append(parts[i] + " ");
        }

        if (isNewThread == 1) {
            stitchedArray[stitchedArray.length - 1] = "&";
        }
        if (builder.length() != 0) {
            stitchedArray[1] = builder.toString().trim();
        }
        return stitchedArray;
    }

    /**
     * Checks to see if a file exists and if it is a directory
     * @param path relative path of the file
     * @return Status of the check
     * @throws IOException
     */
    public static String checkFile(String path) throws IOException {
        String absolutePath = path;
        File file = new File(absolutePath);

        if (!file.exists()) {
            return "File \"" + absolutePath + "\" not found.\n";
        }

        if (file.isDirectory()) {
            return "Cannot download non-regular file: " + absolutePath + "\n";
        }
        return "ok";
    }

    /**
     * Send a file across a socket connection
     * @param file File to be sent
     * @param commandId Command id registrered with this action
     * @param commandIds Command ID mapping
     * @param out Output stream to send the file to
     * @return A boolean which represents whether the sending was complete
     * @throws IOException
     */
    public static boolean sendFile(File file, DataOutputStream out, String commandId, SyncMap commandIds) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        long length = file.length(); // Get the size of the file
        int i;
        int bytesUploaded = 0;

        out.writeLong(length);
        while ((i = bis.read()) != -1) {
            out.write(i);

            if (bytesUploaded % 10000 == 0) {
                if (commandIds.get(commandId) == true) { // terminated
                    return false;
                }
            }
            bytesUploaded++;
        }
        out.flush();
        return true;
    }

    /**
     * Receive a file across a socket connection
     * @param fileBuffer FileBuffer to write to
     * @param in Incoming stream that will carry file data
     * @param commandId Command id registrered with this action
     * @param commandIds Command ID mapping
     * @return
     * @throws IOException
     */
    public static boolean receiveFile(OutputStream fileBuffer, DataInputStream in, String commandId, SyncMap commandIds) throws IOException {
        byte[] bytes = new byte[16*1024];
        long fileSize = in.readLong();
        int count;

        while (fileSize > 0 && (count = in.read(bytes, 0, (int)Math.min(bytes.length, fileSize))) != -1) {
            fileBuffer.write(bytes,0, count);
            fileSize -= count;

            if (commandIds.get(commandId) == true) { // terminated
                return false;
            }
        }
        fileBuffer.close();
        return true;
    }

    /**
     * Replaces linux style slashes with windows style slashes
     * @param str
     * @return
     */
    public static String convertSlashes(String str) {
        return str.replaceAll("/", File.separator);
    }
}
