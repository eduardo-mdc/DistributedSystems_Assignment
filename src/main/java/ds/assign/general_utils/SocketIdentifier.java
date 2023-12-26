package ds.assign.general_utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketIdentifier that = (SocketIdentifier) o;
        return Objects.equals(hostname, that.hostname) &&
                Objects.equals(port, that.port);
}

}
