package sz.ly;

import sz.ly.handler.SyncToUnix;
import sz.ly.listener.DirChangeListener;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Ly on 2017/9/26.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        SyncToUnix syncToUnix = new SyncToUnix("E:\\git\\Mit6.824\\src", Arrays.asList(".go"), "/home/ly/Mit6.824/src");
        syncToUnix.connect("192.168.117.129", 21);
        syncToUnix.login("ly", "ly");
        DirChangeListener listener = new DirChangeListener("E:\\git\\Mit6.824\\src", syncToUnix);
        listener.start();
    }
}
