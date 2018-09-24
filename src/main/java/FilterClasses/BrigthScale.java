package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

public class BrigthScale {
	private File inputFile;
	private File outputFile;
	private static int factor;
	private long elapsedMs;

	//Propiedades de modo paralelo
	private static int[] srcRgbArray;
	private static int[] destRgbArray;

	public BrigthScale(File inputFile,int factor,String opMode){
		this.inputFile = inputFile;
		this.factor = factor;
		if(opMode == "Paralelo")
			paralellProcess();
		else
			sequentialProcess();
	}

	private void sequentialProcess(){
		long startTime = System.currentTimeMillis();
		try {
			BufferedImage image = ImageIO.read(this.inputFile);
			int width = image.getWidth();
			int height = image.getHeight();
       
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

	private void paralellProcess(){
		long start = (long)(System.nanoTime() / 1000000);
		try {
			BufferedImage srcImg = ImageIO.read(this.inputFile);
			int width = srcImg.getWidth();
			int height = srcImg.getHeight();

			srcRgbArray = srcImg.getRGB(0,0, width, height, null, 0, width);
			destRgbArray = new int[srcRgbArray.length];

			IntStream pixelIndexList= IntStream.range(0, srcRgbArray.length);
			destRgbArray = pixelIndexList.parallel().map(BrigthScale::applyPixelTransform).toArray();

			BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			dstImage.setRGB(0, 0, width, height, destRgbArray, 0, width);
			this.outputFile = new File("src/main/java/imgs/brigth.jpg");
		
			ImageIO.write(dstImage, "jpg", this.outputFile);
			long finish = (long)(System.nanoTime() / 1000000);
			this.elapsedMs = (long)(finish - start);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int applyPixelTransform(int pixelIndex){
		Color pixelColor = new Color(srcRgbArray[pixelIndex]);
		int red = pixelColor.getRed() + factor;;
		int green = pixelColor.getGreen() + factor;
		int blue = pixelColor.getBlue() + factor;
		if (red >= 256) {
			red = 255;
		} else if (red < 0) {
			red = 0;
		}
	   
		if (green >= 256) {
		   green = 255;
		} else if (green < 0) {
		   green = 0;
		}
	   
		if (blue >= 256) {
		   blue = 255;
		} else if (blue < 0) {
		   blue = 0;
		}
		Color newColor = new Color(red,green,blue);
		return newColor.getRGB();
	}

	public long getElapsedMs(){
		return this.elapsedMs;
	}

	public File getOutputFile(){
		return this.outputFile;
	}
}