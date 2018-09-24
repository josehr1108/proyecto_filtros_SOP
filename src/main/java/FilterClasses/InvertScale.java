package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;


public class InvertScale {
	File inputFile;
	File outputFile;
	private long elapsedMs;

		//Propiedades de modo paralelo
	private static int[] srcRgbArray;
	private static int[] destRgbArray;

	private final int LIMIT_CHUNK = 10000000; //el arreglo se divide en trozos de este tamaño máximo


	public InvertScale(File inputFile,String opMode) {
		this.inputFile = inputFile;
		if(opMode == "Paralelo"){
			this.paralellProcess();
		}else{
			this.sequentialProcess();
		}
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

	private static int applyPixelTransform(int pixelIndex){
		Color color = new Color(srcRgbArray[pixelIndex]);
					int red = 255 - color.getRed();
					int green = 255 - color.getGreen();
					int blue = 255 - color.getBlue();
			color = new Color(red, green, blue, color.getAlpha());
			
		return color.getRGB();
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
			this.destRgbArray = lista.parallel().map(InvertScale::applyPixelTransform).toArray();

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