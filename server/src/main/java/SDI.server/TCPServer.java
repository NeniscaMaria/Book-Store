package SDI.server;

import domain.Message;
import domain.ServerException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

public class TCPServer {
    private ExecutorService executorService;
    private Map<String, UnaryOperator<Message>> methodHandlers;

    public TCPServer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void addHandler(String methodName, UnaryOperator<Message> handler){
        methodHandlers.put(methodName,handler);
    }

    public void startServer(){
        try(var serverSocket = new ServerSocket(Message.PORT)){
            while(true){
                Socket client = serverSocket.accept();
                executorService.submit(new ClientHandler(client));
            }
        }catch(IOException e){
            throw new ServerException("Error connecting clients",e);
        }
    }

    private class ClientHandler implements Runnable{
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run(){
            try(var is = socket.getInputStream();  var os = socket.getOutputStream()){
                Message request = new Message();
                request.readFrom(is);
                System.out.println("Received request: "+request);
                //request header = method name
                //request body = method arguments
                Message response = methodHandlers.get(request.getHeader()).apply(request);
                response.writeTo(os);
            }catch(IOException e){
                throw new ServerException("Error processing client",e);
            }
        }
    }

}

