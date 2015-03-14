package drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

public class CategoryImageBuilder 
{
	private BufferedImage image;

	private List<Double[]> data;
	
	private Graphics2D graphics;
	
	private int offsetFromTop;
	
	private final int WIDTH = 360;
	private final int HEIGHT_PER_ROW = 100;
	private final int HORIZONTAL_INCREMENT = 6;
	
	
	public CategoryImageBuilder(List<Double[]> section) {
		this.data = section;
		int height = HEIGHT_PER_ROW * section.size();
		
		if(height == 0) {
			height = 100;
		}

		
		image = new BufferedImage(WIDTH, height, BufferedImage.TYPE_3BYTE_BGR);
		graphics = image.createGraphics();

		graphics.setBackground(Color.WHITE);
		graphics.fillRect(0, 0, WIDTH, height);
		
		graphics.setColor(Color.BLACK);

	}
	
	public void createCategoryImage() {
		offsetFromTop = 0;
		
		for(int i = 0; i < data.size(); i++, offsetFromTop += 100) {
			drawSingleLine(i);
		}
	}
	
	private void drawSingleLine(int lineNum) {
		Double[] lineData = data.get(lineNum);
		int offsetFromLeft = 0;
		int verticalPlacement1, verticalPlacement2;
		
		// draw the lines
		for(int i = 0; i < lineData.length-1; i++, offsetFromLeft += HORIZONTAL_INCREMENT) {
			verticalPlacement1 = lineData[i].intValue();
			verticalPlacement2 = lineData[i+1].intValue();
			
			graphics.drawLine(offsetFromLeft, offsetFromTop + verticalPlacement1, 
							  offsetFromLeft + HORIZONTAL_INCREMENT, 
							  offsetFromTop + verticalPlacement2);	
		}
		
	}
	
	
	public void savePngImage(File outputFile) throws Exception {
		ImageIO.write(image, "png", outputFile);
	}
}
