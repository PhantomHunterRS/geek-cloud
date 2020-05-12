public class FileSentServer extends ServiceMessage {
    private String fileName;

    public String getFileName() {
        return fileName;
    }
    public FileSentServer(String fileName) {
        this.fileName = fileName;
    }
}
