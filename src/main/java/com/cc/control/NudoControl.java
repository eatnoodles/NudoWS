package com.cc.control;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cc.Application;

/**
 * @author Caleb Cheng
 *
 */
@Component
@Path("/API")
public class NudoControl {
	
	private static final Logger LOG = LoggerFactory.getLogger(NudoControl.class);
	
	@Context
	HttpHeaders header;
	
	@Context
	HttpServletResponse response;
	
	@Path("/img/{image}")
	@Produces("image/png")
	public Response getImage(@PathParam("image") int num) {
		try {
			String path = String.format("img/%s.png", num > 100 ? "default" : num);
		    InputStream is = Application.class.getClassLoader().getResourceAsStream(path);

		    if (is == null) {
		    	path = String.format("img/default.png");
		    	is = Application.class.getClassLoader().getResourceAsStream(path);
		    }
		    
		    BufferedImage image = ImageIO.read(is);
		    if (image != null) {
		    	image = this.resize(image, 50, 50, false);
		    	
		    	Graphics g = image.getGraphics();
		    	
		    	int height = image.getHeight();
		    	int width = image.getWidth();
		    	
	            Font f = new Font(null, Font.BOLD, 12);  
	            Color mycolor = Color.black;
	            g.setColor(mycolor);
	            g.setFont(f);
	              
	            g.drawString(Integer.toString(num), (width/2) , height - 12);  
	              
	            g.dispose();
	            
		        OutputStream out = response.getOutputStream();
		        ImageIO.write(image, "png", out);
		        out.close();
		    }
		    return Response.noContent().build();
		} catch (Exception e) {
			return Response.noContent().build();
		}
	}
	
	@Path("/parrot/{index}/{wording}")
	@Produces("image/png")
	public Response getParrotImage(@PathParam("wording") String wording, @PathParam("index") int index) {
		try {
			LOG.info("wording="+wording);
			
			wording = URLDecoder.decode(wording, "UTF-8");
			
			String path = String.format("img/parrot%s.png", index);
			
		    InputStream is = Application.class.getClassLoader().getResourceAsStream(path);

		    if (is == null) {
		    	path = "img/parrot.png";
		    	is = Application.class.getClassLoader().getResourceAsStream(path);
		    }
		    
		    BufferedImage image = ImageIO.read(is);
		    if (image != null) {
		    	image = this.resize(image, 674, 368, false);
		    	Graphics g = image.getGraphics();
		    	
		    	Font f = Font.createFont(Font.TRUETYPE_FONT, Application.class.getClassLoader().getResourceAsStream("MINGLIU.TTC")).deriveFont(Font.BOLD, 60);
		    	
	            Color mycolor = Color.WHITE;
	            g.setColor(mycolor);
	            
	            this.drawCenteredString(g, wording, image, f);
	              
	            g.dispose();
	            
	            response.setCharacterEncoding("UTF-8");
	            response.setHeader("content-type","image/png;charset=UTF-8");
		        OutputStream out = response.getOutputStream();
		        ImageIO.write(image, "png", out);
		        out.close();
		    }
		    return Response.noContent().build();
		} catch (Exception e) {
			return Response.noContent().build();
		}
	}
	
	/**
	 * @param g
	 * @param text
	 * @param image
	 */
	public void drawCenteredString(Graphics g, String text, BufferedImage image, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = image.getMinX() + (image.getWidth() - metrics.stringWidth(text)) / 2;
	    int y = image.getMinY() + ((image.getHeight() - metrics.getHeight())) + metrics.getAscent();
	    g.setFont(font);
	    g.drawString(text, x, y);
	}
	
	/**
	 * 
	 * @param originalImage
	 * @param scaledWidth
	 * @param scaledHeight
	 * @param preserveAlpha
	 * @return
	 */
	private BufferedImage resize(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
        g.dispose();
        return scaledBI;
    }
}
