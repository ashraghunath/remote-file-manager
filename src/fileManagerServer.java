import java.io.*;
import java.net.*;
import java.util.*;
public class fileManagerServer {

    private static ServerSocket serverSocket;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    private static int port = 8080;
    private static int statusCode = 200;
    static boolean isDebug = false;

    public static void main(String[] args) throws IOException, URISyntaxException {
        String request;
        List<String> requestList = new ArrayList<>();
        String directory = System.getProperty("user.dir");
        System.out.print("You can now start the server :\n");
        Scanner scan = new Scanner(System.in);
        request = scan.nextLine();
        if (request.isEmpty()) {
            System.out.println("Command unknown");
        }
        String[] requestArray = request.split(" ");

        requestList.addAll(Arrays.asList(requestArray));

        if (requestList.contains("-v")) {
            isDebug = true;
        }

        if (requestList.contains("-p")) {
            String portStr = requestList.get(requestList.indexOf("-p") + 1).trim();
            port = Integer.parseInt(portStr);
        }

        if (requestList.contains("-d")) {
            directory = requestList.get(requestList.indexOf("-d") + 1).trim();
            System.out.println("Working directory requested : " + directory + "\n");
        }

        serverSocket = new ServerSocket(port);
        if (isDebug)
            System.out.println("Server started on port : " + port);


        File currentFolder = new File(directory);

        currentFolder.mkdirs();

        System.out.println("\nWorking Directory : " + directory + "\n");

        while(true)
        {
            Socket socket = serverSocket.accept();
            if (isDebug)
                System.out.println("Server and Client successfully connected");

            try {
                out = new PrintWriter(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            request = in.readLine();
            String clientRequestType = request.substring(0,7);
            if(clientRequestType.contains("httpc"))
            {

                if(isDebug)
                    System.out.println("detected HTTPC command\n\n");

                String url = "";
                String response = "";
                String options = "";
                int cl = 0;
                boolean verbose = false;

                List<String> requestData = Arrays.asList(request.split(" "));

                url = request.substring(request.indexOf("http://"), request.length() - 1);

                if(url.contains(" "))
                {
                    url = url.split(" ")[0];
                }
                URI uri = new URI(url);
                String host = uri.getHost();
                if (request.contains("get"))
                {
                    options = request.substring(request.indexOf("get") + 4);
                }

                else if(request.contains("post"))
                {
                    options = request.substring(request.indexOf("post") + 5);
                }

                if(options.contains("-v"))
                    verbose = true;

                String[] datalist = options.split(" ");
                List<String> data = Arrays.asList(datalist);


                String body = "{\n";


                if (request.contains("get"))
                {

                    String query = uri.getRawQuery();


                    List<String> querylist = Arrays.asList(query.split("&"));


                    //Appending the query arguments to the body
                    body = body + "\t\"args\": {\n";

                    for (int i = 0 ; i < querylist.size() ; i++)
                    {
                        String t1 = querylist.get(i).split("=")[0];
                        String t2 = querylist.get(i).split("=")[1];

                        body = body + "\t\t\"" + t1 + "\": \"" + t2 + "\",\n";
                    }

                    body = body + "\t}, \n";


                    //Appending headers to the body
                    body = body + "\t\"headers\": {\n";
                    for (int i = 0; i < data.size(); i++)
                    {
                        if (data.get(i).equals("-h")) {

                            String t1 = data.get(i+1).split(":")[0];
                            String t2 = data.get(i+1).split(":")[1];
                            body = body + "\t\t\"" + t1 + "\": \"" + t2 + "\",\n";
                        }
                    }

                    body = body + "\t\t\"Connection\": \"close\",\n";
                    body = body + "\t\t\"Host\": \"" + host + "\"\n";
                    body = body + "\t},\n";
                }

                else if(request.contains("post"))
                {

                    boolean jsonFlag = false;
                    String inlineData = "";
                    body = body + "\t\"args\": {},\n";

                    //Appending the data to the body
                    body = body + "\t\"data\": \"";
                    if(options.contains("-d ")){

                        int index = data.indexOf("-d") + 1;

                        for (int i = index ; i < data.size() - 1 ; i++)
                            inlineData = inlineData + data.get(i);

                        inlineData = inlineData.substring(2, inlineData.length()-2);

                        body = body + inlineData + "\", \n";
                        cl = body.length();
                    }

                    body = body + "\t\"files\": {},\n";
                    body = body + "\t\"form\": {},\n";

                    //HEADERS
                    body = body + "\t\"headers\": {\n";
                    for (int i = 0; i < data.size(); i++)
                    {
                        if (data.get(i).equals("-h")) {

                            String t1 = data.get(i+1).split(":")[0];
                            String t2 = data.get(i+1).split(":")[1];

                            if(t2.contains("json"))
                                jsonFlag = true;

                            body = body + "\t\t\"" + t1 + "\": \"" + t2 + "\",\n";
                        }
                    }

                    body = body + "\t\t\"Connection\": \"close\",\n";
                    body = body + "\t\t\"Host\": \"" + host + "\"\n";
                    body = body + "\t\t\"Content-Length\": \"" + cl + "\"\n";
                    body = body + "\t},\n";


                    //JSON CONTENT
                    if(jsonFlag )
                    {
                        body = body + "\t\"json\": {\n";

                        List<String> jsonData = Arrays.asList(inlineData.split(","));

                        for (String s : jsonData)
                        {
                            body = body + "\t\t" + s + ",\n";
                        }
                        body = body + "\t},\n";

                    }

                }


                body = body + "\t\"origin\": \"" + InetAddress.getLocalHost().getHostAddress() + "\",\n";
                body = body + "\t\"url\": \"" + url + "\"\n";
                body = body + "}\n";

                response = body;

                String verboseBody = "";

                if(verbose)
                {
                    verboseBody = verboseBody + "HTTP/1.1 200 OK\n";
                    verboseBody = verboseBody + "Date: " + Calendar.getInstance().getTime() + "\n";
                    verboseBody = verboseBody + "Content-Type: application/json\n";
                    verboseBody = verboseBody + "Content-Length: "+ body.length() +"\n";
                    verboseBody = verboseBody + "Connection: close\n";
                    verboseBody = verboseBody + "Server: Localhost\n";
                    verboseBody = verboseBody + "Access-Control-Allow-Origin: *\n";
                    verboseBody = verboseBody + "Access-Control-Allow-Credentials: true\n";

                    response = verboseBody;
                    response = response + body;
                }

                if(isDebug)
                    System.out.println(response);
                out.write(response);
                out.flush();
                socket.close();

        }

            else if(clientRequestType.contains("httpfs"))
            {

                if(isDebug)
                    System.out.println("Detected httpfs command");
                String url = "";

                List<String> requestData = Arrays.asList(request.split(" "));

                if(request.contains("post"))
                {
                    url = requestData.get(3);
                }
                else
                {
                    url = requestData.get(requestData.size() - 1);
                }

                URI uri = new URI(url);

                String host = uri.getHost();


                String body = "{\n";
                body = body + "\t\"args\":";
                body = body + "{},\n";
                body = body + "\t\"headers\": {";


                body = body + "\n\t\t\"Connection\": \"close\",\n";
                body = body + "\t\t\"Host\": \"" + host + "\"\n";


                String requestType = requestData.get(1);
                String content_type = "";
                String disposition_type= "";

                if(requestType.equalsIgnoreCase("GET") && requestData.get(2).equals("/"))
                {

                    content_type = "application/json"; //iska kuch karna hai?
                    disposition_type = "inline";
                    body = body + "\t\t\"Content-Type\": \"" + content_type + "\"\n";
                    body = body + "\t\t\"Content-disposition\": \"" + disposition_type + "\"\n";
                    body = body + "\t},\n";
                    body = body + "\t\"files\": { ";
                    List<String> files = getFilesFromDirectory(currentFolder);

                    List<String> fileFilterList = new ArrayList<>();
                    fileFilterList.addAll(files);

                    for (int i = 0; i < fileFilterList.size() - 1; i++) {
                        body = body + files.get(i) + " , ";
                    }

                    if(fileFilterList.size()==0)
                        body = body + " },\n";
                    if(fileFilterList.size()!=0)
                        body = body + fileFilterList.get(fileFilterList.size() - 1) + " },\n";
                    statusCode = 200;

                }

                else if(requestType.equalsIgnoreCase("GET") && !requestData.get(2).equals("/"))
                {


                    String response = "";
                    String requestedFile = requestData.get(2).substring(1);
                    System.out.println("requested file" + requestedFile);
                    String type = requestedFile.split("\\.")[1];
                    System.out.println("type"+ type);
                    if (type.matches("jpeg|png|gif|tiff|svg+xml")){
                        content_type = "image/"+ type;
                        disposition_type = "attachment";
                    }
                    else if (type.matches("css|csv|html|plain|xml|txt")){
                        content_type = "text/"+ type;
                        disposition_type = "inline";
                    }
                    else if (type.matches("pdf|json|zip|ogg|EDIFACT|octet-stream")){
                        content_type = "application/"+ type;
                        disposition_type = "attachment";
                    }
                    else if (type.matches("mpeg|x-wav|mp3")){
                        content_type = "audio/"+ type;
                        disposition_type = "attachment";
                    }
                    else if (type.matches("mpeg|x-wav|mp3")){
                        content_type = "audio/"+ type;
                        disposition_type = "attachment";
                    }
                    else if (type.matches("mpeg|quicktime|mp4|webm")){
                        content_type = "video/"+ type;
                        disposition_type = "attachment";
                    }

                    List<String> files = getFilesFromDirectory(currentFolder);
                    body = body + "\t\t\"Content-Type\": \"" + content_type + "\"\n";
                    body = body + "\t\t\"Content-disposition\": \"" + disposition_type + "\"\n";
                    body = body + "\t},\n";

                    if (!files.contains(requestedFile)) {
                        statusCode = 404;
                    }
                    else {
                        File file = new File(directory + "/" + requestedFile);
                        response = fileManagerServer.readDataFromFile(file);
                        if(disposition_type.equals("attachment")){
                            System.out.println("creating an attachment....");
                            System.out.println("directory + \"/attach\"");
                            body= body + "\t\"location\" : " + file.getAbsolutePath()+"\n";
                        }
                        else {
                            body = body + "\t\"data\": \"" + response + "\",\n";
                            statusCode = 200;
                        }
                    }


                }
                else if(requestType.equalsIgnoreCase("POST"))
                {
                    String response = "";
                    String requestedFile = requestData.get(2).substring(1);
                    String data = "";
                    List<String> files = getFilesFromDirectory(currentFolder);

                    boolean flagOverwrite = true;

                    if(requestData.size()==11 && requestData.get(10).contains("false"))
                        flagOverwrite=false;

                    if (!files.contains(requestedFile))
                        statusCode = 202;
                    else if(flagOverwrite)
                        statusCode = 201;
                    else
                        statusCode = 301;


                    if(flagOverwrite) {
                    int index = requestData.indexOf("-d");

                    for(int i = index + 1 ; i < requestData.size() ; i++)
                    {
                        if(requestData.get(i).contains("overwrite"))
                            break;
                        data = data + requestData.get(i) + " ";
                    }

                        File file = new File(directory + "/" + requestedFile);
                        fileManagerServer.writeResponseToFile(file, data);
                    }

                }
                String responseMessage = "";
                if(statusCode == 200)
                {
                    responseMessage = "HTTP/1.1 200 OK";

                }
                else if(statusCode == 201)
                {
                    responseMessage = "HTTP/1.1 201 FILE OVER-WRITTEN";
                }
                else if(statusCode == 202)
                {
                    responseMessage = "HTTP/1.1 202 NEW FILE CREATED";
                }
                else if(statusCode == 301)
                {
                    responseMessage = "HTTP/1.1 301 NOT MODIFIED";
                }
                else if(statusCode == 404)
                {
                    responseMessage = "HTTP/1.1 404 FILE NOT FOUND";
                }
                body = body + "\t\"status\": \"" + responseMessage + "\",\n";


                body = body + "\t\"origin\": \"" + InetAddress.getLocalHost().getHostAddress() + "\",\n";
                body = body + "\t\"url\": \"" + url + "\"\n";
                body = body + "}\n";

                String header = "";
                header = header + responseMessage+"\n";
                header = header + "Date: " + Calendar.getInstance().getTime() + "\n";
                header = header + "Content-Type: application/json\n";
                header = header + "Content-Length: "+ body.length() +"\n";
                header = header + "Connection: close\n";
                header = header + "Server: Localhost\n";
                header = header + "Access-Control-Allow-Origin: *\n";
                header = header + "Access-Control-Allow-Credentials: true\n";

                body = header + body;

                if(isDebug)
                    System.out.println(body);
                out.write(body);
                out.flush();
            }

        }

    }

    static public void writeResponseToFile(File filename, String data)
    {
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));

            bufferedWriter.write(data);
            bufferedWriter.close();

            if(isDebug)
                System.out.println("Server response written to : " + filename);

        } catch (IOException ex) {
            if(isDebug)
                System.out.println("Error Writing to file '" + filename + "'" + ex);
        }
    }

    static public String readDataFromFile(File filename)
    {
        StringBuilder lines = new StringBuilder("");
        String line = null;

        try
        {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            while((line = bufferedReader.readLine()) != null)
            {
                lines.append(line);

            }
            bufferedReader.close();
        }
        catch(IOException ex)
        {
            if(isDebug)
                System.out.println("Error reading file '" + filename + "'" + ex);
        }

        return lines.toString();
    }

    /**
     * This method will give list of files from specific directory
     *
     * @return List of files
     */
    static private List<String> getFilesFromDirectory(File currentDir) {
        List<String> filelist = new ArrayList<>();
        for (File file : currentDir.listFiles()) {
            if (!file.isDirectory()) {
                filelist.add(file.getName());
            }
        }
        return filelist;
    }
}
