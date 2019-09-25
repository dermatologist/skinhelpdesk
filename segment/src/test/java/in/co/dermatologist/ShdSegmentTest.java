package in.co.dermatologist;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class ShdSegmentTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void shouldSegmentAndPrintWithIj() throws IOException {
        String skinhelpdesk = System.getProperty("user.dir");
        String inpI = new String(Files.readAllBytes(Paths.get(skinhelpdesk + "/test_images/image.txt")));
        ShdSegment shdSegment = new ShdSegment(inpI);
        String outI = shdSegment.getOutputImageStr();
        BufferedWriter writer = new BufferedWriter(new FileWriter(skinhelpdesk + "/test_images/image_out.txt"));
        writer.write(outI);
        File outputfile = new File(skinhelpdesk + "/test_images/image_out.jpg");
        ImageIO.write(ShdUtils.decodeToImage(outI), "jpg", outputfile);

        writer.close();
    }

    @Test
    public void shouldSegmentAndPrintWithKmeans() throws IOException {
        String skinhelpdesk = System.getProperty("user.dir");
        String inpI = new String(Files.readAllBytes(Paths.get(skinhelpdesk + "/test_images/image.txt")));
        ShdKmeansSegment shdKmeansSegment = new ShdKmeansSegment(inpI);
        String outI = shdKmeansSegment.getOutputImageStr();
        BufferedWriter writer = new BufferedWriter(new FileWriter(skinhelpdesk + "/test_images/image_out_kmeans.txt"));
        writer.write(outI);
        File outputfile = new File(skinhelpdesk + "/test_images/image_out_kmeans.jpg");
        ImageIO.write(ShdUtils.decodeToImage(outI), "jpg", outputfile);

        writer.close();
    }
}
