import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class fileManagerClient {

    private static Socket clientSocket = null;

    private static PrintWriter out = null;
    private static BufferedReader in = null;

    public static void main(String[] args) throws IOException, URISyntaxException {


        while(true)
        {

            String ftp_request = "";
            String server_response = "";
            System.out.print("Please enter your command : ");
            Scanner sc = new Scanner(System.in);
            ftp_request = sc.nextLine();
            String url = "";


            if (ftp_request.length() == 0 ||  ftp_request.isEmpty() ) {
                System.out.println("Invalid Command detected, please try again");
                continue;
            }

            if((ftp_request.contains("post") && !ftp_request.contains("-d")))
            {
                System.out.println("POST url should be with inline data");
                continue;
            }

            List<String> ftp_requestlist;
            ftp_requestlist = Arrays.asList(ftp_request.split(" "));

            if(ftp_request.contains("post"))
            {
                url = ftp_requestlist.get(3);
            }
            else
            {
                url = ftp_requestlist.get(ftp_requestlist.size() - 1);
            }

            URI uri = new URI(url);

            String hostName = uri.getHost();
            clientSocket = new Socket(hostName, uri.getPort());

            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            //Send Request
            System.out.println("Sending file transfer request to Server");
            out.write(ftp_request + "\n");
            out.flush();

            clientSocket.setSoTimeout(1000);


            //Receive Response
            try {


                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                }

            }catch(SocketTimeoutException s){
                    clientSocket.close();
            }

            out.close();
            in.close();

            server_response = sb.toString();

            System.out.println("\nServer response : \n " + server_response);


        }


    }

}