
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileRequestedClient extends ServiceMessage{
    private String fileName;
    private long fileSize;
    private byte[] content;

    public String getFileName() {
        return fileName;
    }
    public long getFileSize() {
        return fileSize;
    }
    public byte[] getContent() {
        return content;
    }

    public FileRequestedClient(Path path) throws IOException {
        fileName = path.getFileName().toString();
        content = Files.readAllBytes(path);
    }
}
