package general_utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SocketIdentifier {
    private final String hostname;
    private Integer port;

    @JsonCreator
    public SocketIdentifier(
            @JsonProperty("hostname") String hostname,
            @JsonProperty("port") Integer port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return hostname + ":" + port;
    }
}
