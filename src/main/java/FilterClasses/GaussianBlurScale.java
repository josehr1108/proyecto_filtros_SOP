package FilterClasses;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;


public class GaussianBlurScale {
	private final int[] FILTER = { 1, 2, 1,
								   2, 4, 2,
								   1, 2, 1 };

	private final int FILTER_WIDTH = 9;
	
	private File inputFile;
	private File outputFile;
	private long elapsedMs;

	//Propiedades de modo paralelo
	private static int[] srcRgbArray;
	private static int[] destRgbArray;

	public GaussianBlurScale(File inputFile, String opMode){
			this.inputFile = inputFile;
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
			int sum = IntStream.of(FILTER).sum();

			int[] input = image.getRGB(0, 0, width, height, null, 0, width);
			int[] output = new int[input.length];

			final int pixelIndexOffset = width - FILTER_WIDTH;
			final int centerOffsetX = FILTER_WIDTH / 2;
			final int centerOffsetY = FILTER.length / FILTER_WIDTH / 2;
		
			// apply filter
			for (int h = height - FILTER.length / FILTER_WIDTH + 1, w = width - FILTER_WIDTH + 1, y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					int rgb = image.getRGB(x, y);
					Color color = new Color(rgb, true);
					int r = 0;
					int g = 0;
					int b = 0;
					for (int filterIndex = 0, pixelIndex = y * width + x;
							filterIndex < FILTER.length;
							pixelIndex += pixelIndexOffset) {
						for (int fx = 0; fx < FILTER_WIDTH; fx++, pixelIndex++, filterIndex++) {
							int col = input[pixelIndex];
							int factor = FILTER[filterIndex];

							// sum up color channels seperately
							r += ((col >>> 16) & 0xFF) * factor;
							g += ((col >>> 8) & 0xFF) * factor;
							b += (col & 0xFF) * factor;
						}
					}
					r /= sum;
					g /= sum;
					b /= sum;
					// combine channels with full opacity
					output[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;

				color = new Color(r, g, b, color.getAlpha());
				image.setRGB(x, y, color.getRGB());
				}
			}
			this.outputFile = new File("src/main/java/imgs/gaussianblurscale.jpg");
			ImageIO.write(image, "jpg", this.outputFile);
			elapsedMs = System.currentTimeMillis() - startTime;
		} catch (Exception e) {
			//TODO: handle exception
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
			destRgbArray = pixelIndexList.parallel().map(GaussianBlurScale::applyPixelTransform).toArray();

			BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			dstImage.setRGB(0, 0, width, height, destRgbArray, 0, width);
			this.outputFile = new File("src/main/java/imgs/gaussianblurscale.jpg");
		
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
