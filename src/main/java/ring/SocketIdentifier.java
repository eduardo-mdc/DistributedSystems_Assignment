package ring;

public class SocketIdentifier {
    private String host;
    private Integer port;
    public SocketIdentifier(String host, Integer port) {
        this.host = host;
        this.port = port;
    }
    public String getHost() {
        return host;
    }
    public Integer getPort() {
        return port;
    }
}
