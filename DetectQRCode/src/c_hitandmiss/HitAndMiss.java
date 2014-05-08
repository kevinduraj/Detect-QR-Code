package c_hitandmiss;

public class HitAndMiss {

    private int[][] image = null;
    public int[][] outImage = null;

    int[][] topLeftPoint = null;
    int[][] topRightPoint = null;
    int[][] botLeftPoint = null;
    int[][] botRightPoint = null;

    private int height;
    private int width;

    /*-------------------------------------------------------------------------*/
    public HitAndMiss(int[][] image) {

        height = image.length;
        width = image[0].length;
        this.image = image;
        outImage = new int[height][width];

        topLeftPoint = new int[10000][2];
        topRightPoint = new int[10000][2];
        botLeftPoint = new int[10000][2];
        botRightPoint = new int[10000][2];

    }
    /*-------------------------------------------------------------------------*/

    public int[][] convolve(int[][] structure, String structName) {

        int halfH = structure.length / 2;
        int halfW = structure[0].length / 2;
        int match = 0;

        int topLeftRegion = 0;
        int topRightRegion = 0;
        int bottomLeftRegion = 0;
        int bottomRightRegion = 0;

        /*-------------------------- IMAGE ------------------------------*/
        for (int j = halfH; j < height - halfH; j++) {
            for (int i = halfW; i < width - halfW; i++) {

                match = 0;
                /*-------------------- KERNEL --------------------*/
                for (int jj = -halfH; jj <= halfH; jj++) {
                    for (int ii = -halfW; ii <= halfW; ii++) {

                        if ((structure[ii + halfH][jj + halfW] == 1)
                                && image[j - ii][i - jj] > 0) {
                            match++;

                        } else if ((structure[ii + halfH][jj + halfW] == 0)
                                && image[j - ii][i - jj] == 0) {
                            match++;

                        } else if (structure[ii + halfH][jj + halfW] == -1) {
                            match++;
                        }
                    }
                }
                if (match == 9) {

                    if (structName.equals("topLeft")) {

                        if (topLeftRegion < topLeftPoint.length) {
                            System.out.format("Reg=%1d %3d %3d\t", topLeftRegion, j, i);
                            System.out.println("topLeftPoint");
                            topLeftPoint[topLeftRegion][0] = j;
                            topLeftPoint[topLeftRegion][1] = i;

                            outImage[j][i] = 255;
                            topLeftRegion++;
                        }

                    } else if (structName.equals("topRight")) {
                        if (topRightRegion < topRightPoint.length) {
                            System.out.format("Reg=%1d %3d %3d\t", topRightRegion, j, i);
                            System.out.println("topRightPoint");
                            topRightPoint[topRightRegion][0] = j;
                            topRightPoint[topRightRegion][1] = i;

                            outImage[j][i] = 255;
                            topRightRegion++;
                        }
                    } else if (structName.equals("bottomLeft")) {
                        if (bottomLeftRegion < botLeftPoint.length) {
                        System.out.format("Reg=%1d %3d %3d\t", bottomLeftRegion, j, i);
                        System.out.println("botLeftPoint");
                        botLeftPoint[bottomLeftRegion][0] = j;
                        botLeftPoint[bottomLeftRegion][1] = i;

                        outImage[j][i] = 255;
                        bottomLeftRegion++;
                        }
                    } else if (structName.equals("bottomRight")) {
                        if (bottomRightRegion < botRightPoint.length) {
                        System.out.format("Reg=%1d %3d %3d\t", bottomRightRegion, j, i);
                        System.out.println("botRightPoint");
                        botRightPoint[bottomRightRegion][0] = j;
                        botRightPoint[bottomRightRegion][1] = i;

                        outImage[j][i] = 255;
                        bottomRightRegion++;
                        }
                    }

                }
                /*----------------- ENF OF KERNEL -----------------*/
            }
        }
        /*------------------------ END OF IMAGE ------------------------------*/
        return outImage;
    }
    /*-------------------------------------------------------------------------*/

    public String qrcodeDetection() {

        int[] check = new int[4];

        if (topLeftPoint.length > 2) {
            check[0] = topRightPoint[0][1] - topLeftPoint[0][0];
            check[1] = botRightPoint[0][1] - botLeftPoint[0][1];
            check[2] = topRightPoint[0][1] - topLeftPoint[0][1];
            check[3] = botRightPoint[1][1] - botLeftPoint[1][1];

            int result = check[0];

            for (int i : check) {
                if (result != i || i == 0) {
                    return "NOT a QR Code";
                }
            }

            if (botRightPoint[0][0] == botRightPoint[1][0]) {
                return "QR Code Deteted";
            }
        } else {
            return "NOT a QR Code";
        }

        return "NOT a QR Code";

    }
    /*-------------------------------------------------------------------------*/
}
