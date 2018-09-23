package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.google.common.base.Stopwatch;

public class GrayScale extends RecursiveAction{
	File inputFile;
	File outputFile;
	private long elapsedMs;

	//Propiedades de modo paralelo
	private int[] srcRgbArray;
    private int first;		//indices de inicio y final para este segmento de array 
    private int length;
	private int[] destRgbArray;
	private final int LIMIT_CHUNK = 10000; //el arreglo se divide en trozos de este tamaño máximo

	public GrayScale(File inputFile, String opMode) {
		this.inputFile = inputFile;
		if(opMode == "Paralelo"){
			this.paralellProcess();
		}
		else{
			this.sequentialProcess();
		}
	}

	public GrayScale(int[] imgBuffer,int first,int length,int[] destBuffer){
		this.srcRgbArray = imgBuffer;
		this.first = first;
		this.length = length;
		this.destRgbArray = destBuffer;
	}

	private void sequentialProcess(){
		try {
			BufferedImage image = ImageIO.read(this.inputFile);
			int width = image.getWidth();
			int height = image.getHeight();

			//inicializaciones requeridas para llamar a computeDirectly secuencial
			//this.first,this.length,srcRgbArray
			this.first = 0;
			this.length = width * height;
			this.srcRgbArray = image.getRGB(0, 0, width, height, null, 0, width);
			this.destRgbArray = new int[srcRgbArray.length];

			long startTime = System.nanoTime();
			computeDirectly();
			long endTime = System.nanoTime();
			this.elapsedMs = TimeUnit.NANOSECONDS.toMillis(endTime-startTime);

			BufferedImage finalImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			finalImg.setRGB(0, 0, width, height, destRgbArray, 0, width);
			this.outputFile = new File("src/main/java/imgs/gray.jpg");
			ImageIO.write(finalImg, "jpg", this.outputFile);
		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}

	private void paralellProcess(){
		try {
			BufferedImage srcImage = ImageIO.read(this.inputFile);
			BufferedImage processedImage = applyFilter(srcImage);
			this.outputFile = new File("src/main/java/imgs/gray.jpg");
			ImageIO.write(processedImage, "jpg", this.outputFile);
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
		if (this.length < LIMIT_CHUNK) {
            computeDirectly();
            return;
        }

        int split = this.length / 2;

        invokeAll(new GrayScale(this.srcRgbArray, first, split, this.destRgbArray),
                new GrayScale(this.srcRgbArray, first + split, length - split, this.destRgbArray));
	}

	public long getElapsedMs(){
		return this.elapsedMs;
	}

	public File getOutputFile(){
		return this.outputFile;
	}
}