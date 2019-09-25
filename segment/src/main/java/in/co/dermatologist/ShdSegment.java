package in.co.dermatologist;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import inra.ijpb.binary.BinaryImages;
import inra.ijpb.morphology.MinimaAndMaxima;
import inra.ijpb.watershed.Watershed;

import java.awt.image.BufferedImage;

public class ShdSegment {

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
