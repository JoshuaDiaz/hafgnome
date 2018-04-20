
/*
 * GameplayController.java
 *
 * For many of you, this class will seem like the most unusual one in the entire project.  
 * It implements a lot of functionality that looks like it should go into the various 
 * GameObject subclasses. However, a lot of this functionality involves the creation or
 * destruction of objects.  We cannot do this without a lot of cyclic dependencies, 
 * which are bad.
 *
 * You will notice that gameplay-wise, most of the features in this class are 
 * interactions, not actions. This demonstrates why a software developer needs to 
 * understand the difference between these two.  
 *
 * You will definitely need to modify this file in Part 2 of the lab. However, you are
 * free to modify any file you want.  You are also free to add new classes and assets.
 *
 * Author: Walker M. White
 * Based on original Optimization Lab by Don Holden, 2007
 * LibGDX version, 2/2/2015
 */
package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import edu.cornell.gdiac.mangosnoops.hudentity.*;
import edu.cornell.gdiac.mangosnoops.roadentity.*;
import edu.cornell.gdiac.util.FilmStrip;


import java.util.Random;

/**
 * Controller to handle gameplay interactions.
 * This controller also acts as the root class for all the models.
 */
public class GameplayController {
	/** The change in x, computed based on the wheel angle */
	private float rotationMagnitude;
	/** Road instance, contains road "conveyor belt" logic */
	private Road road;
	/** Car instance, containing information about the wheel and children */
	private Car yonda;
	/** Location and animation information for the wheel **/
	private Wheel wheel;
	/** Location, animation information for vroomstick */
	private VroomStick vroomStick;
	/** Location and animation information for the wheel */
	private Radio radio;
	/** Inventory */
	private Inventory inventory;
	/** Visor */
    private Visor visor;
	/** Contains location for the previous click, used for debouncing */
	private Vector2 prevClick = null;
	/** An array of enemies for this level */
	private Array<Gnome> gnomez;
	/** An array of events for this level */
	private Array<Event> events;
	/** The next event to happen */
	private int nextEvent;
	/** Object containing all information about the current level. This includes
	 *  everything specific to a level: the songs, enemies, events, etc. */
	private LevelObject level;
	/** Rearview enemy instance. The way it's handled right now, there is only
	 *  one at a time. FIXME: could change that if necessary */
	private RearviewEnemy rearviewEnemy;
	/** The y-position player is driving over, used for checking for events */
	private float ypos;
	
	private Image healthGauge;
	private Image rearviewBackground;
	private Image rearviewSeats;
	private Image rearviewCover;

	private ObjectSet<Image> hudObjects;
	/** If there is sun shining right now */
	public boolean sunShine;

	// Graphics assets for the entities
    /** The texture file for the wheel **/
    private static final String WHEEL_FILE = "images/DashHUD/Wheel.png";
    /** The texture file for the vroom stick*/
	private static final String VROOM_STICK_FILE = "images/DashHUD/vroomstick.png";
    /** The texture file for the gnomes */
	private static final String GNOME_FILE = "images/Enemies/gnome.png";
	private static final String REARVIEW_GNOME_FILE = "images/Enemies/gnome_rear.png";
	/** The texture file for the radio knob */
	private static final String RADIO_KNOB_FILE = "images/DashHUD/radioDial.png";
	/** The texture files for Nosh's moods */
	private static final String NOSH_HAPPY_FILE = "images/NoshTextures/nosh_happy.png";
	private static final String NOSH_NEUTRAL_FILE = "images/NoshTextures/nosh_neutral.png";
	private static final String NOSH_SAD_FILE = "images/NoshTextures/nosh_sad.png";
	private static final String NOSH_CRITICAL_FILE = "images/NoshTextures/nosh_critical.png";
	private static final String NOSH_SLEEP_FILE = "images/NoshTextures/nosh_sleep.png";
	/** The texture files for Ned's moods */
	private static final String NED_HAPPY_FILE = "images/NedTextures/ned_happy.png";
	private static final String NED_NEUTRAL_FILE = "images/NedTextures/ned_neutral.png";
	private static final String NED_SAD_FILE = "images/NedTextures/ned_sad.png";
	private static final String NED_CRITICAL_FILE = "images/NedTextures/ned_critical.png";
	private static final String NED_SLEEP_FILE = "images/NedTextures/ned_sleep.png";
	/** The texture files for all Items **/
	private static final String DVD_FILE = "images/Items/dvd.png";
	private static final String SNACK_FILE = "images/Items/snack.png";
	/** The texture files for the visor states */
    private static final String VISOR_OPEN_FILE = "images/visor_open.png";
    private static final String VISOR_CLOSED_FILE = "images/visor_closed.png";
	/** The texture file for the road */
	private static final String ROAD_FILE = "images/road.png";
	/** The texture file for the grass */
	private static final String GRASS_FILE = "images/grass.png";
	/** The texture file for the exit */
	private static final String EXIT_FILE = "images/exit.png";
	/** The texture file for the dash */
	private static final String DASH_FILE = "images/DashHUD/dashv2.png";
	/** The file for the health gauge */
	private static final String HEALTH_GAUGE_FILE = "images/DashHUD/gauge.png";
	/** The file for the health gauge pointer */
	private static final String HEALTH_POINTER_FILE = "images/DashHUD/pointer.png";
	/** Rearview mirror stuff */
	private static final String REARVIEW_BACKGROUND = "images/rearview_background.png";
	private static final String REARVIEW_COVER = "images/rearview_cover.png";
	private static final String REARVIEW_SEATS = "images/rearview_seats.png";

