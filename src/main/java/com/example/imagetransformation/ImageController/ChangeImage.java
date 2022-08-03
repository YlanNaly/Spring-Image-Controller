package com.example.imagetransformation.ImageController;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
public class ChangeImage {
    @GetMapping({"/hello/{name}","/hello/"})
    public String HelloWorld(@PathVariable(required = false)String name){
        if(name == null){
            return "hello";
        }
        else {
            return "Hello " +name ;
        }
    }
    @PostMapping(
            path = "image/" ,
            produces = {MediaType.IMAGE_JPEG_VALUE , MediaType.IMAGE_PNG_VALUE} ,
            consumes = {MediaType.IMAGE_PNG_VALUE , MediaType.IMAGE_JPEG_VALUE}
    )

    public @ResponseBody byte[] postTest(@RequestBody byte[] image)throws IOException {
        ByteArrayInputStream img = new ByteArrayInputStream(image);
        return makeGray(ImageIO.read(img));
    }
    public static byte[] makeGray(BufferedImage img) throws IOException {
        byte[] bytes = new byte[0];
        for (int x = 0; x < img.getWidth(); ++x)
            for (int y = 0; y < img.getHeight(); ++y)
            {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                double color = 0.2126 * Math.pow(r / 255.0, 2.2) + 0.7152 * Math.pow(g / 255.0, 2.2) + 0.0722 * Math.pow(b / 255.0, 2.2);
                int contrast = (int) (255.0 * Math.pow(color , 1.0 / 2.2));
                int grey = (contrast << 16) + (contrast << 8) + contrast;
                img.setRGB(x, y, grey);
                ByteArrayOutputStream byteImage = new ByteArrayOutputStream();
                ImageIO.write(img, "jpeg", byteImage);
                bytes = byteImage.toByteArray() ;
            }
        return bytes ;
    }
}
