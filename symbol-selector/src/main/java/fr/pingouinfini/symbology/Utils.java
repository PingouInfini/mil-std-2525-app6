package fr.pingouinfini.symbology;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.batik.transcoder.ToSVGAbstractTranscoder.KEY_WIDTH;

public class Utils {

    public static JButton createSVGButton(String svgPath, int width, int height) {
        JButton button = new JButton();
        try {
            byte[] svgBytes = getSVGBytes(svgPath);
            Icon svgIcon = createSVGIcon(svgBytes, width, height);
            button.setIcon(svgIcon);
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
        return button;
    }

    private static byte[] getSVGBytes(String svgPath) throws IOException {
        return Files.readAllBytes(Paths.get(svgPath));
    }

    private static Icon createSVGIcon(byte[] svgBytes, int width, int height) throws TranscoderException {
        PNGTranscoder transcoder = new PNGTranscoder();
        TranscodingHints hints = new TranscodingHints();
        hints.put(KEY_WIDTH, (float) width);
        hints.put(ImageTranscoder.KEY_HEIGHT, (float) height);
        transcoder.setTranscodingHints(hints);

        TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(svgBytes));
        // Create a BufferedImage from the SVG content
        BufferedImage image = transcoder.createImage(50, 50);

        // Create an ImageIcon from the BufferedImage
        return new ImageIcon(image);
    }
}