	/** Texture for road */
	private Texture roadTexture;
	/** Texture for grass */
	private Texture grassTexture;
	/** Texture for exit */
	private Texture exitTexture;

	/** Texture for the wheel */
	private Texture wheelTexture;
	/** Texture for the vroomstick */
	private Texture vroomStickTexture;
	/** Texture for the gnomes */
	private Texture gnomeTexture;
	private Texture rearviewGnomeTexture;
	/** Texture for the radio knob */
	private Texture radioknobTexture;
	/** Textures for nosh */
	private FilmStrip nosh_happy;
	private FilmStrip nosh_neutral;
	private FilmStrip nosh_sad;
	private FilmStrip nosh_critical;
	private FilmStrip nosh_sleep;
	/** Textures for ned */
	private FilmStrip ned_happy;
	private FilmStrip ned_neutral;
	private FilmStrip ned_sad;
	private FilmStrip ned_critical;
	private FilmStrip ned_sleep;
	/** Textures for items **/
	private Texture dvdTexture;
	private Texture snackTexture;
	/** Textures for visor states */
	private Texture visorOpen;
	private Texture visorClosed;

	/** Texture of the dash **/
	private Texture dashTexture;
	/** Texture of the health gauge */
	private Texture healthGaugeTexture;
	/** Texture of the health gauge's pointer */
	private Texture healthPointerTexture;
	/** Texture of the rear view mirror */
	private Texture rearviewBackgroundTexture;
	private Texture rearviewSeatsTexture;
	private Texture rearviewCoverTexture;

	// List of objects with the garbage collection set.
	/** The backing set for garbage collection */
	private Array<Gnome> backing;

	/** Enum specifying the region this level takes place in. */
	public enum Region {
		SUBURBS, HIGHWAY, MIDWEST, COLORADO;
	}

