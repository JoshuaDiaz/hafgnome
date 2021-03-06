package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class NormalLevelController extends GameplayController {

    public NormalLevelController(String levelName, GameCanvas canvas, LevelObject level, SoundController sc) {
        super(levelName, level.getRegion(), canvas, level.getLevelEndY(), level.getEnemiez(), level.getEvents(),
                level.getSongs(), sc, level.getRoadsideObjs());
        getRoad().setSpeedFactor(level.getSpeed());
    }

    public void start(float x, float y) {
        super.start(x, y);
    }

    public void resolveActions(InputController input, float delta) {
        super.resolveActions(input, delta);
    }

    public void preLoadContent(AssetManager manager, Array<String> assets) {
        super.preLoadContent(manager, assets);
    }

    public void loadContent(AssetManager manager) {
        super.loadContent(manager);
    }

}
