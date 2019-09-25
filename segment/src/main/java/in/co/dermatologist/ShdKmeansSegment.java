package in.co.dermatologist;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/*
From https://github.com/nikhitareddy/Image-Segmentation
Thank you Nikita

 */
public class ShdKmeansSegment {

    BufferedImage original;
    BufferedImage result;
    Cluster[] clusters;
    public static final int MODE_CONTINUOUS = 1;
    public static final int MODE_ITERATIVE = 2;
    int mode = 2;
    int k = 3;
    private String inputImageStr = "";
    private String outputImageStr = "";


    ShdKmeansSegment(String inputImageStr) throws IOException {
        this.inputImageStr = inputImageStr;
        original = ShdUtils.decodeToImage(inputImageStr);
        calculate();
    }

    public String getInputImageStr() {
        return inputImageStr;
    }

    public void setInputImageStr(String inputImageStr) {
        this.inputImageStr = inputImageStr;
    }

    public String getOutputImageStr() {
        return outputImageStr;
    }


    public BufferedImage getOriginal() {
        return original;
    }

    public void setOriginal(BufferedImage original) {
        this.original = original;
    }

    public BufferedImage getResult() {
        return result;
    }

    public void setResult(BufferedImage result) {
        this.result = result;
    }

    public Cluster[] getClusters() {
        return clusters;
    }

    public void setClusters(Cluster[] clusters) {
        this.clusters = clusters;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void calculate() throws IOException {
        long start = System.currentTimeMillis();
        int w = original.getWidth();
        int h = original.getHeight();
        // create clusters
        clusters = createClusters(original, k);
        // create cluster lookup table
        int[] lut = new int[w * h];
        Arrays.fill(lut, -1);
        // at first loop all pixels will move their clusters

        boolean pixelChangedCluster = true;

// loop until all clusters are stable!
        int loops = 0;
        while (pixelChangedCluster) {
            pixelChangedCluster = false;
            loops++;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = original.getRGB(x, y);
                    Cluster cluster = findMinimalCluster(pixel);
                    if (lut[w * y + x] != cluster.getId()) {
                        // cluster changed
                        if (mode == MODE_CONTINUOUS) {
                            if (lut[w * y + x] != -1) {
                                // remove from possible previous
                                // cluster
                                clusters[lut[w * y + x]].removePixel(
                                        pixel);
                            }
                            // add pixel to cluster
                            cluster.addPixel(pixel);
                        }
                        // continue looping
                        pixelChangedCluster = true;

                        // update lut
                        lut[w * y + x] = cluster.getId();

                    }
                }

            }
            if (mode == MODE_ITERATIVE) {
                // update clusters
                for (int i = 0; i < clusters.length; i++) {
                    clusters[i].clear();
                }
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        int clusterId = lut[w * y + x];
                        // add pixels to cluster
                        clusters[clusterId].addPixel(
                                original.getRGB(x, y));
                    }
                }
            }

        }
        // create result image
        result = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int clusterId = lut[w * y + x];
                result.setRGB(x, y, clusters[clusterId].getRGB());
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("Clustered to " + k + " clusters in " + loops
                + " loops in " + (end - start) + " ms.");
        outputImageStr = ShdUtils.encodeToString(result, "jpeg");
//        File outputfile = new File("image_out.jpg");
//        ImageIO.write(result, "jpg", outputfile);
        //return result;
    }

    public Cluster[] createClusters(BufferedImage image, int k) {
        // Here the clusters are taken with specific steps,
        // so the result looks always same with same image.
        // You can randomize the cluster centers, if you like.
        Cluster[] result = new Cluster[k];
        int x = 0;
        int y = 0;
        int dx = image.getWidth() / k;
        int dy = image.getHeight() / k;
        for (int i = 0; i < k; i++) {
            result[i] = new Cluster(i, image.getRGB(x, y));
            x += dx;
            y += dy;
        }
        return result;
    }

    public Cluster findMinimalCluster(int rgb) {
        Cluster cluster = null;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) {
            int distance = clusters[i].distance(rgb);
            if (distance < min) {

                min = distance;
                cluster = clusters[i];
            }
        }
        return cluster;
    }
}