	/**
	 * Preloads the assets for this game.
	 *
	 * The asset manager for LibGDX is asynchronous.  That means that you
	 * tell it what to load and then wait while it loads them.  This is
	 * the first step: telling it what to load.
	 *
	 * @param manager Reference to global asset manager.
	 * @param assets  Asset list to track which assets where loaded
	 */
	public void preLoadContent(AssetManager manager, Array<String> assets) {
		manager.load(WHEEL_FILE,Texture.class);
		assets.add(WHEEL_FILE);
		manager.load(VROOM_STICK_FILE, Texture.class);
		assets.add(VROOM_STICK_FILE);
		manager.load(GNOME_FILE, Texture.class);
		assets.add(GNOME_FILE);
		manager.load(REARVIEW_GNOME_FILE, Texture.class);
		assets.add(REARVIEW_GNOME_FILE);
		manager.load(RADIO_KNOB_FILE, Texture.class);
		assets.add(RADIO_KNOB_FILE);
		manager.load(NOSH_HAPPY_FILE, Texture.class);
		assets.add(NOSH_HAPPY_FILE);
		manager.load(NOSH_NEUTRAL_FILE, Texture.class);
		assets.add(NOSH_NEUTRAL_FILE);
		manager.load(NOSH_SAD_FILE, Texture.class);
		assets.add(NOSH_SAD_FILE);
		manager.load(NOSH_CRITICAL_FILE, Texture.class);
		assets.add(NOSH_CRITICAL_FILE);
		manager.load(NOSH_SLEEP_FILE, Texture.class);
		assets.add(NOSH_SLEEP_FILE);
		manager.load(NED_HAPPY_FILE, Texture.class);
		assets.add(NED_HAPPY_FILE);
		manager.load(NED_NEUTRAL_FILE, Texture.class);
		assets.add(NED_NEUTRAL_FILE);
		manager.load(NED_SAD_FILE, Texture.class);
		assets.add(NED_SAD_FILE);
		manager.load(NED_CRITICAL_FILE, Texture.class);
		assets.add(NED_CRITICAL_FILE);
		manager.load(NED_SLEEP_FILE, Texture.class);
		assets.add(NED_SLEEP_FILE);
		manager.load(DVD_FILE,Texture.class);
		assets.add(DVD_FILE);
		manager.load(SNACK_FILE,Texture.class);
		assets.add(SNACK_FILE);
		manager.load(GRASS_FILE, Texture.class);
		assets.add(GRASS_FILE);
		manager.load(ROAD_FILE, Texture.class);
		assets.add(ROAD_FILE);
		manager.load(EXIT_FILE, Texture.class);
		assets.add(EXIT_FILE);
		manager.load(DASH_FILE,Texture.class);
		assets.add(DASH_FILE);
		manager.load(HEALTH_GAUGE_FILE, Texture.class);
		assets.add(HEALTH_GAUGE_FILE);
		manager.load(HEALTH_POINTER_FILE, Texture.class);
		assets.add(HEALTH_POINTER_FILE);
		manager.load(REARVIEW_BACKGROUND, Texture.class);
		assets.add(REARVIEW_BACKGROUND);
		manager.load(REARVIEW_COVER, Texture.class);
		assets.add(REARVIEW_COVER);
		manager.load(REARVIEW_SEATS, Texture.class);
		assets.add(REARVIEW_SEATS);
        manager.load(VISOR_OPEN_FILE, Texture.class);
        assets.add(VISOR_OPEN_FILE);
        manager.load(VISOR_CLOSED_FILE, Texture.class);
        assets.add(VISOR_OPEN_FILE);
	}

