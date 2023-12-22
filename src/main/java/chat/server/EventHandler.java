package chat.server;

import chat.server.PeerServerImplementation;
import com.proto.peer.*;
import general_utils.Event;
import general_utils.SocketIdentifier;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

public class EventHandler implements Runnable{

    PeerServerImplementation peerServer;
    public EventHandler(PeerServerImplementation peerServer){
        this.peerServer = peerServer;
    }

    @Override
    public void run() {
        while (true){
            try {
                if(!peerServer.unprocessed_events.isEmpty()){
                    Event event = peerServer.unprocessed_events.remove();
                    peerServer.logger.info("<PEER SERVER " + peerServer.thisPeer.toString() + ">: Processing New Local Event -> " + event);
                    for (SocketIdentifier neighbour : peerServer.neighbours){
                        peerServer.logger.info("<PEER SERVER " + peerServer.thisPeer.toString() + ">: Sending new Event to Neighbour -> " + neighbour.toString());
                        this.sendMessage(neighbour, event);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(SocketIdentifier destination, Event event) {
        // Create a channel to the destination peer
        ManagedChannel channel = ManagedChannelBuilder.forAddress(destination.getHostname(), destination.getPort())
                .usePlaintext()
                .build();

        // Create a stub for the PeerService
        ChatPeerServiceGrpc.ChatPeerServiceBlockingStub stub = ChatPeerServiceGrpc.newBlockingStub(channel);

        // Build and send the PushRequest
        MessageRequest messageRequest = MessageRequest.newBuilder().
                setMessage(event.message).
                setCounter(event.counterTimestamp++).
                setSourceIp(peerServer.thisPeer.getHostname()).
                setSourcePort(peerServer.thisPeer.getPort()).
                build();
        MessageResponse response = stub.sendMessage(messageRequest);

        // Close the channel after the request is sent
        channel.shutdown();
    }
}
