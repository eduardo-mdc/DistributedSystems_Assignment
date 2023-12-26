package ds.assign;

import ds.assign.ring.RingPeer;
import ds.assign.ring.RingPeerMapper;
import ds.assign.general_utils.SocketIdentifier;
import ds.assign.ring.central.CentralServer;
import ds.assign.ring.utils.Requester;

import java.util.List;

public class RingGenerator {

    private static final String peer_host = "localhost";
    public static void main(String[] args){
        RingPeerMapper ringPeerMapper = new RingPeerMapper();
        List<SocketIdentifier> socketList = ringPeerMapper.generateSocketList(peer_host);
        SocketIdentifier startPeer = socketList.get(1);
        SocketIdentifier centralServer = new SocketIdentifier(peer_host, 8000);

        new Thread(new CentralServer(centralServer)).start();

        for(SocketIdentifier socketId : socketList){
            SocketIdentifier nextSocket = new SocketIdentifier(peer_host, ringPeerMapper.portMap.get(socketId.getPort()));
            RingPeer ringPeer = new RingPeer(socketId,nextSocket);
            ringPeer.start();
        }

        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Sending Token to First Peer " + socketList.get(0).getPort() + "\n");
        //Set token to true in the first peer in the list
        Requester.setTokenRequest(startPeer, true);
        System.out.println("Token set to true in peer at port " + socketList.get(0).getPort() + "\n");
    }
}