	/**
	 * Loads the assets for this game.
	 *
	 * The asset manager for LibGDX is asynchronous.  That means that you
	 * tell it what to load and then wait while it loads them.  This is
	 * the second step: extracting assets from the manager after it has
	 * finished loading them.
	 *
	 * @param manager Reference to global asset manager.
	 */
	public void loadContent(AssetManager manager) {
		wheelTexture = createTexture(manager,WHEEL_FILE);
		vroomStickTexture = createTexture(manager, VROOM_STICK_FILE);
		gnomeTexture = createTexture(manager, GNOME_FILE);
		rearviewGnomeTexture = createTexture(manager, REARVIEW_GNOME_FILE);
		radioknobTexture = createTexture(manager,RADIO_KNOB_FILE);
		nosh_happy = new FilmStrip(createTexture(manager,NOSH_HAPPY_FILE), 1, 1);
		nosh_neutral = new FilmStrip(createTexture(manager,NOSH_NEUTRAL_FILE), 1, 1);
		nosh_sad = new FilmStrip(createTexture(manager, NOSH_SAD_FILE), 1, 1);
		nosh_critical = new FilmStrip(createTexture(manager, NOSH_CRITICAL_FILE), 1, 1);
		nosh_sleep= new FilmStrip(createTexture(manager, NOSH_SLEEP_FILE), 1, 1);
		ned_happy = new FilmStrip(createTexture(manager,NED_HAPPY_FILE), 4, 5);
		ned_neutral = new FilmStrip(createTexture(manager,NED_NEUTRAL_FILE), 1, 1);
		ned_sad = new FilmStrip(createTexture(manager, NED_SAD_FILE), 1, 1);
		ned_critical = new FilmStrip(createTexture(manager, NED_CRITICAL_FILE), 1, 1);
		ned_sleep = new FilmStrip(createTexture(manager, NED_SLEEP_FILE), 1, 1);
		dvdTexture = createTexture(manager,DVD_FILE);
		snackTexture = createTexture(manager,SNACK_FILE);
		roadTexture = createTexture(manager, ROAD_FILE);
		grassTexture = createTexture(manager, GRASS_FILE);
		exitTexture = createTexture(manager, EXIT_FILE);
		dashTexture = createTexture(manager, DASH_FILE);
		healthGaugeTexture = createTexture(manager, HEALTH_GAUGE_FILE);
		healthPointerTexture = createTexture(manager, HEALTH_POINTER_FILE);
		rearviewBackgroundTexture = createTexture(manager, REARVIEW_BACKGROUND);
		rearviewSeatsTexture = createTexture(manager, REARVIEW_SEATS);
		rearviewCoverTexture = createTexture(manager, REARVIEW_COVER);
        visorOpen = createTexture(manager, VISOR_OPEN_FILE);
        visorClosed = createTexture(manager, VISOR_CLOSED_FILE);
	}

	private Texture createTexture(AssetManager manager, String file) {
		if (manager.isLoaded(file)) {
			Texture texture = manager.get(file, Texture.class);
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			return texture;
		}
		return null;
	}

	/**
	 * Creates a new GameplayController with no active elements.
	 *
	 * @param level is the Level information
	 */
	public GameplayController(LevelObject level) {
		this.level = level;
		gnomez = new Array<Gnome>();
		events = new Array<Event>();
		yonda = new Car();
		backing = new Array<Gnome>();
		road = new Road(level.getLevelEndY());
		ypos = 0.0f;
		nextEvent = 0;
		sunShine = false;
	}

	/**
	 * Returns the list of the currently active (not destroyed) game objects
	 *
	 * As this method returns a reference and Lists are mutable, other classes can
	 * technical modify this list.  That is a very bad idea.  Other classes should
	 * only mark objects as destroyed and leave list management to this class.
	 *
	 * @return a reference to all the gnomes
	 */
	public Array<Gnome> getGnomez() { return gnomez; }

	/**
	 * Returns a reference to the car.
	 */
	public Car getCar() {
		return yonda;
	}

	/**
	 * Returns a reference to the road.
	 */
	public Road getRoad() { return road; }

  /**
   * Returns a reference to a rearview enemy.
   */
	public RearviewEnemy getRearviewEnemy() { return rearviewEnemy; }

    /**
     * Returns a reference to the wheel.
     */
    public Wheel getWheel(){ return wheel; }

  /**
   * Returns a reference to the vroom stick.
   */
	public VroomStick getVroomStick() { return vroomStick; }

	public Image getHealthGauge() { return healthGauge; }
	public Image getRearviewBackground() { return rearviewBackground; }
	public Image getRearviewSeats() { return rearviewSeats; }
	public Image getRearviewCover() { return rearviewCover; }

	/**
	 * Returns a reference to the radio
	 */
    public Radio getRadio(){ return radio; }

	/**
	 * Returns a reference to the inventory
	 */
	public Inventory getInventory(){ return inventory; }

    /**
     * Returns a reference to the visor
     */
    public Visor getVisor(){ return visor; }

