package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

public class GrayScale{
	File inputFile;
	File outputFile;
	private long elapsedMs;

	//Propiedades de modo paralelo
	private static int[] srcRgbArray;
    private int first;		//indices de inicio y final para este segmento de array 
    private int length;
	private static int[] destRgbArray;

	private final int LIMIT_CHUNK = 10000000; //el arreglo se divide en trozos de este tamaño máximo

	public GrayScale(File inputFile, String opMode) {
		this.inputFile = inputFile;
		if(opMode == "Paralelo"){
			this.paralellProcess();
		}else{
			this.sequentialProcess();
		}
	}

	/*
	public GrayScale(int[] imgBuffer,int first,int length,int[] destBuffer){
		this.srcRgbArray = imgBuffer;
		this.first = first;
		this.length = length;
		this.destRgbArray = destBuffer;
	}*/

	private void sequentialProcess(){
		long start = (long)(System.nanoTime() / 1000000);
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
			long finish = (long)(System.nanoTime() / 1000000);
			this.elapsedMs = (long)(finish - start);

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}

	private static int applyPixelTransform(int pixelIndex){
		Color pixelColor = new Color(srcRgbArray[pixelIndex]);
		int red = (int) (pixelColor.getRed() * 0.299);
		int green = (int) (pixelColor.getGreen() * 0.587);
		int blue = (int) (pixelColor.getBlue() * 0.114);
		Color newColor = new Color(red + green + blue,
									red + green + blue,
									red + green + blue);
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
			this.destRgbArray = lista.parallel().map(GrayScale::applyPixelTransform).toArray();

			BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			dstImage.setRGB(0, 0, width, height, destRgbArray, 0, width);
			this.outputFile = new File("src/main/java/imgs/gray.jpg");
		
			ImageIO.write(dstImage, "jpg", this.outputFile);
			long finish = (long)(System.nanoTime() / 1000000);
			this.elapsedMs = (long)(finish - start);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*
	private void paralellProcess(){
		try {
			long start = (long)(System.nanoTime() / 1000000);
			BufferedImage srcImage = ImageIO.read(this.inputFile);

			//BufferedImage processedImage = applyFilter(srcImage);
			this.outputFile = new File("src/main/java/imgs/gray.jpg");
			ImageIO.write(processedImage, "jpg", this.outputFile);
			long finish = (long)(System.nanoTime() / 1000000);
			this.elapsedMs = (long)(finish - start);
		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}

	private BufferedImage applyFilter(BufferedImage srcImage){
		int	width = srcImage.getWidth();
		int	height = srcImage.getHeight();

		int[] srcBuffer = srcImage.getRGB(0, 0, width, height, null, 0, width);
		int[] destBuffer = new int[srcBuffer.length];

		GrayScale filterChunk = new GrayScale(srcBuffer,0,srcBuffer.length,destBuffer);

		ForkJoinPool pool = new ForkJoinPool();

        long startTime = System.nanoTime();
		pool.invoke(filterChunk);
		long endTime = System.nanoTime();
		this.elapsedMs = TimeUnit.NANOSECONDS.toMillis(endTime-startTime);

        BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        dstImage.setRGB(0, 0, width, height, destBuffer, 0, width);

        return dstImage;
	}

	protected void computeDirectly(){
		System.out.println("Este ciclo va desde index " + this.first +
						   " hasta index " + ((this.first + this.length) -1));
		for (int index = this.first; index < this.first + this.length; index++) {
			Color pixelcolor = new Color(srcRgbArray[index]);
			int red = (int) (pixelcolor.getRed() * 0.299);
			int green = (int) (pixelcolor.getGreen() * 0.587);
			int blue = (int) (pixelcolor.getBlue() * 0.114);
			Color newColor = new Color(red + green + blue,
									   red + green + blue,
									   red + green + blue);
			destRgbArray[index] = newColor.getRGB();
		}
	}

	@Override
	protected void compute() {
		if (this.length <= LIMIT_CHUNK) {
            computeDirectly();
            return;
        }

		int split = this.length / 2;
		System.out.println("Array se parte en index " + split);

        invokeAll(new GrayScale(this.srcRgbArray, first, split, this.destRgbArray),
                  new GrayScale(this.srcRgbArray, first + split, length - split, this.destRgbArray));
	}
*/
	public long getElapsedMs(){
		return this.elapsedMs;
	}

	public File getOutputFile(){
		return this.outputFile;
	}
}