package TCP;

import domain.Message;
import domain.ServerException;
import java.io.IOException;
import java.net.Socket;

public class TCPClient {
    public Message sendAndReceive(Message request){
        try(var socket = new Socket(Message.HOST, Message.PORT);
            var is = socket.getInputStream();
            var os = socket.getOutputStream()){

            //send request
            request.writeTo(os);
            //receive response
            Message response = new Message();
            response.readFrom(is);
            return response;
        } catch (IOException e) {
            throw new ServerException("error connecting to server "+e.getMessage(),e);
        }
    }
}
