package HttpLibrary;

/**
 * Class to describe the help option form the user input
 */

public class HELP {
    /**
     * Function to execute the help request for GET and POST
     * @param argumentTokens
     */

    public void run(String[] argumentTokens) {
        if (argumentTokens[1].equals("help")) {
            if (argumentTokens.length == 2)
                displayHelp();
            else {
                if (argumentTokens[2].equals("get") && argumentTokens.length == 3) {
                    displayGET();
                } else if (argumentTokens[2].equals("post") && argumentTokens.length == 3) {
                    displayPOST();
                } else {
                    System.out.println("Invalid arguments Please run again with valid arguments");
                }
            }
        }
    }

    /**
     * Display general help output for the request of form "httpc help"
     */

    private void displayHelp() {
        System.out.println("httpc is a curl-like application but supports HTTP protocol only. \n" +
                "Usage: \n " +
                "\t httpc command [arguments] \n " +
                "The commands are: \n" +
                "\t get\texecutes a HTTP GET request and prints the response. \n" +
                "\t post\texecutes a HTTP POST request and prints the response. \n " +
                "\t help\tprints this screen.\n\n" +
                "Use \"httpc help [command]\" for more information about a command.");
    }

    /**
     * Display information for GET request, i.e., "httpc get help"
     */

    private void displayGET() {
        System.out.println("usage: httpc get [-v] [-h key:value] URL \n\n" +
                "Get executes a HTTP GET request for a given URL.\n\n " +
                "\t-v\t\t\t\tPrints the detail of the response such as protocol, status,\n" +
                "and headers.\n" +
                "\t-h key:value\tAssociates headers to HTTP Request with the format\n" +
                "'key:value'.");

    }

    /**
     *  Display information for POST request, i.e., "httpc post help"
     */
    private void displayPOST() {
        System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL \n\n" +
                "Post executes a HTTP POST request for a given URL with inline data or from\n" +
                "file.\n\n " +
                "-v\t\t\t\tPrints the detail of the response such as protocol, status,\n" +
                "and headers.\n" +
                "-h key:value\tAssociates headers to HTTP Request with the format\n" +
                "'key:value'.\n" +
                "-d string\t\tAssociates an inline data to the body HTTP POST request.\n" +
                "-f file\t\t\tAssociates the content of a file to the body HTTP POST\n" +
                "request.\n\n" +
                "Either [-d] or [-f] can be used but not both."
        );

    }

}
