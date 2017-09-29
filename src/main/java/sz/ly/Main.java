package sz.ly;

import sz.ly.handler.SyncToUnix;
import sz.ly.listener.DirChangeListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ly on 2017/9/26.
 */
public class Main {

    //Directory in Windows
    private static String srcDir = "";

    //Directory in Unix
    private static String dstDir = "";

    //The file type(suffix),which you need to sync,in Windows
    private static List<String> fileTypes = Arrays.asList();

    //The ip of unix machine
    private static String unixHost = "";

    //FTP port
    private static int ftpPort = 21;

    //Unix username
    private static String username = "";

    //Unix password
    private static String password = "";

    public static void main(String[] args) throws IOException {
        SyncToUnix syncToUnix = new SyncToUnix(srcDir, fileTypes, dstDir);
        syncToUnix.connect(unixHost, ftpPort);
        syncToUnix.login(username, password);
        DirChangeListener listener = new DirChangeListener(srcDir, syncToUnix);
        listener.start();
    }
}
