package ds.assign.ring.utils;

import com.proto.peer.*;
import ds.assign.general_utils.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import ds.assign.general_utils.SocketIdentifier;

public class Requester {

    public static void setTokenRequest(SocketIdentifier service, Boolean tokenValue) {
        ManagedChannel channel = Grpc.createChannel(service);

        try {
            RingPeerServiceGrpc.RingPeerServiceBlockingStub stub = RingPeerServiceGrpc.newBlockingStub(channel);
            SetTokenRequest request = SetTokenRequest.newBuilder()
                    .setToken(tokenValue)
                    .build();
            stub.setToken(request);

        } catch (StatusRuntimeException e) {
            // Handle gRPC-specific exceptions
            if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                System.err.println("Error: Server <"+service.getHostname() + ":" + service.getPort() +"> is unavailable. Check if the server is running.");
            } else {
                e.printStackTrace();
            }

        } catch (Exception e) {
            // Handle general exceptions
            e.printStackTrace();

        } finally {
            Grpc.shutdown_channel(channel);
        }
    }


    public static void algebraJobRequest(SocketIdentifier service, String operation, Double number1, Double number2){
        ManagedChannel channel = Grpc.createChannel(service);
        RingPeerServiceGrpc.RingPeerServiceBlockingStub stub = RingPeerServiceGrpc.newBlockingStub(channel);
        AlgebraRequest request = AlgebraRequest.newBuilder()
                .setOperation(operation)
                .setNumber1(number1)
                .setNumber2(number2)
                .build();
        try {
            stub.sendAlgebra(request);
        }
        finally {
            Grpc.shutdown_channel(channel);
        }
    }

    public static AlgebraResponse processAlgebraRequest(SocketIdentifier service, String operation, Double number1, Double number2) {
        ManagedChannel channel = Grpc.createChannel(service);
        try {
            RingPeerServiceGrpc.RingPeerServiceBlockingStub stub = RingPeerServiceGrpc.newBlockingStub(channel);
            AlgebraRequest request = AlgebraRequest.newBuilder()
                    .setOperation(operation)
                    .setNumber1(number1)
                    .setNumber2(number2)
                    .build();
            AlgebraResponse response = stub.processAlgebra(request);
            return response;
        } catch (StatusRuntimeException e) {
            // Print the error and handle it accordingly
            System.err.println("Error calling processAlgebra: " + e.getStatus());
            throw e; // or handle the exception as needed
        } finally {
            Grpc.shutdown_channel(channel);
        }
    }

}
