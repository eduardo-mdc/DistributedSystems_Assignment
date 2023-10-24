package peer.server;
import com.proto.peer.*;
import com.proto.peer.PeerServiceGrpc;
import io.grpc.stub.StreamObserver;


public class PeerServerImplementation extends PeerServiceGrpc.PeerServiceImplBase {
    @Override
    public void getToken(TokenRequest request, StreamObserver < TokenResponse > responseObserver) {
        responseObserver.onNext(TokenResponse.newBuilder().setResult(
                true
        ).build());
        responseObserver.onCompleted();
    }
}