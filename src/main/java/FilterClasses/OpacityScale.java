package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

public class OpacityScale {
	File inputFile;
	File outputFile;
	static Double opacityLevel;
	private long elapsedMs;


	//Propiedades de modo paralelo
	private static int[] srcRgbArray;
	private static int[] destRgbArray;

	public OpacityScale(File inputFile, Double opLevel,String opMode) {
		this.inputFile = inputFile;
		this.opacityLevel = opLevel;
		this.sequentialProcess();
	}

	private Double getOpacityLevel(){
		return this.opacityLevel;
	};

	private void sequentialProcess(){
		long startTime = System.currentTimeMillis();
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
			elapsedMs = System.currentTimeMillis() - startTime;

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}


	private static int applyPixelTransform(int pixelIndex){

		Color c = new Color(srcRgbArray[pixelIndex]);

		int red = (int) (c.getRed() * opacityLevel);
		int green = (int) (c.getGreen() * opacityLevel);
		int blue = (int) (c.getBlue() * opacityLevel);
		Color newColor = new Color(red , green , blue);

		
			
		return newColor.getRGB();
	}


	private void paralellProcess(){
		long start = (long)(System.nanoTime() / 1000000);
		try {
			BufferedImage srcImg = ImageIO.read(this.inputFile);
			int width = srcImg.getWidth();
			int height = srcImg.getHeight();

			this.srcRgbArray = srcImg.getRGB(0,0, width, height, null, 0, width);
			this.destRgbArray = new int[srcRgbArray.length];

			IntStream lista= IntStream.range(0, srcRgbArray.length);
			this.destRgbArray = lista.parallel().map(OpacityScale::applyPixelTransform).toArray();

			BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			dstImage.setRGB(0, 0, width, height, destRgbArray, 0, width);
			this.outputFile = new File("src/main/java/imgs/invert.jpg");
		
			ImageIO.write(dstImage, "jpg", this.outputFile);
			long finish = (long)(System.nanoTime() / 1000000);
			this.elapsedMs = (long)(finish - start);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public long getElapsedMs(){
		return this.elapsedMs;
	}

	public File getOutputFile(){
		return this.outputFile;
	}
}