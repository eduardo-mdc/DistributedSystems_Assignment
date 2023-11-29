package entropy.server;

import com.google.protobuf.Empty;
import com.proto.peer.Entropy;
import com.proto.peer.PullResponse;
import com.proto.peer.PushRequest;
import com.proto.peer.EntropyPeerServiceGrpc;
import general_utils.SocketIdentifier;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class PeerServerImplementation extends EntropyPeerServiceGrpc.EntropyPeerServiceImplBase{
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    Logger logger;

    List<Double> server_values;

    public PeerServerImplementation(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours, Logger logger){
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        this.logger = logger;
        server_values = new ArrayList<Double>();
        server_values.add(1.0);
        server_values.add(2.0);
        server_values.add(3.0);
    }
    public void push(PushRequest request, StreamObserver <Empty> responseObserver) {
        List<Double> values = request.getValuesList();
        // Process the received list of doubles as needed
        server_values.addAll(values);

        // You can log or perform any other processing here

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    public void pull(Empty request, StreamObserver<PullResponse> responseObserver) {
        List<Double> values = fetchDataFromSource();

        // Build and send the response
        PullResponse pullResponse = PullResponse.newBuilder().addAllValues(values).build();
        responseObserver.onNext(pullResponse);
        responseObserver.onCompleted();
    }

    // Replace this method with the actual logic to fetch data from your source
    private List<Double> fetchDataFromSource() {
        // Example: Fetch data from a database, file, or other source
        return server_values;
    }
}
