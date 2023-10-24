package peer.server;
import com.proto.peer.*;
import com.proto.peer.PeerServiceGrpc;
import io.grpc.stub.StreamObserver;


public class PeerServerImplementation extends PeerServiceGrpc.PeerServiceImplBase {
    Boolean token = false;

    @Override
    public void getToken(EmptyRequest request, StreamObserver < GetTokenResponse > responseObserver) {
        responseObserver.onNext(GetTokenResponse.newBuilder().setResult(
                token
        ).build());
        responseObserver.onCompleted();
    }

    @Override
    public void setToken(SetTokenRequest request, StreamObserver < SetTokenResponse > responseObserver) {
        token = request.getToken(); // Change this to obtain request value.
        responseObserver.onNext(SetTokenResponse.newBuilder().setResult(
                token
        ).build());
        responseObserver.onCompleted();
    }



}