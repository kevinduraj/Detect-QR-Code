package detectqrcode;

import a_otsu_bin.OtsuBinarize;
import b_components.*;
import c_hitandmiss.HitAndMiss;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {

    private static final String filename = "qrcode4";
    
    public static final int[][] bottomLeft = {
        { -1, 0,  0 },
        {  1, 1,  0 },
        { -1, 1, -1 }
    };
    
     public static final int[][] topLeft = {
        { -1, 1, -1 },
        {  1, 1,  0 },
        { -1, 0,  0 }
    };
     
    public static final int[][] bottomRight = {
        { 0,  0, -1 },
        { 0,  1,  1 },
        { -1, 1, -1 }
    };

    public static final int[][] topRight = {
        { -1, 1, -1 },
        { 0,  1,  1 },
        { 0,  0, -1 }
    };    
    /*--------------------------------------------------------------------------------------------*/
    public static void main(String[] args) throws IOException {

        Otsu();
        Region();
        ProcesHit2Miss();
    }
    /*--------------------------------------------------------------------------------------------*/
    private static void Otsu() throws IOException {
               
        OtsuBinarize otsu = new OtsuBinarize("src/images/" + filename + ".png");
        otsu.run(); 
        File file = new File("src/output1/" + filename + ".png");
        ImageIO.write(otsu.binarized, "png", file);
    }  
    
    /*--------------------------------------------------------------------------------------------*/
    public static void Region() throws IOException {

        int[][] image = ImageRead("src/output1/" + filename + ".png");
        ConnectedComponent comp = new ConnectedComponent();
        int region = comp.Apply(image, image.length, image[0].length, 0);
        System.out.println("Region: " +  region);
        ImageWrite("src/output2/" + filename + ".png", image);     
        
        int[][] max = ImageReadMax("src/output2/" + filename + ".png");
        ImageWrite("src/output3/" + filename + ".png", max);    

    }
    /*--------------------------------------------------------------------------------------------*/

    public static void ProcesHit2Miss() throws IOException {
       
        int[][] image = ImageRead("src/output3/" + filename + ".png");
        HitAndMiss hit = new HitAndMiss(image);

        hit.convolve( topLeft, "topLeft");
        hit.convolve( topRight, "topRight");
        hit.convolve( bottomLeft, "bottomLeft");       
        hit.convolve( bottomRight, "bottomRight");
        
        System.out.println(hit.qrcodeDetection());
        ImageWrite("src/output4/" + filename  + ".png", hit.outImage);
    }  
    /*--------------------------------------------------------------------------------------------*/
    private static int[][] ImageRead(String filename) throws IOException {

        File infile = new File(filename);
        BufferedImage bi = ImageIO.read(infile);

        int red[][] = new int[bi.getHeight()][bi.getWidth()];
        int grn[][] = new int[bi.getHeight()][bi.getWidth()];
        int blu[][] = new int[bi.getHeight()][bi.getWidth()];

        for (int i = 0; i < red.length; ++i) {
            for (int j = 0; j < red[i].length; ++j) {
                red[i][j] = bi.getRGB(j, i) >> 16 & 0xFF;
                grn[i][j] = bi.getRGB(j, i) >> 8 & 0xFF;
                blu[i][j] = bi.getRGB(j, i) & 0xFF;
            }
        }
        return grn;
    }
    /*--------------------------------------------------------------------------------------------*/

    private static int[][] ImageReadMax(String filename) throws IOException {

        File infile = new File(filename);
        BufferedImage bi = ImageIO.read(infile);

        int red[][] = new int[bi.getHeight()][bi.getWidth()];
        int grn[][] = new int[bi.getHeight()][bi.getWidth()];
        int blu[][] = new int[bi.getHeight()][bi.getWidth()];

        for (int i = 0; i < red.length; ++i) {
            for (int j = 0; j < red[i].length; ++j) {
                red[i][j] = bi.getRGB(j, i) >> 16 & 0xFF;
                grn[i][j] = bi.getRGB(j, i) >> 8 & 0xFF;
                
                if((bi.getRGB(j, i) >> 8 & 0xFF) > 1) { 
                    grn[i][j] = 255; //257-(bi.getRGB(j, i) >> 8 & 0xFF);
                }
                else {
                    grn[i][j] = 0;
                }
                
                blu[i][j] = bi.getRGB(j, i) & 0xFF;
            }
        }
        return grn;
    }    
    /*--------------------------------------------------------------------------------------------*/

    private static void ImageWrite(String filename, int img[][]) throws IOException {

        BufferedImage bi = new BufferedImage(img[0].length, img.length, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < bi.getHeight(); ++i) {
            for (int j = 0; j < bi.getWidth(); ++j) {
                int val = img[i][j];
                int pixel = (val << 16) | (val << 8) | (val);
                bi.setRGB(j, i, pixel);
            }
        }

        File outputfile = new File(filename);
        ImageIO.write(bi, "png", outputfile);
    }
    /*--------------------------------------------------------------------------------------------*/
    
}
