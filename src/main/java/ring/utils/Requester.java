package ring.utils;

import com.proto.peer.*;
import io.grpc.ManagedChannel;
import ring.SocketIdentifier;

public class Requester {

    public static void setTokenRequest(SocketIdentifier service, Boolean tokenValue) {
        ManagedChannel channel = Grpc.createChannel(service);
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        SetTokenRequest request = SetTokenRequest.newBuilder()
                .setToken(tokenValue)
                .build();
        try{
            stub.setToken(request);
        } finally {
            Grpc.shutdown_channel(channel);
        }
    }

    public static void helloJobRequest(SocketIdentifier service, Integer peerPort) {
        ManagedChannel channel = Grpc.createChannel(service);
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        HelloRequest request = HelloRequest.newBuilder()
                .setPeerName(peerPort.toString())
                .build();
        try{
            stub.sendHello(request);
        } finally {
            Grpc.shutdown_channel(channel);
        }
    }

    public static HelloResponse processHelloRequest(SocketIdentifier service, String peerName) {
        ManagedChannel channel = Grpc.createChannel(service);
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        HelloRequest request = HelloRequest.newBuilder()
                .setPeerName(peerName)
                .build();
        HelloResponse response = stub.processHello(request);
        Grpc.shutdown_channel(channel);
        return response;

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

    public static AlgebraResponse processHelloRequest(SocketIdentifier service, String operation, Double number1, Double number2) {
        ManagedChannel channel = Grpc.createChannel(service);
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        AlgebraRequest request = AlgebraRequest.newBuilder()
                .setOperation(operation)
                .setNumber1(number1)
                .setNumber2(number2)
                .build();
        AlgebraResponse response = stub.processAlgebra(request);
        Grpc.shutdown_channel(channel);
        return response;

    }
}
