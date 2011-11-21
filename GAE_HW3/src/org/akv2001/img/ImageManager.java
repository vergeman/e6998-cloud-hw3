package org.akv2001.img;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

/*
 * http://code.google.com/appengine/docs/java/images/overview.html
 */
public class ImageManager {
	 byte[] orig;
	 ImagesService imagesService;
	 String type;
	 
	public ImageManager(byte[] orig_bytes, String res_type) {
		imagesService = ImagesServiceFactory.getImagesService();
		orig = orig_bytes;
		type = res_type;
	}
	
	public Image rotate_and_resize(int rotate, int width, int height) {
		Image newImg;
		
		newImg = resize(this.orig, width, height);
		newImg = rotate(newImg.getImageData(), rotate);
		return newImg;
	}
	
	public Image resize(byte[] img, int width, int height) {
	    Image orig = ImagesServiceFactory.makeImage(img);
	    Transform resize = ImagesServiceFactory.makeResize((int) (1.5 * orig.getWidth()),(int) (1.5 * orig.getHeight()));
	    Image newImage = imagesService.applyTransform(resize, orig, ImagesService.OutputEncoding.PNG);
	    return newImage;

	}
	
	public Image rotate(byte[] img, int degrees) {
		 Image orig = ImagesServiceFactory.makeImage(img);
		 Transform rotate = ImagesServiceFactory.makeRotate(degrees);
		 Image newImage = imagesService.applyTransform(rotate, orig, ImagesService.OutputEncoding.PNG);
		 return newImage;
	}
	
}
