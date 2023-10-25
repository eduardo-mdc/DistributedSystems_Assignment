import com.proto.peer.PeerServiceGrpc;
import com.proto.peer.SetTokenRequest;
import com.proto.peer.SetTokenResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import peer.Peer;
import peer.SocketIdentifier;

import java.util.ArrayList;
import java.util.List;

public class PeerGenerator {


    private static List<SocketIdentifier> generateSocketList() {
        List<SocketIdentifier> socketList = new ArrayList<>();
        socketList.add(new SocketIdentifier("127.0.0.1", 5000));
        socketList.add(new SocketIdentifier("127.0.0.1", 5001));
        return socketList;
    }

    private static ManagedChannel createChannel(SocketIdentifier server) {
        return ManagedChannelBuilder.forAddress(server.getHost(), server.getPort())
                .usePlaintext()
                .build();
    }

    private static void set_token(ManagedChannel channel, Boolean tokenValue) {
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        SetTokenResponse response = stub.setToken(SetTokenRequest.newBuilder()
                .setToken(tokenValue)
                .build());
    }
    public static void main(String[] args) throws InterruptedException {
        List<SocketIdentifier> socketList = generateSocketList();

        for(SocketIdentifier socketId : socketList){
            Peer peer = new Peer(socketId.getHost(), socketId.getPort());
            peer.start();
        }

        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Set token to true in the first peer in the list
        ManagedChannel channel = createChannel(socketList.get(0));
        set_token(channel, true);

        System.out.println("Token set to true in peer at port " + socketList.get(0).getPort() + "\n");
    }
}
