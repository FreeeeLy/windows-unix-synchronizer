package sz.ly.listener;

import com.sun.nio.file.ExtendedWatchEventModifier;
import sz.ly.handler.Synchronizer;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by Administrator on 2017/9/26.
 */
public class DirChangeListener {

    private String directory;

    private Synchronizer handler;

    private WatchService watchService;

    public DirChangeListener(String directory, Synchronizer handler) {
        this.directory = directory;
        this.handler = handler;
    }

    private void registry() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(directory);
            path.register(watchService, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW}, ExtendedWatchEventModifier.FILE_TREE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        registry();
        Path base = Paths.get(directory);
        while (true) {
            WatchKey watchKey;
            try {
                watchKey = watchService.take();
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    filename = base.resolve(filename);
                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        handler.deleteFile(filename);
                        continue;
                    }
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        handler.updateFile(filename);
                        continue;
                    }
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        handler.createFile(filename);
                        continue;
                    }
                }
                boolean valid = watchKey.reset();
                if (!valid) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
