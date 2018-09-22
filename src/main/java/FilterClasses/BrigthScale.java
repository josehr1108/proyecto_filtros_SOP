package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class BrigthScale {
	private File inputFile;
	private File outputFile;
	private int factor;
	private long elapsedMs;

	public BrigthScale(File inputFile,int factor,String opMode){
		this.inputFile = inputFile;
		this.factor = factor;
		sequentialProcess();
	}

	private void sequentialProcess(){
		long startTime = System.currentTimeMillis();
		try {
			BufferedImage image = ImageIO.read(this.inputFile);
			int width = image.getWidth();
			int height = image.getHeight();

			int w = image.getWidth();
			int h = image.getHeight();
	
			// We need 3 integers (for R,G,B color values) per pixel.
			int[] pixels = new int[w * h * 3];
			image.getRaster().getPixels(0, 0, w, h, pixels);
       
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					Color c=new Color(image.getRGB(j, i));

					//adding factor to rgb values
					int r=c.getRed()+ factor;
					int b=c.getBlue()+factor;
					int g=c.getGreen()+factor;
					if (r >= 256) {
					 r = 255;
					} else if (r < 0) {
					r = 0;
					}
				
					if (g >= 256) {
					g = 255;
					} else if (g < 0) {
					g = 0;
					}
				
					 if (b >= 256) {
					b = 255;
					} else if (b < 0) {
					b = 0;
					 }
					image.setRGB(j, i,new Color(r,g,b).getRGB());
				}
			}
			this.outputFile = new File("src/main/java/imgs/brigthscale.jpg");
			ImageIO.write(image, "jpg", this.outputFile);
			elapsedMs = System.currentTimeMillis() - startTime;

		} catch (Exception e) {
			System.out.print("Exception: " + e.toString());
		}
	}

	public long getElapsedMs(){
		return this.elapsedMs;
	}

	public File getOutputFile(){
		return this.outputFile;
	}
}