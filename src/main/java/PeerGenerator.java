import ring.Peer;
import ring.PortMapper;
import ring.SocketIdentifier;
import ring.central.CentralServer;
import ring.server.PeerServer;
import ring.utils.Requester;

import java.util.ArrayList;
import java.util.List;

public class PeerGenerator {
    public static void main(String[] args){
        PortMapper portMapper = new PortMapper();
        List<SocketIdentifier> socketList = portMapper.generateSocketList("127.0.0.1");
        SocketIdentifier startPeer = socketList.get(1);
        SocketIdentifier centralServer = new SocketIdentifier("127.0.0.1", 8000);

        new Thread(new CentralServer(centralServer)).start();

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
        Requester.setTokenRequest(startPeer, true);
        System.out.println("Token set to true in peer at port " + socketList.get(0).getPort() + "\n");
    }
}
