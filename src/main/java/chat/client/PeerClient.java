package chat.client;

import general_utils.SocketIdentifier;

import java.util.List;
import java.util.logging.Logger;

public class PeerClient implements Runnable{
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    Logger logger;

    final int lambda = 5;

    public PeerClient(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours, Logger logger){
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        this.logger = logger;

    }

    @Override
    public void run() {

    }
}
