package in.co.dermatologist;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import inra.ijpb.binary.BinaryImages;
import inra.ijpb.morphology.MinimaAndMaxima;
import inra.ijpb.watershed.Watershed;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayRandomAccess;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imagej.notebook.Images;

import net.imglib2.img.display.imagej.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShdSegment {

    private static Object imageObject;
    private int radius = 2;
    private int tolerance = 3;
    private int conn = 4;
    private Boolean dams = true;
    private ImagePlus imp = null;
    private BufferedImage image = null;
    private BufferedImage retImage = null;
    private String inputImageStr = "";
    private String outputImageStr = "";

    public ShdSegment(String inputImageStr) {
        this.inputImageStr = inputImageStr;
        segmentImage();
    }

    public void segmentImage() {
        if(imp == null && image == null){
            image = ShdUtils.decodeToImage(inputImageStr);
        }
        if(imp == null)
            imp = new ImagePlus("Title", image);

        ImageProcessor imageStack = imp.getProcessor();

        ImageProcessor regionalMinima = MinimaAndMaxima.extendedMinima(imageStack, tolerance, conn);
        ImageProcessor imposedMinima = MinimaAndMaxima.imposeMinima(imageStack, regionalMinima, conn);
        ImageProcessor labeledMinima = BinaryImages.componentsLabeling(regionalMinima, conn, 32);
        ImageProcessor resultStack = Watershed.computeWatershed(imposedMinima, labeledMinima, conn, dams);
        this.retImage = resultStack.getBufferedImage();
        this.outputImageStr = ShdUtils.encodeToString(this.retImage, "jpeg");
    }

    public static void convertToGreyScale() throws IOException {
        ImageJ ij = new ImageJ();
        Object clown = ij.io().open("https://imagej.net/images/clown.png");
        Double[] scaleFactors = {0.5, 0.5, 1.0};
        NLinearInterpolatorFactory interpolationStrategy = new NLinearInterpolatorFactory();
        imageObject = ij.op().run("scaleView", clown, scaleFactors, interpolationStrategy);
//        String skinhelpdesk = System.getProperty("user.dir");
//        ij.ui().show(imageObject);
//        ij.io().save(imageObject, skinhelpdesk + "/test_images/image_clown.png");

        ArrayImg image32 = (ArrayImg) ij.op().run("create.img", imageObject);

        ImageJFunctions imageJFunctions = new ImageJFunctions();

        ImagePlus imagePlus = imageJFunctions.wrap(image32, "Title");

        ImageProcessor imageProcessor = imagePlus.getProcessor();
        BufferedImage bufferedImage = imageProcessor.getBufferedImage();
        //BufferedImage image1 = Images.bufferedImage((Dataset) image32.randomAccess());
        //System.out.println(ShdUtils.encodeToString(image1, "png"));
        System.out.println(ShdUtils.encodeToString(bufferedImage, "png"));
        System.out.println(Images.base64(bufferedImage));
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTolerance() {
        return tolerance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    public int getConn() {
        return conn;
    }

    public void setConn(int conn) {
        this.conn = conn;
    }

    public Boolean getDams() {
        return dams;
    }

    public void setDams(Boolean dams) {
        this.dams = dams;
    }

    public ImagePlus getImp() {
        return imp;
    }

    public void setImp(ImagePlus imp) {
        this.imp = imp;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getImageStr() {
        return inputImageStr;
    }

    public void setImageStr(String imageStr) {
        this.inputImageStr = imageStr;
        segmentImage();
    }

    public String getOutputImageStr() {
        return outputImageStr;
    }

    public BufferedImage getRetImage() {
        return retImage;
    }
}