	/**
	 * Starts a new game.
	 *
	 * This method creates a single player, but does nothing else.
	 *
	 * @param x Starting x-position for the player
	 * @param y Starting y-position for the player
	 */
	public void start(float x, float y) {
		hudObjects = new ObjectSet<Image>();
        sunShine = false;
		Inventory.Item.setTexturesAndScales(dvdTexture,0.1f,snackTexture,0.1f);
		yonda.getNosh().setChildFilmStrips(nosh_happy,nosh_neutral,nosh_sad,nosh_critical,nosh_sleep);
		yonda.getNed().setChildFilmStrips(ned_happy,ned_neutral,ned_sad,ned_critical,ned_sleep);
		yonda.setDashTexture(dashTexture);
		getCar().setGaugeTexture(healthGaugeTexture);
		getCar().setGaugePointerTexture(healthPointerTexture);

		healthGauge = new Image(0.35f, 0.023f, 0.175f, healthGaugeTexture);
		rearviewBackground = new Image(0.63f, 0.71f, 0.3f, rearviewBackgroundTexture);
		rearviewSeats = new Image(0.63f, 0.71f, 0.3f, rearviewSeatsTexture);
		rearviewCover = new Image(0.63f, 0.71f, 0.3f, rearviewSeatsTexture);

		for(Gnome g: level.getGnomez()){
			gnomez.add(new Gnome(g));
		}
		// TODO CHANGE THIS LOL
		for (Gnome g : gnomez) {
			g.setTexture(gnomeTexture);
		}
		events = level.getEvents();

		wheel = new Wheel(0.193f,0.22f, 0.5f, 60, wheelTexture);
		vroomStick = new VroomStick(0.193f, 0.2f,0.3f, 0, vroomStickTexture);
		radio = new Radio(0.66f, 0.06f, 0.07f, 0, radioknobTexture, level.getSongs());
		inventory = new Inventory(0.4756f,0.0366f, 0,0,wheelTexture, 0.146f, 0.128f, 2);
		Array<Inventory.Slot> i = new Array<Inventory.Slot>();
		i.add(new Inventory.Slot(i,inventory, Inventory.Item.ItemType.DVD,3));
		i.add(new Inventory.Slot(i,inventory, Inventory.Item.ItemType.SNACK,1));
		inventory.load(i);
		visor = new Visor(visorOpen, visorClosed);

		road.setRoadTexture(roadTexture);
		road.setGrassTexture(grassTexture);
		road.setExitTexture(exitTexture);

		rearviewEnemy = new RearviewEnemy(0.843f, 0.81f, 0.18f,0, rearviewGnomeTexture);

		// Add all HUD objects to hudObjects
		hudObjects.add(vroomStick);
		hudObjects.add(radio);
		hudObjects.add(inventory);
		hudObjects.add(rearviewEnemy);
		hudObjects.add(wheel);
		hudObjects.add(healthGauge);
		hudObjects.add(rearviewBackground);
		hudObjects.add(rearviewCover);
		hudObjects.add(rearviewSeats);

  }

	/**
	 * Resets the game, deleting all objects.
	 */
	public void reset() {
		road.reset();
		rotationMagnitude = 0;
		yonda.reset();
		inventory.reset();
		wheel = null;
		radio = null;
		gnomez = new Array<Gnome>(level.getGnomez().size);
		backing.clear();
		ypos = 0.0f;
		nextEvent = 0;
	}

	/**
	 * Garbage collects all deleted objects.
	 *
	 * This method works on the principle that it is always cheaper to copy live objects
	 * than to delete dead ones.  Deletion restructures the list and is O(n^2) if the
	 * number of deletions is high.  Since Add() is O(1), copying is O(n).
	 */
	public void garbageCollect() {
		// INVARIANT: backing and objects are disjoint
		for (Gnome g : gnomez) {
			if (g.isDestroyed()) {
				destroy(g);
			} else {
				backing.add(g);
			}
		}

		// Swap the backing store and the objects.
		// This is essentially stop-and-copy garbage collection
		Array<Gnome> tmp = backing;
		backing = gnomez;
		gnomez = tmp;
		backing.clear();
	}

	/**
	 * Process specialized destruction functionality
	 *
	 * Some objects do something special (e.g. explode) on destruction. That is handled
	 * in this method.
	 *
	 * Notice that this allocates memory to the heap.  If we were REALLY worried about
	 * performance, we would use a memory pool here.
	 *
	 * @param o Object to destroy
	 */
	protected void destroy(RoadObject o) {
	    // TODO: carry out actions that occur on death of object o
		switch(o.getType()) {
			case CAR:
				break;
			case GNOME:
				break;
			default:
				break;
		}

	}

