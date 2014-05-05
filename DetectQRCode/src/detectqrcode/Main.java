package detectqrcode;

import a_otsu_bin.OtsuBinarize;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {

    private static final String filename = "qrcode4";
    
    /*--------------------------------------------------------------------------------------------*/
    public static void main(String[] args) throws IOException {

        Otsu();
        
    }
    /*--------------------------------------------------------------------------------------------*/
    private static void Otsu() throws IOException {
               
        OtsuBinarize otsu = new OtsuBinarize("src/images/" + filename + ".png");
        otsu.run(); 
        File file = new File("src/output1/" + filename + ".png");
        ImageIO.write(otsu.binarized, "png", file);
    }  
    /*--------------------------------------------------------------------------------------------*/
}
