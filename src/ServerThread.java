import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    Socket serverClient;
    static int clientNo;
    String[] takingData;


    ServerThread(Socket inSocket, int counter){
        serverClient    =   inSocket;
        clientNo        =   counter;
    }

    public void run(){
        DataInputStream inStream = null;
        DataOutputStream outStream = null;
        try{
            inStream   =   new DataInputStream(serverClient.getInputStream());
            outStream  =   new DataOutputStream(serverClient.getOutputStream());
            String clientMessage        =   "";
            String serverMessage        =   "";
            while(true){

                // taking client message
                clientMessage   =   inStream.readUTF();

                // we are split the taking data
                takingData = clientMessage.split("/");

                // the 6th element of taking data is number by sending user
                // this data coming String type we will convert to int;
                int equal = Integer.parseInt(takingData[6]);

                // the 5th element of taking data is what do user want?
                switch (takingData[5]) {
                    case "tp":
                        System.out.println("Toplama");
                        System.out.println(equal);
                        while(true) {
                            // We are in the loop until one digit remains
                            serverMessage = "[Server] >> " + ToplamaIslemi(equal);
                            equal = ToplamaIslemi(equal);
                            if (equal < 10){
                                outStream.writeUTF(serverMessage);
                                outStream.flush();
                                outStream.writeUTF("[Server] >> ok");
                                outStream.flush();
                                break;
                            }
                            System.out.println(equal);
                            outStream.writeUTF(serverMessage);
                            outStream.flush();
                            fileProcces(clientMessage);
                        }
                        break;
                    case "fak":
                        System.out.println("faktoriyel");
                        serverMessage = "[Server] >> " + FaktoriyelIslemi(equal);
                        System.out.println(FaktoriyelIslemi(equal));
                        outStream.writeUTF(serverMessage);
                        outStream.flush();
                        outStream.writeUTF("[Server] >> ok");
                        outStream.flush();
                        fileProcces(clientMessage);
                        break;
                    case "cikis":
                        serverMessage = "[Server] >> Client was disconnected";
                        outStream.writeUTF(serverMessage);
                        outStream.flush();
                        fileProcces(clientMessage);
                        break;
                }


            }
        }catch(Exception ex){
            System.out.println("Server Client Thread Exception");
            System.out.println(ex);
        }finally{

            // if client left unexpectedly we are cutting connection
            System.out.println("Client " + clientNo + " exit!! ");
            try {
                assert inStream != null;
                assert outStream != null;
                inStream.close();
                outStream.close();
                serverClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    // Factorial Func.
    public  int FaktoriyelIslemi(int input){
        int sonuc = 1;
        for (int i=1 ;i<=input;i++)
            sonuc *= i;
        return sonuc;

    }

    // Sum Func.
    public int ToplamaIslemi(int input){
        int sonuc = 0;
        while(input > 0){
            sonuc += (input%10);
            input = input/10;
        }
        return sonuc;
    }

    public static void fileProcces(String clientMessage){

        // Log file path
        // if you wanna running this program you must changing this line
        File file = new File("C:\\Users\\musta\\IdeaProjects\\javaSocketProgramming\\logs\\server.logs");

        if(!file.exists()){
            try {
                // if file does'nt exist we are creating file
                file.createNewFile();
            }catch (IOException ex){
                System.out.println(ex);
            }
        }

        try {
            // splitting and pushing client data
            String logs[] = clientMessage.split("/");

            // file writer creating
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // writing the file all client information
            bufferedWriter.write("Client " + clientNo + ">> " + logs[0] + " " + logs[1] + " " + logs[2] + " " +
                    logs[3] + " " + logs[4] + " " + logs[5]+System.lineSeparator());

            // file closing
            bufferedWriter.close();
        }catch (IOException ex){
            System.out.println(ex);
        }
    }
}
