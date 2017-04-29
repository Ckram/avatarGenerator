import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Moi on 29/04/2017.
 */

public class Generator {

    /**
     *
     * @param seed the seed to generate the image
     * @param height number of pixel in height of the random image (max 16, min 1)
     * @param width number of pixel in width of the random image (max 16, min 1)
     * @param magnificationRatio ratio how much should the image be magnified
     * @return The image as a BufferedImage
     */
    public static BufferedImage generateRandomImage(String seed, int height, int width, int magnificationRatio){

        //Hash the password with MD5
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        byte[] hash = messageDigest.digest(seed.getBytes());

        BufferedImage identicon = new BufferedImage(width * magnificationRatio, height * magnificationRatio, BufferedImage.TYPE_INT_ARGB);


        int r = hash[0] & 255;
        int g = hash[1] & 255;
        int b = hash[2] & 255;


        //Put the background in white
        int[] background = new int[]{255, 255, 255};
        //Randomize the foreground with the hash of the name
        int[] foreground = new int[]{r, g, b};


        for (int x = 0; x < width; x++) {

            //For the symmetry
            int i = x < (width / 2) ? x : width - 1 - x;
            for (int y = 0; y < height; y++) {
                int[] pixelColor;

                if ((hash[i % 16] >> y - i & 1) == 1)
                    pixelColor = foreground;
                else
                    pixelColor = background;
                for (int xx = 0; xx < magnificationRatio; xx++) {
                    for (int yy = 0; yy < magnificationRatio; yy++) {
                        identicon.setRGB(x * magnificationRatio + xx, y * magnificationRatio + yy, (255 << 24) | (pixelColor[0] << 16) | (pixelColor[1] << 8) | pixelColor[2]);
                    }
                }

            }
        }



        return identicon;
    }

    public static void main(String[] args) {


        try {
            BufferedImage bufferedImage = generateRandomImage("randomSeed",8,8,10);
            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",new File("testImage.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
