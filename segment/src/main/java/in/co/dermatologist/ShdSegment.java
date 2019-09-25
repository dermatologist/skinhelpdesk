package in.co.dermatologist;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;

import inra.ijpb.binary.BinaryImages;
import inra.ijpb.data.image.ByteStackWrapper;
import inra.ijpb.data.image.Image3D;
import inra.ijpb.data.image.Images3D;
import inra.ijpb.morphology.*;
import inra.ijpb.watershed.Watershed;

public class ShdSegment {

    private int radius = 2;
    private int tolerance = 3;
    private String strConn = "4";
    private Boolean dams = true;
    private ImagePlus imp = null;
    private BufferedImage image = null;
    private BufferedImage retImage = null;
    private String inputImageStr = "";
    private String outputImageStr = "";

    ShdSegment(String inputImageStr){
        this.inputImageStr = inputImageStr;
    }

    public void segmentImage() {
        if(imp == null && image == null){
            image = ShdUtils.decodeToImage(inputImageStr);
        }
        if(imp == null)
            imp = new ImagePlus("Title", image);

        // convert connectivity string to int

        int conn = Integer.parseInt(strConn);


//        ImageStack imageStack = null;
//
//        if (radius != 0) {
//
//            Strel strel = Strel.Shape.SQUARE.fromRadius(radius);
//
//
//            imageStack = Morphology.gradient(imp.getImageStack(), strel);
//
//        } else {
//            imageStack = imp.getImageStack();
//        }

        ImageProcessor imageStack = imp.getProcessor();

        ImageProcessor regionalMinima = MinimaAndMaxima.extendedMinima(imageStack, tolerance, conn);
        ImageProcessor imposedMinima = MinimaAndMaxima.imposeMinima(imageStack, regionalMinima, conn);
        ImageProcessor labeledMinima = BinaryImages.componentsLabeling(regionalMinima, conn, 32);
        ImageProcessor resultStack = Watershed.computeWatershed(imposedMinima, labeledMinima, conn, dams);
        //ImagePlus resultImage = new ImagePlus("watershed", resultStack);
//        resultImage.setCalibration(imp.getCalibration());
//        Images3D.optimizeDisplayRange(resultImage);
//        resultImage.setSlice(imp.getCurrentSlice());
        //ImageProcessor imgProcessor = resultImage.getProcessor();
        //System.out.println(resultStack.getWidth());
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

    public String getStrConn() {
        return strConn;
    }

    public void setStrConn(String strConn) {
        this.strConn = strConn;
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
    }

    public String getOutputImageStr() {
        return outputImageStr;
    }

    public BufferedImage getRetImage() {
        return retImage;
    }
}
