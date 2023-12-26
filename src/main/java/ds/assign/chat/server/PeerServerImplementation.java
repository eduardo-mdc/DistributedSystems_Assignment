package ds.assign.chat.server;

import com.proto.peer.ChatPeerServiceGrpc;
import com.proto.peer.MessageRequest;
import com.proto.peer.MessageResponse;
import ds.assign.general_utils.Event;
import ds.assign.general_utils.SocketIdentifier;
import io.grpc.stub.StreamObserver;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import static java.lang.Math.max;

public class PeerServerImplementation extends ChatPeerServiceGrpc.ChatPeerServiceImplBase {
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    Logger logger;
    PriorityBlockingQueue<Event> server_events;

    Queue<Event> unprocessed_events;

    Integer local_counter;

   public PeerServerImplementation(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours, Logger logger){
        this.local_counter = 0;
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        this.logger = logger;
        this.server_events = new PriorityBlockingQueue<>(11, Comparator.comparing(Event::getCounterTimestamp));
        this.unprocessed_events = new ConcurrentLinkedQueue<>();
        //Handles Local Event creation and unprocessed events queueing
        Thread messageGeneratorThread = new Thread(new MessageGenerator(this));
        messageGeneratorThread.start();
        //Checks for unprocessed events and runs accordingly
        Thread eventHandlerThread = new Thread(new EventHandler(this));
        eventHandlerThread.start();
   }

    public void sendMessage(MessageRequest request, StreamObserver <MessageResponse> responseObserver) {
        String message = request.getMessage();
        SocketIdentifier sender = new SocketIdentifier(request.getSourceIp(), request.getSourcePort());
        logger.info("<PEER SERVER " + thisPeer.toString() + ">: Received message from " + sender + " to " + this.thisPeer + " : " + message);
        Event event = new Event(sender, message, request.getCounter());
        server_events.add(event);
        logger.info("<PEER SERVER " + thisPeer.toString() + ">: Current Server Values ->" + server_events);
        local_counter = max(local_counter, event.getCounterTimestamp());
        //TODO Change this to a code format ?
        MessageResponse response = MessageResponse.newBuilder().setResponse("message_sent").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
