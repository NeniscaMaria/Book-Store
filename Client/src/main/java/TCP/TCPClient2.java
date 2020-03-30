package TCP;

import domain.Client;
import domain.Message;
import domain.Message2;
import domain.ServerException;
import java.io.IOException;
import java.net.Socket;

public class TCPClient2 {
    public Message2<Client> sendAndReceive(Message2<Client> request){
        try(var socket = new Socket(Message.HOST, Message.PORT);
            var is = socket.getInputStream();
            var os = socket.getOutputStream()){

            //send request
            request.writeTo(os);
            //receive response
            Message2<Client> response = new Message2<Client>();
            response.readFrom(is);
            return response;
        } catch (IOException e) {
            throw new ServerException("error connecting to server "+e.getMessage(),e);
        }
    }
}
