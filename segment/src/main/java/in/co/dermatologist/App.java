package in.co.dermatologist;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        String skinhelpdesk = System.getProperty("user.dir");
        String inpI = new String(Files.readAllBytes(Paths.get(skinhelpdesk + "/test_images/image.txt")));
        ShdSegment shdSegment = new ShdSegment(inpI);
        shdSegment.segmentImage();
        String outI = shdSegment.getOutputImageStr();
//        ShdKmeansSegment shdKmeansSegment = new ShdKmeansSegment(inpI);
//        String outI = shdKmeansSegment.getInputImageStr();
//        System.out.println( outI );
        BufferedWriter writer = new BufferedWriter(new FileWriter(skinhelpdesk + "/test_images/image_out.txt"));
        writer.write(outI);
        File outputfile = new File(skinhelpdesk + "image_out.jpg");
        ImageIO.write(ShdUtils.decodeToImage(outI), "jpg", outputfile);

        writer.close();
    }
}
