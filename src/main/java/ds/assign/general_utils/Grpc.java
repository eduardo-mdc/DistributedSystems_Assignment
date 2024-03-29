package ds.assign.general_utils;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class Grpc {

    private static Integer timeout = 5;

    public static ManagedChannel createChannel(SocketIdentifier server) {
        return ManagedChannelBuilder.forAddress(server.getHostname(), server.getPort())
                .usePlaintext()
                .build();
    }

    public static void shutdown_channel(ManagedChannel channel) {
        if (channel != null) {
            channel.shutdown();
            try {
                channel.awaitTermination(timeout, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
