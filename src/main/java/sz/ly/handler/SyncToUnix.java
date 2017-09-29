package sz.ly.handler;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Ly on 2017/9/26.
 */
public class SyncToUnix extends Synchronizer {
    public SyncToUnix(String srcBaseDir, List<String> fileTypes, String dstBaseDir) {
        this.srcBaseDir = srcBaseDir;
        this.fileTypes = fileTypes;
        this.dstBaseDir = dstBaseDir;
        this.ftpClient = new FTPClient();
    }

    public SyncToUnix(String srcBaseDir, String dstBaseDir) {
        this(srcBaseDir, null, dstBaseDir);
    }

    private boolean transferFile(Path path) throws IOException {
        BufferedInputStream inputStream = null;
        OutputStream out = null;
        File file = path.toFile();
        String type = file.getName().substring(file.getName().lastIndexOf('.'));
        if (!fileTypes.contains(type)) {
            return true;
        }
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            String dstFile = dstBaseDir + file.getCanonicalPath().replace(srcBaseDir, "").replace("\\", "/");

            ftpClient.dele(dstFile);

            out = ftpClient.appendFileStream(new String(dstFile.getBytes("UTF-8")));
            byte[] bytes = new byte[1024];
            int c = inputStream.read(bytes);
            while (c != -1) {
                out.write(bytes, 0, c);
                c = inputStream.read(bytes);
            }
            out.flush();
        } catch (Exception e) {
            System.err.println("SyncToUnix : Transfer file " + file + " failed");
            e.printStackTrace();
        } finally {
            closeIO(inputStream);
            closeIO(out);
        }
        return ftpClient.completePendingCommand();
    }

    private boolean transferDir(Path path) throws IOException {
        ftpClient.dele(path.toString());
        String dstFile = dstBaseDir + path.toFile().getCanonicalPath().replace(srcBaseDir, "").replace("\\", "/");
        ftpClient.makeDirectory(dstFile);
        for (File file : path.toFile().listFiles()) {
            if (file.isDirectory()) {
                dstFile = dstBaseDir + file.getCanonicalPath().replace(srcBaseDir, "").replace("\\", "/");
                ftpClient.makeDirectory(dstFile);
            } else {
                transferFile(file.toPath());
            }
        }
        return true;
    }

    @Override
    public void deleteFile(Path path) throws IOException {
        ftpClient.dele(path.toString());
    }

    @Override
    public void updateFile(Path path) throws IOException {
        if (!path.toFile().isDirectory()) {
            transferFile(path);
        } else {
            transferDir(path);
        }
    }

    @Override
    public void createFile(Path file) throws IOException {
        updateFile(file);
    }
}
