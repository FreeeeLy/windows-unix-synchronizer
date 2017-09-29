package sz.ly.handler;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Administrator on 2017/9/26.
 */
public abstract class Synchronizer {
    protected FTPClient ftpClient;

    protected String srcBaseDir;

    protected List<String> fileTypes;

    protected String dstBaseDir;

    public void connect(String host, int port) throws IOException {
        ftpClient.connect(host, port);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setKeepAlive(true);
    }

    public void login(String username, String password) throws IOException {
        ftpClient.login(username, password);
    }

    public void close() throws IOException {
        this.ftpClient.disconnect();
    }

    protected void closeIO(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void deleteFile(Path file) throws IOException;

    public abstract void updateFile(Path file) throws IOException;

    public abstract void createFile(Path file) throws IOException;

}
