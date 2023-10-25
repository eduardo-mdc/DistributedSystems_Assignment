package peer.utils;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import peer.SocketIdentifier;

public class Grpc {

    public static ManagedChannel createChannel(SocketIdentifier server) {
        return ManagedChannelBuilder.forAddress(server.getHost(), server.getPort())
                .usePlaintext()
                .build();
    }

}
