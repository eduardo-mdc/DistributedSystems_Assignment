package ring.utils;

import com.proto.peer.*;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import ring.SocketIdentifier;

public class Requester {

    public static void setTokenRequest(SocketIdentifier service, Boolean tokenValue) {
        ManagedChannel channel = Grpc.createChannel(service);

        try {
            PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
            SetTokenRequest request = SetTokenRequest.newBuilder()
                    .setToken(tokenValue)
                    .build();

            stub.setToken(request);

        } catch (StatusRuntimeException e) {
            // Handle gRPC-specific exceptions
            if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                System.err.println("Error: Server <"+service.getHost() + ":" + service.getPort() +"> is unavailable. Check if the server is running.");
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
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
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
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
            PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
            AlgebraRequest request = AlgebraRequest.newBuilder()
                    .setOperation(operation)
                    .setNumber1(number1)
                    .setNumber2(number2)
                    .build();
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            AlgebraResponse response = stub.processAlgebra(request);
            System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCC RESULT : " + response.getResult());
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
