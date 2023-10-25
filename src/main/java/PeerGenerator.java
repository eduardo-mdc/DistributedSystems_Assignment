import io.grpc.ManagedChannel;
import peer.Peer;
import peer.SocketIdentifier;
import peer.utils.Grpc;
import peer.utils.Requester;

import java.util.ArrayList;
import java.util.List;

public class PeerGenerator {


    private static List<SocketIdentifier> generateSocketList() {
        List<SocketIdentifier> socketList = new ArrayList<>();
        socketList.add(new SocketIdentifier("127.0.0.1", 5000));
        socketList.add(new SocketIdentifier("127.0.0.1", 5001));
        return socketList;
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
        Requester.setTokenRequest(socketList.get(0), true);

        System.out.println("Token set to true in peer at port " + socketList.get(0).getPort() + "\n");
    }
}
