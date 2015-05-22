package com.LyndonFawcett.MiniArchitect.utils;

import java.nio.ByteBuffer;

import com.LyndonFawcett.MiniArchitect.Arena;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * 
 * @author Lyndon
 *
 * Factory method that generates screen shots by pulling from the OpenGLES buffer
 *
 */
public class ScreenshotFactory {

	
	
    private static int counter = 1;
    /*
     * Save screen shot to file location (named by count)
     */
    public static void saveScreenshot(){

        try{
            FileHandle fh;
            do{
                fh = new FileHandle(Gdx.files.getLocalStoragePath() + "screenshot" + counter++ + ".png");
            }while (fh.exists());
            Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
        }catch (Exception e){           
        }
    }
    /*
     * Get current image of screen and return into a pixel map
     */
    public static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);
    	Camera pCam=Arena.pCam;//camera to take screenshot from
        if (yDown) {
            // Flip the pixmap upside down
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        }

        return pixmap;
    }
}