package ring.server;
import com.proto.peer.*;
import com.proto.peer.PeerServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;


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
    public void processAlgebra(AlgebraRequest request, StreamObserver<AlgebraResponse> responseObserver) {
        String operation = request.getOperation();
        Double number1 = request.getNumber1();
        Double number2 = request.getNumber2();
        logger.info("- CENTRAL SERVER: Received Algebra Request: <" + number1 + "><" + number2 + "> <" + operation + ">");

        try {
            double result = 0.0;

            switch (operation) {
                case "add":
                    result = number1 + number2;
                    break;
                case "sub":
                    result = number1 - number2;
                    break;
                case "mul":
                    result = number1 * number2;
                    break;
                case "div":
                    if (number2 != 0) {
                        result = number1 / number2;
                    } else {
                        responseObserver.onError(Status.INVALID_ARGUMENT
                                .withDescription("Division by zero is not allowed.")
                                .asRuntimeException());
                        return;
                    }
                    break;
                default:
                    responseObserver.onError(Status.INVALID_ARGUMENT
                            .withDescription("Unsupported operation: " + operation)
                            .asRuntimeException());
                    return;
            }

            responseObserver.onNext(AlgebraResponse.newBuilder().setResult(result).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            logger.warning("- CENTRAL SERVER: Error processing Algebra Request");
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }






}