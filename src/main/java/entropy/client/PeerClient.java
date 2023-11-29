package entropy.client;

import com.google.protobuf.Empty;
import com.proto.peer.PullResponse;
import com.proto.peer.PushRequest;
import com.proto.peer.EntropyPeerServiceGrpc;
import general_utils.PoissonProcess;
import general_utils.SocketIdentifier;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.Random;
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
    private SocketIdentifier getRandomNeighbour() {
        Random rand = new Random();
        return neighbours.get(rand.nextInt(neighbours.size()));
    }


    private List<Double> sendPull(SocketIdentifier neighbour) {
        // Create a channel to the neighbour
        ManagedChannel channel = ManagedChannelBuilder.forAddress(neighbour.getHostname(), neighbour.getPort())
                .usePlaintext()
                .build();

        // Create a stub for the PeerService
        EntropyPeerServiceGrpc.EntropyPeerServiceBlockingStub stub = EntropyPeerServiceGrpc.newBlockingStub(channel);

        // Build and send the Empty request for pull
        Empty empty = Empty.newBuilder().build();
        PullResponse pullResponse = stub.pull(empty);

        // Process the received data from the pull response as needed
        List<Double> receivedData = pullResponse.getValuesList();
        logger.info("<PEER CLIENT>: Received data from neighbour: " + receivedData);

        // Close the channel after the request is sent
        channel.shutdown();

        return receivedData;
    }

    private void sendPush(SocketIdentifier destination, List<Double> values) {
        logger.info("<PEER CLIENT>: Fetched data from local server");

        // Create a channel to the destination peer
        ManagedChannel channel = ManagedChannelBuilder.forAddress(destination.getHostname(), destination.getPort())
                .usePlaintext()
                .build();

        // Create a stub for the PeerService
        EntropyPeerServiceGrpc.EntropyPeerServiceBlockingStub stub = EntropyPeerServiceGrpc.newBlockingStub(channel);

        // Build and send the PushRequest
        PushRequest pushRequest = PushRequest.newBuilder().addAllValues(values).build();
        stub.push(pushRequest);

        // Close the channel after the request is sent
        channel.shutdown();
    }

    @Override
    public void run() {
        logger.info("<PEER CLIENT>: Client Running at " + thisPeer.getHostname() + ":" + thisPeer.getPort());
        PoissonProcess poisson = new PoissonProcess(lambda, new Random());
        while (true){
            try {
                double time = poisson.timeForNextEvent() * 60.0 * 1000.0;
                System.out.println("next event in -> " + (int)time + " ms");
                Thread.sleep((int)time);

                Random rand = new Random();
                int command = rand.nextInt(3);

                SocketIdentifier randomNeighbour = getRandomNeighbour();
                switch (command) {
                    case 2:
                        sendPush(randomNeighbour, sendPull(randomNeighbour));
                        break;
                    case 1:
                        // Push
                        sendPush(randomNeighbour, sendPull(thisPeer));
                        break;
                    case 0:
                        //Pull
                        List<Double> receivedData = sendPull(randomNeighbour);
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
