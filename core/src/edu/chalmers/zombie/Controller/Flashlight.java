package edu.chalmers.zombie.controller;

/**
 * Created by daniel on 5/19/2015.
 */
import com.badlogic.gdx.Gdx;
import edu.chalmers.zombie.ZombieWeek;
import edu.chalmers.zombie.model.GameModel;
import edu.chalmers.zombie.utils.Constants;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import javax.swing.*;
public class Flashlight {
    private int centerX, centerY;
    private int x1, y1;
    private int x2, y2;
    private Area lightCone;
    private float direction;
    private Graphics2D g;
    private BufferedImage image;

    public Flashlight() {


    }
}
