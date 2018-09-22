package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;

public class OpacityScale {
	File inputFile;
	File outputFile;
	Double opacityLevel;

	public OpacityScale(File inputFile, Double opLevel,String opMode) {
		this.inputFile = inputFile;
		this.opacityLevel = opLevel;
		this.sequentialProcess();
	}

	private void sequentialProcess(){
		try {
			BufferedImage image = ImageIO.read(this.inputFile);
			int	width = image.getWidth();
			int	height = image.getHeight();
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {

					Color c = new Color(image.getRGB(j, i));
					int red = (int) (c.getRed() * this.opacityLevel);
					int green = (int) (c.getGreen() * this.opacityLevel);
					int blue = (int) (c.getBlue() * this.opacityLevel);
					Color newColor = new Color(red , green , blue);
				
					image.setRGB(j, i, newColor.getRGB());
				}
			}
			this.outputFile = new File("src/main/java/imgs/opac.jpg");
			ImageIO.write(image, "jpg", this.outputFile);

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}

	public File getOutputFile(){
		return this.outputFile;
	}
}