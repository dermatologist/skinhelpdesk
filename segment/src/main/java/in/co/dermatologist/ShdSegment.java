package in.co.dermatologist;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;

import inra.ijpb.binary.BinaryImages;
import inra.ijpb.data.image.Images3D;
import inra.ijpb.morphology.MinimaAndMaxima3D;
import inra.ijpb.morphology.Morphology;
import inra.ijpb.morphology.Strel3D;
import inra.ijpb.watershed.Watershed;

public class ShdSegment {

    private int radius = 2;
    private int tolerance = 3;
    private String strConn = "26";
    private Boolean dams = true;
    private ImagePlus imp = null;
    private BufferedImage image = null;

    public BufferedImage segmentImage() {
        if(imp == null)
            imp = new ImagePlus("Title", image);

        // convert connectivity string to int

        int conn = Integer.parseInt(strConn);


        ImageStack imageStack = null;

        if (radius != 0) {

            Strel3D strel = Strel3D.Shape.CUBE.fromRadius(radius);


            imageStack = Morphology.gradient(imp.getImageStack(), strel);

        } else {
            imageStack = imp.getImageStack();
        }

        ImageStack regionalMinima = MinimaAndMaxima3D.extendedMinima(imageStack, tolerance, conn);
        ImageStack imposedMinima = MinimaAndMaxima3D.imposeMinima(imageStack, regionalMinima, conn);
        ImageStack labeledMinima = BinaryImages.componentsLabeling(regionalMinima, conn, 32);
        ImageStack resultStack = Watershed.computeWatershed(imposedMinima, labeledMinima, conn, dams);
        ImagePlus resultImage = new ImagePlus("watershed", resultStack);
        resultImage.setCalibration(imp.getCalibration());
        Images3D.optimizeDisplayRange(resultImage);
        resultImage.setSlice(imp.getCurrentSlice());
        ImageProcessor imgProcessor = resultImage.getProcessor();
        BufferedImage retImage = imgProcessor.getBufferedImage();
        return retImage;
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
}
