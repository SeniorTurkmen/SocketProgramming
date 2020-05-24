import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class ClientThread {
    static int girdi = 0, dataOutTime = 0, dataInTime = 0;

    // Menu Caller Func.
    private static void getMenu(){
        System.out.println( "*************************\n" +
                "***********Menu**********\n" +
                "1. Toplama Islemi\n" +
                "2. Faktoriyel Islemi\n" +
                "3. Cikis\n" +
                "*************************\n" +
                "Seciminizi yapiniz...");
        System.out.print(">>");
    }

    public static void main(String[] args) throws Exception {
        // User ip address added IP Variable
        InetAddress IP = InetAddress.getLocalHost();

        // Client - Server Connection settings...
        Socket socket               =   new Socket("127.0.0.1",4027);
        DataInputStream inStream    =   new DataInputStream(socket.getInputStream());
        DataOutputStream outStream  =   new DataOutputStream(socket.getOutputStream());
        BufferedReader br           =   new BufferedReader(new InputStreamReader(System.in));
        try{
            // serverMessage define "[Server] >> ok"
            String serverMessage        =   "[Server] >> ok";
            // Packet define
            String header = "Ip Adress:/" + IP.getHostAddress() + "/Date & Time:/" + (new Date().toString());

            while(true){
                // Checking Server response
                if (serverMessage.equals("[Server] >> ok")){
                    getMenu();

                    // taking user input "What do he/she want?"
                    Scanner secim = new Scanner(System.in);
                    girdi = secim.nextInt();
                    switch (girdi) {
                        case 1:
                            System.out.println("Lutfen rakamlarının toplanmasını istediğiniz sayiyi giriniz...");

                            // taking user input for summary
                            Scanner tp = new Scanner(System.in);
                            girdi = tp.nextInt();

                            // Adding header info
                            header += "/Islem Turu:/" + "tp/" + girdi;
                            break;
                        case 2:
                            System.out.println("Lutfen faktoriyelini almak istediginiz sayiyi giriniz...");

                            // taking user input for factorial
                            Scanner fak = new Scanner(System.in);
                            girdi = fak.nextInt();

                            // Adding header info
                            header += "/Islem Turu:/" + "fak/" + girdi;
                            break;
                        case 3:
                            // User exit process
                            header += "/Islem Turu:/" + "cikis/" + girdi;
                            break;
                        default:
                            // checking undefined selection situation
                            System.out.println("Lutfen gecerli bir islem seciniz...");
                            getMenu();
                            break;
                    }

                    // User information sending server
                    outStream.writeUTF(header);

                    // taking data sending time for calculate the delay
                    dataOutTime = (int) System.nanoTime();

                    // port cleaning
                    outStream.flush();

                    // reassignment header information
                    header = "Ip Adress:/" + IP.getHostAddress() + "/Date & Time:/" + (new Date().toString());
                }

                // taking server response
                serverMessage   =   inStream.readUTF();
                if (serverMessage.equals("Client was disconnected")) {
                    // if user wanna disconnect we are closing connection and shutdown the program
                    inStream.close();
                    outStream.close();
                    System.exit(0);
                }

                // printing server response
                System.out.println(serverMessage);

                //taking data when came for calculating delay
                dataInTime = (int) System.nanoTime();

                // Printing all logs in the file
                fileProcces();
            }
        }catch(Exception e){
            System.out.println("client ex...");
            System.out.println(e);
        }finally {
            // if program closing unexpectedly we are cutting connection
            inStream.close();
            outStream.close();
            socket.close();
        }
    }

    public static void fileProcces(){

        // Log file path
        // if you wanna running this program you must changing this line
        File file = new File("C:\\Users\\musta\\IdeaProjects\\javaSocketProgramming\\logs\\client.logs");
        if(!file.exists()){
            try {
                // if file does'nt exist we are creating file
                file.createNewFile();
            }catch (IOException ex){
                System.out.println(ex);
            }
        }
        try {
            //  calculating the Delay
            int delay = dataInTime - dataOutTime;

            // time assignment
            String dateTime = new SimpleDateFormat("dd-MM-yyy mm:HH:ss").format(Calendar.getInstance().getTime());

            // file writer creating
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // writing the file all server information
            bufferedWriter.write("Date & Time: " + dateTime + " Delay: " + delay + System.lineSeparator());

            // file closing
            bufferedWriter.close();
        }catch (IOException ex){
            System.out.println(ex);
        }
    }
}