package chat.server;

import general_utils.Event;
import general_utils.PoissonProcess;

import java.util.Random;

public class MessageGenerator implements Runnable {
    PeerServerImplementation peerServer;

    public MessageGenerator(PeerServerImplementation peerServer){
        this.peerServer = peerServer;
    }

    @Override
    public void run() {
        double lambda = 5;
        PoissonProcess poisson = new PoissonProcess(lambda, new Random());
        Event event = new Event(peerServer.thisPeer, "word_" + peerServer.local_counter + "(" + peerServer.thisPeer.toString() + ")", peerServer.local_counter);
        peerServer.server_events.add(event);
        while (true){
            try {
                double time = poisson.timeForNextEvent() * 60.0 * 1000.0;
                peerServer.logger.info("<PEER SERVER " + peerServer.thisPeer.toString() + ">: Adding new Event to List In -> " + (int)time);

                Thread.sleep((int)time);

                peerServer.local_counter++;
                event = new Event(peerServer.thisPeer, "word_" + peerServer.local_counter + "(" + peerServer.thisPeer.toString() + ")", peerServer.local_counter);
                peerServer.server_events.add(event);
                peerServer.unprocessed_events.add(event);

                peerServer.logger.info("<PEER SERVER " + peerServer.thisPeer.toString() + ">: Added new Event to Local List -> " + event);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
