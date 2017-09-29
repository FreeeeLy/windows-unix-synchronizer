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

    private static String srcDir = "";

    private static String dstDir = "";

    private static List<String> fileTypes = Arrays.asList();

    private static String unixHost = "";

    private static int ftpPort = 21;

    private static String username = "";

    private static String password = "";

    public static void main(String[] args) throws IOException {
        SyncToUnix syncToUnix = new SyncToUnix(srcDir, fileTypes, dstDir);
        syncToUnix.connect(unixHost, ftpPort);
        syncToUnix.login(username, password);
        DirChangeListener listener = new DirChangeListener(srcDir, syncToUnix);
        listener.start();
    }
}
