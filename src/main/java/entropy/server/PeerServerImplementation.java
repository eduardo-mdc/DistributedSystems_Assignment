package entropy.server;

import com.proto.peer.PeerServiceGrpc;
import general_utils.SocketIdentifier;

import java.util.List;
import java.util.logging.Logger;

public class PeerServerImplementation extends PeerServiceGrpc.PeerServiceImplBase{
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    Logger logger;

    public PeerServerImplementation(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours, Logger logger){
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        this.logger = logger;
    }
}