	/**
	 * Makes the first event in the event queue occur and removes it from the
	 * queue if it should occur at the current time. Does nothing if the first
	 * event in the queue should not occur at this time or if the queue is empty.
	 *
	 * @param delta Number of seconds since the last animation frame
	 * @param ned Ned
	 * @param nosh Nosh
	 */
	public void handleEvents(float delta, Child ned, Child nosh) {
		if (events.size != 0) {
			float dy = road.getSpeed() * delta; // change in y position
			ypos += dy; // add it to y position tracker
			Event first = events.get(nextEvent);

			// give some leeway in position for events to occur
			if (Math.abs(first.getY() - ypos) < 0.1f) {
				switch (first.getType()) {
					case REAR_ENEMY:
						rearviewEnemy.create();
						break;
					case SUN_START:
						sunShine = true;
						break;
					case SUN_END:
						sunShine = false;
						break;
					case NED_WAKES_UP:
						if (!ned.isAwake()) {
							ned.setMood(Child.Mood.NEUTRAL);
							ned.setMoodShifting(true, false);
						}
						break;
					case NOSH_WAKES_UP:
						if (!nosh.isAwake()) {
							nosh.setMood(Child.Mood.NEUTRAL);
							nosh.setMoodShifting(true, false);
						}
						break;
					case SAT_QUESTION:
						// TODO
						break;
					default:
						break;
				}
				nextEvent++;
			}
		}
	}

	/**
	 * Resolve the actions of all game objects
	 *
	 * @param input  Reference to the input controller
	 * @param delta  Number of seconds since last animation frame
	 */
	public void resolveActions(InputController input, float delta) {

//		for(int i=0; i<gnomez.size; i++){
//			System.out.println("gnome["+i+"]: " + gnomez.get(i).getY());
//		}
		// Update world objects (road and gnome positions)
        road.update(delta);
        for (Gnome g : gnomez) {
            g.update(delta, road.getSpeed());
        }

        // Update the HUD
        Vector2 in = input.getClickPos();
        Vector2 dr = new Vector2(input.getDX(), input.getDY());
  		boolean mousePressed = input.isMousePressed();
        if(in != null) {
			wheel.update(new Vector2(in), dr.x);
			vroomStick.update(new Vector2(in), dr.y);
			radio.update(new Vector2(in), dr.x);
			inventory.update(new Vector2(in), mousePressed);
			visor.update(new Vector2(in), input.isPrevMousePressed());
		}
		else{
			wheel.update(null, dr.x);
			vroomStick.update(null, dr.y);
			radio.update(null, dr.x);
			inventory.update(null, mousePressed);
		}
		resolveItemDrop(input);
		rearviewEnemy.update(delta*0.0004f);

		if (vroomStick.isEngaged()) {
			rearviewEnemy.destroyIfAlive();
			road.setVrooming();
		}

		if (rearviewEnemy.isAttackingCar()) {
			getCar().damage();
			if (getCar().getHealth() == 0)
				getCar().setDestroyed(true);
		}

		if(prevClick != null && input.getClickPos() == null) {
			yonda.update(prevClick, wheel, delta);
			prevClick = null;
		} else {
			prevClick = input.getClickPos();
			yonda.update(null, wheel, delta);
		}

		if (road.reachedEndOfLevel()) {
			getCar().takeExit();
		}

		for (Image i : hudObjects) {
			i.updateShake(delta);
		}

	}

