import ring.RingPeer;
import ring.RingPeerMapper;
import general_utils.SocketIdentifier;
import ring.central.CentralServer;
import ring.utils.Requester;

import java.util.List;

public class RingGenerator {
    public static void main(String[] args){
        RingPeerMapper ringPeerMapper = new RingPeerMapper();
        List<SocketIdentifier> socketList = ringPeerMapper.generateSocketList("127.0.0.1");
        SocketIdentifier startPeer = socketList.get(1);
        SocketIdentifier centralServer = new SocketIdentifier("127.0.0.1", 8000);

        new Thread(new CentralServer(centralServer)).start();

        for(SocketIdentifier socketId : socketList){
            RingPeer ringPeer = new RingPeer(socketId.getHost(), socketId.getPort());
            ringPeer.start();
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
