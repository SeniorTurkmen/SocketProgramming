import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadSocketServer {
    public static void main(String[] args) throws Exception {
        try{
            ServerSocket server =   new ServerSocket(4027);
            int counter         =   0;
            System.out.println("Server Started ....");
            while(true){
                counter++;

                //server accept the client connection request
                Socket serverClient     =   server.accept();

                System.out.println(" >> " + "Client No:" + counter + " started!");

                //send  the request to a separate thread
                ServerClientThread sct  =   new ServerClientThread(serverClient, counter);

                sct.start();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
