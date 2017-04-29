import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Moi on 30/04/2017.
 */
public class CharacterGenerator {


    public static BufferedImage generateRandomCharacterFace(String seed){

        //Hash the password with MD5
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        int height =10;
        int width = 8;


        byte[] hash = messageDigest.digest(seed.getBytes());

        BufferedImage characterFace = new BufferedImage(width, height ,BufferedImage.TYPE_INT_ARGB);


        int r = hash[0] & 255;
        int g = hash[1] & 255;
        int b = hash[2] & 255;


        //Put the background in white
        int[] background = new int[]{255-r, 255-g, 255-b,126};
        //Randomize the foreground with the hash of the name
        int[] foreground = new int[]{r, g, b,255};

        for (int x = 0; x<width;x++){
            int i = x < (width / 2) ? x : width - 1 - x;
            for (int y=0; y< height;y++){

                //For the hair asymetric
                if(y<2){
                    int[] pixelColor;
                    if ((hash[(x +y)%16] >> y & 1) == 1)
                        pixelColor = foreground;
                    else
                        pixelColor = background;
                    characterFace.setRGB(x,y,(pixelColor[3] << 24) | (pixelColor[0] << 16) | (pixelColor[1] << 8) | pixelColor[2]);
                }
                //For the eyes
                else if(y==3 && (x==2 || x==5)){
                    characterFace.setRGB(x,y,(foreground[3] << 24) | (foreground[0] << 16) | (foreground[1] << 8) | foreground[2]);
                }
                //For the nose
                else if(y==5&& (x==3 || x==4)){
                    characterFace.setRGB(x,y,(foreground[3] << 24) | (foreground[0] << 16) | (foreground[1] << 8) | foreground[2]);
                }
                //For the mouth
                else if((y==7 || y == 8) && (x!=0 && x!=7)){
                    //Main and fix mouth
                    if((x==3 || x==4))
                        characterFace.setRGB(x,y,(foreground[3] << 24) | (foreground[0] << 16) | (foreground[1] << 8) | foreground[2]);
                    //Extra mouth
                    else {
                        int[] pixelColor;
                        if ((hash[i % 16] >> y - i & 1) == 1)
                            pixelColor = foreground;
                        else
                            pixelColor = background;
                        characterFace.setRGB(x,y,(pixelColor[3] << 24) | (pixelColor[0] << 16) | (pixelColor[1] << 8) | pixelColor[2]);
                    }
                }
                else {
                    characterFace.setRGB(x,y,(background[3] << 24) | (background[0] << 16) | (background[1] << 8) | background[2]);
                }
            }
        }

        BufferedImage resized = new BufferedImage(width*20, height*20, characterFace.getType());
        Graphics2D gr = resized.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
        gr.drawImage(characterFace, 0, 0, width*20, height*20, 0, 0, characterFace.getWidth(),
                characterFace.getHeight(), null);
        gr.dispose();



        return resized;

    }
}