	/** TODO: MAKE THIS NOT JANK IM JUST TRYING TO MAKE THE KIDS SLEEP also handles radio
	 *
	 * @param counter
	 * @param ned
	 * @param nosh
	 */
	public void resolveChildren(int counter, Child ned, Child nosh, Radio r) {
		// check radio station and update each child's happiness based on it,
		// checks this every 200 frames (may need to adjust)
		if (r.getCurrentStation() != null && r.getknobAng() <= 0 && counter%200 == 0) {

			// TODO : ADD CASES FOR OTHER GENRES
			switch (r.getCurrentStationGenre()){
				case DANCE: // ned likes, nosh dislikes
					if(ned.isAwake()){
                        ned.setMoodShifting(true, true);
					}
					if(nosh.isAwake()){
                        nosh.setMoodShifting(true, false);
					}
					break;
				case CREEPY: // ned likes, nosh dislikes
					if(ned.isAwake()){
                        ned.setMoodShifting(true, true);
					}
					if(nosh.isAwake()){
						nosh.setMoodShifting(true, false);
					}
					break;
				case JAZZ: // ned likes, nosh dislikes
					if(ned.isAwake()){
						ned.setMoodShifting(true, true);
					}
					if(nosh.isAwake()){
                        nosh.setMoodShifting(true, false);
					}
					break;
				case COMEDY: // ned dislikes, nosh likes
					if(ned.isAwake()){
						ned.setMoodShifting(true, false);
					}
					if(nosh.isAwake()){
						nosh.setMoodShifting(true, true);
					}
					break;
				default:
					break;
			}
//			if (ned.isAwake()) {
//				if (r.getCurrentStationNed()) {
//					ned.setHappy();
//				} else {
//					ned.decreaseHappiness();
//				}
//			}
//
//			// check radio for nosh
//			if (nosh.isAwake()) {
//				if (r.getCurrentStationNosh()) {
//					nosh.setHappy();
//				} else {
//					nosh.decreaseHappiness();
//				}
//			}
		} else if (r.getCurrentStation() == null && counter != 0 && counter % 240 == 0 && ned.isAwake()) {
            ned.setMoodShifting(true, false);
        } else if (r.getCurrentStation() == null && counter != 0 && counter % 115 == 0 && nosh.isAwake()){
            nosh.setMoodShifting(true, false);
        }


        // TODO: eventually remove this random stuff, commenting out for testing
//		Random generator = new Random();
//		float ned_prob = 0.3f;
//		float nosh_prob = 0.3f;
//
//		float rearviewProb = 0.3f;
//
//		// check every 100 frames
//		if (counter % 100 == 0) {
//			// make ned sleepy with given probability
//			if (generator.nextFloat() <= ned_prob) {
////				ned.setAsleep();
//				ned.setMoodShifting(true, false);
//			}
//			// make nosh sleepy with given probability
//			if (generator.nextFloat() <= nosh_prob) {
//				nosh.setAsleep();
//			}
//
//			// Create rearview enemy with given probability
//			if (generator.nextFloat() <= rearviewProb) {
//				rearviewEnemy.create();
//			}
//
//		}

	}

	Vector2 droppedPos;
	public void resolveItemDrop(InputController inputController) {
//		System.out.println("prevClick: "+inputController.isPrevMousePressed());
//		System.out.println("currClick: "+inputController.isMousePressed());
		if (inventory.getItemInHand() != null && inputController.isPrevMousePressed() && !inputController.isMousePressed()) {
			if (yonda.getNosh().inChildArea(droppedPos)) {
				//TODO ITEM CHECKS FOR NOSH
				switch (inventory.getItemInHand().getItemType()) {
					case SNACK:
						yonda.getNosh().setMood(Child.Mood.HAPPY);
						break;
					case DVD:
						if(!yonda.getNosh().isAwake()) {
							inventory.cancelTake();
							break;
						}
						yonda.getNosh().setMood(Child.Mood.SLEEP);
						break;
				}
			} else if (yonda.getNed().inChildArea(droppedPos)) {
				//TODO ITEM CHECKS FOR NED
				switch (inventory.getItemInHand().getItemType()) {
					case SNACK:
						yonda.getNed().setMood(Child.Mood.HAPPY);
						break;
					case DVD:
						if(!yonda.getNed().isAwake()) {
							inventory.cancelTake();
							break;
						}
						yonda.getNed().setMood(Child.Mood.SLEEP);
						break;
				}
			} else if (inventory.inArea(droppedPos)){
				inventory.store(inventory.slotInArea(droppedPos), inventory.getItemInHand());
			} else {
				inventory.cancelTake();
				return;
			}
		inventory.setItemInHand(null);
		}
		droppedPos = inputController.getClickPos();
	}

	public void shakeHUD() {
		for (Image i : hudObjects) {
			i.applyShake();
		}

	}
}