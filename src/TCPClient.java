import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;


public class TCPClient {
    static int girdi = 0, dataOutTime = 0, dataInTime = 0;
    private static void getMenu(){
        System.out.println( "*************************\n" +
                            "***********Menu**********\n" +
                            "1. Toplama Islemi\n" +
                            "2. Faktoriyel Islemi\n" +
                            "3. Cikis\n" +
                            "*************************\n" +
                            "Seciminizi yapiniz...");
        System.out.print(">>");
        Scanner secim = new Scanner(System.in);
        girdi = secim.nextInt();
        if(girdi == 1){
            System.out.print("Toplamak istediginiz sayiyi giriniz : ");
            Scanner toplam = new Scanner(System.in);
            girdi = toplam.nextInt();
        } else if (girdi == 2){
            System.out.print("Faktoriyelini almak istediginiz sayiyi giriniz : ");
            Scanner faktoriyel = new Scanner(System.in);
            girdi = faktoriyel.nextInt();
        }
    }

    public static void DosyaIslemleri(){
        // TODO Dosya İşlemleri
    }

    public static void main(String[] args) throws Exception {
        InetAddress IP = InetAddress.getLocalHost();
        Socket socket               =   new Socket("127.0.0.1",4027);
        DataInputStream inStream    =   new DataInputStream(socket.getInputStream());
        DataOutputStream outStream  =   new DataOutputStream(socket.getOutputStream());
        BufferedReader br           =   new BufferedReader(new InputStreamReader(System.in));
        try{
            String clientMessage        =   "";
            String serverMessage        =   "";

            while(true){

                getMenu();
                String paket = "Ip Adress:/" + IP.getHostAddress() + "/Date & Time:/" + (new Date().toString());
                if(girdi == 1)
                    paket += "/Islem Turu:/" + "tp/" + girdi;
                else if(girdi == 2)
                    paket += "/Islem Turu:/" + "fak/" + girdi;
                else if (girdi == 3)
                    paket += "/Islem Turu:/" + "cikis/" + girdi;
                outStream.writeUTF(paket);
                dataOutTime = (int) System.nanoTime();
                outStream.flush();
                serverMessage   =   inStream.readUTF();
                if (serverMessage.equals("Client was disconnected"))
                    break;
                System.out.println(serverMessage);
                dataInTime = (int) System.nanoTime();
                DosyaIslemleri();
            }
            inStream.close();
            outStream.close();
            socket.close();

        }catch(Exception e){
            System.out.println(e);
        }
    }
}