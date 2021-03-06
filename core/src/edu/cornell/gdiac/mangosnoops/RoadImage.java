package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class RoadImage extends RoadObject {
    // CONSTANTS
    /** How fast we change frames (one frame per 4 calls to update) */
    private float animationSpeed = 0.25f;
    /** The number of animation frames in our filmstrip */
    private int   numAnimationFrames;

    // ATTRIBUTES
    /** Current animation frame for this ship */
    private float animeframe;
    /** How high the enemy hovers in the world */
    protected float hoverDistance = 4.5f;
    /** Data about this image - name, miles (for exit signs), texture */
    private String name;
    private int miles = -1;
    private Texture texture;
    /** speed of road */
    private float currSpeed;
    /** The minimum animation frame */
    protected int minAnimFrame;
    /** The maximum animation frame */
    protected int maxAnimFrame;

    /**
     * Return the type of RoadObject
     */
    public ObjectType getType() {
        return ObjectType.IMAGE;
    }

    /**
     * Return the name of the RoadObject
     */
    public String getName() { return name; }

    /**
     * Return the miles, or -1 if this item does not have an associated mileage
     */
    public int getMiles() { return miles; }

    /**
     * Set the miles.
     */
    public void setMiles(int m) { miles = m; }

    /**
     * Construct a roadside image at the given position.
     * @param x the x-position of the image
     * @param y the y-position of the image
     * @param name the name of the roadside image
     */
    public RoadImage(float x, float y, String name) {
        super();
        position = new Vector2(x,y);
        this.name = name;
        minAnimFrame = 0;
        maxAnimFrame = 0;
        animeframe = 0;
        // TODO velocity, moving stuff
    }

    /**
     * Construct a roadside image at the given position.
     * @param x the x-position of the image
     * @param y the y-position of the image
     * @param name the name of the roadside image
     * @param hovDistance how far above the road this object is
     */
    public RoadImage(float x, float y, String name, float hovDistance) {
        super();
        position = new Vector2(x,y);
        this.name = name;
        minAnimFrame = 0;
        maxAnimFrame = 0;
        animeframe = 0;
        hoverDistance = hovDistance;
    }

    /**
     * Create a copy of a road image.
     */
    public RoadImage(RoadImage i) {
        super();
        position = new Vector2(i.getX(),i.getY());
        name = i.getName();
        miles = i.getMiles();
        minAnimFrame = 0;
        maxAnimFrame = 0;
        animeframe = 0;
        hoverDistance = i.getHoverDistance();
    }

    /**
     * Construct a roadside image at the given position associated with a
     * certain mileage number. Used for exit signs.
     *
     * @param x the x-position of the image
     * @param y the y-position of the image
     * @param name the name of the roadside image
     * @param miles the mileage associated with this image
     */
    public RoadImage(float x, float y, String name, int miles) {
        super();
        position = new Vector2(x,y);
        this.name = name;
        this.miles = miles;
        minAnimFrame = 0;
        maxAnimFrame = 0;
        animeframe = 0;
    }

    /**
     * Set a new hover distance.
     * @param newHoverDistance the new hover distance
     */
    public void setHoverDistance(float newHoverDistance) {
        hoverDistance = newHoverDistance;
    }

    /**
     * Get the current hover distance.
     * @return new hover distance
     */
    public float getHoverDistance() {
        return hoverDistance;
    }

    /**
     * Updates the animation frame and position of this ship.
     *
     * Notice how little this method does.  It does not actively fire the weapon.  It
     * only manages the cooldown and indicates whether the weapon is currently firing.
     * The result of weapon fire is managed by the GameplayController.
     *
     * @param delta Number of seconds since last animation frame
     */
    public void update(float delta, float newSpeed) {
        super.update(delta);
        setSpeed(newSpeed);
        animeframe += animationSpeed;
        setY(getY()-currSpeed * delta);

    }

    public void draw(GameCanvas canvas, Texture t) {
        float width = t.getWidth() * 0.001f;
        float height = t.getHeight() * 0.001f;
        float hover = hoverDistance;
        if (name.toLowerCase().equals("sunflower")) {
            width /= 5.0f;
            height /= 5.0f;
            hover = 4.309f;
        }
        canvas.drawRoadObject(t, getX(), getY(), hover,
                width, height, 90, 0);
    }

    public void draw(GameCanvas canvas, Texture t, BitmapFont font) {
        float width = t.getWidth() * 0.001f;
        float height = t.getHeight() * 0.001f;
        float hover = hoverDistance;
        if (name.toLowerCase().equals("sunflower")) {
            width /= 5.0f;
            height /= 5.0f;
            hover = 4.309f;
        }
        canvas.drawRoadObject(t, getX(), getY(), hover,
                width, height, 90, 0);

        // draw text
        font.setColor(Color.WHITE);
        canvas.drawExitSign("" + miles, t, font, getX(), getY(), hover);
    }

    public void setSpeed (float s) {
        currSpeed = s;
    }
}
