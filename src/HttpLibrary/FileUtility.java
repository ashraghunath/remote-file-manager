package HttpLibrary;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Class to write the output to file
 */
public class FileUtility {

    public static void writeOutputToFile(StringBuilder output,String fileName) {
        try {
            String currentDir = System.getProperty("user.dir");
            String filePath = currentDir + "/" + fileName;
            FileWriter fileWriter = new FileWriter(filePath, false);
            fileWriter.write(output.toString());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("Data written to File Successfully");
    }

}
