import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerClientThread extends Thread {

        Socket serverClient;
        int clientNo;
        String[] takingData;

        ServerClientThread(Socket inSocket, int counter){
            serverClient    =   inSocket;
            clientNo        =   counter;
        }

        public void run(){
            try{
                DataInputStream inStream    =   new DataInputStream(serverClient.getInputStream());
                DataOutputStream outStream  =   new DataOutputStream(serverClient.getOutputStream());
                String clientMessage        =   "";
                String serverMessage        =   "";
                while(true){

                    clientMessage   =   inStream.readUTF();
                    takingData = clientMessage.split("/");
                    if (takingData[5].equals("tp")){
                        serverMessage = "[Server] >> " + ToplamaIslemi(Integer.parseInt(takingData[6]));
                    } else if (takingData[5].equals("fak")){
                        serverMessage = "[Server] >> " + FaktoriyelIslemi(Integer.parseInt(takingData[6]));
                    } else if (takingData[5].equals("cikis")){
                        serverMessage = "[Server] >> Client was disconnected";
                        break;
                    }
                    outStream.writeUTF(serverMessage);
                    outStream.flush();
                    DosyaIslemleri(clientMessage);
                }

                inStream.close();
                outStream.close();
                serverClient.close();

            }catch(Exception ex){
                System.out.println(ex);
            }finally{
                System.out.println("Client " + clientNo + " exit!! ");
            }
        }

        public static int FaktoriyelIslemi(int input){
            int sonuc =1;
            for (int i=1 ;i<=input;i++)
                sonuc *= i;
            return sonuc;
        }

        public static int ToplamaIslemi(int input){
            int sonuc = 0;
            while(true){
                if(input<=0)
                    break;
                sonuc += input%10;
            }
            return sonuc;
        }

        public static void DosyaIslemleri(String clientMessage){
            // TODO Dosya İşlemleri
        }
    }
