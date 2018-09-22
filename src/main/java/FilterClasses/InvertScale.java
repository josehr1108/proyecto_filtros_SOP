package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;


public class InvertScale {
	File inputFile;
	File outputFile;
	private long elapsedMs;

	public InvertScale(File inputFile,String opMode) {
		this.inputFile = inputFile;
		this.sequentialProcess();
	}

	public void sequentialProcess(){
		long startTime = System.currentTimeMillis();
		try {
			BufferedImage image = ImageIO.read(this.inputFile);
			int width = image.getWidth();
			int height = image.getHeight();
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int rgb = image.getRGB(j, i);
					Color color = new Color(rgb, true);
					int r = 255 - color.getRed();
					int g = 255 - color.getGreen();
					int b = 255 - color.getBlue();
	
					color = new Color(r, g, b, color.getAlpha());
					image.setRGB(j, i, color.getRGB());
				}
			}
			this.outputFile = new File("src/main/java/imgs/invertscale.jpg");
			ImageIO.write(image, "jpg", this.outputFile);
			elapsedMs = System.currentTimeMillis() - startTime;
		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}

	public long getElapsedMs(){
		return this.elapsedMs;
	}

	public File getOutputFile(){
		return this.outputFile;
	}
}