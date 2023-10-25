package peer.utils;

import com.proto.peer.HelloRequest;
import com.proto.peer.PeerServiceGrpc;
import com.proto.peer.SetTokenRequest;
import io.grpc.ManagedChannel;
import peer.SocketIdentifier;

public class Requester {
    public static void setTokenRequest(SocketIdentifier service, Boolean tokenValue) {
        ManagedChannel channel = Grpc.createChannel(service);
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        SetTokenRequest request = SetTokenRequest.newBuilder()
                .setToken(tokenValue)
                .build();
        stub.setToken(request);
    }

    public static void helloJobRequest(SocketIdentifier service, Integer peerPort) {
        ManagedChannel channel = Grpc.createChannel(service);
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        HelloRequest request = HelloRequest.newBuilder()
                .setPeerName(peerPort.toString())
                .build();

        stub.sendHello(request);
    }
}
