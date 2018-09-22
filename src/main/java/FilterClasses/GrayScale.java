package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;

import com.google.common.base.Stopwatch;

public class GrayScale {
	File inputFile;
	File outputFile;
	private long elapsedMs;

	public GrayScale(File inputFile, String opMode) {
		this.inputFile = inputFile;
		this.sequentialProcess();
	}

	private void sequentialProcess(){
		long startTime = System.currentTimeMillis();
		try {
			BufferedImage image = ImageIO.read(this.inputFile);
			int	width = image.getWidth();
			int	height = image.getHeight();
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					Color c = new Color(image.getRGB(j, i));
					int red = (int) (c.getRed() * 0.299);
					int green = (int) (c.getGreen() * 0.587);
					int blue = (int) (c.getBlue() * 0.114);
					Color newColor = new Color(red + green + blue,

					red + green + blue, red + green + blue);

					image.setRGB(j, i, newColor.getRGB());
				}
			}
			this.outputFile = new File("src/main/java/imgs/gray.jpg");
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