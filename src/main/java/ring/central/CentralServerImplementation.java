package ring.server;
import com.proto.peer.*;
import com.proto.peer.PeerServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import com.google.protobuf.Empty;
import ring.SocketIdentifier;
import ring.utils.Requester;


public class CentralServerImplementation extends PeerServiceGrpc.PeerServiceImplBase {
    Logger logger;


    public CentralServerImplementation(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void processHello(HelloRequest request, StreamObserver <HelloResponse> responseObserver) {
        final String peerName = request.getPeerName();
        logger.info("- CENTRAL SERVER : Received Hello from: " + peerName);

        // Respond to the gRPC request.
        responseObserver.onNext(HelloResponse.newBuilder().setGreeting("Greetings peer " + peerName).build());
        responseObserver.onCompleted();
    }

    @Override
    public void processAlgebra(AlgebraRequest request, StreamObserver <AlgebraResponse> responseObserver) {
        String operation = request.getOperation();
        Double number1 = request.getNumber1();
        Double number2 = request.getNumber2();

        switch (operation){
            case "add":
                responseObserver.onNext(AlgebraResponse.newBuilder().setResult(number1 + number2).build());
                break;
            case "sub":
                responseObserver.onNext(AlgebraResponse.newBuilder().setResult(number1 - number2).build());
                break;
            case "mul":
                responseObserver.onNext(AlgebraResponse.newBuilder().setResult(number1 * number2).build());
                break;
            case "div":
                responseObserver.onNext(AlgebraResponse.newBuilder().setResult(number1 / number2).build());
                break;
        }
        responseObserver.onCompleted();
    }





}