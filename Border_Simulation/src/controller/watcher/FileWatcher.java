package controller.watcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.function.Consumer;

public class FileWatcher extends Thread{

	
	private Consumer<List<String>> updateStatusConsumer;
	
	@Override
	public void run() {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get("src"+ File.separator+"model"+File.separator+"border");
			dir.register(watcher,ENTRY_MODIFY);

			System.out.println("Watch Service registered for dir: " + dir.getFileName());

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();
					System.out.println(kind.name() + ": " + fileName);
					if(kind.equals(ENTRY_MODIFY)) {
						List<String> content = Files.readAllLines(dir.resolve(fileName));
						updateStatusConsumer.accept(content);
						for(String s: content)
							System.out.println(s);
						System.out.println("========================");
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	/**
	 * @return the updateStatus
	 */
	public Consumer<List<String>> getUpdateStatusConsumer() {
		return updateStatusConsumer;
	}
	/**
	 * @param updateStatus the updateStatus to set
	 */
	public void setUpdateStatusConsumer(Consumer<List<String>> updateStatus) {
		this.updateStatusConsumer = updateStatus;
	}
}
