package hundun.gdxgame.idlemushroom.html.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.freetype.gwt.FreetypeInjector;
import com.badlogic.gdx.graphics.g2d.freetype.gwt.inject.OnCompletion;

import hundun.gdxgame.idlemushroom.IdleMushroomGame;
import hundun.gdxgame.idlemushroom.html.GwtPreferencesSaveTool;

public class HtmlLauncher extends GwtApplication {

    private final IdleMushroomGame game;
    
    public HtmlLauncher() {
        game = new IdleMushroomGame(new GwtPreferencesSaveTool("IdleMushroomGame-html-save"));
    }
    
    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(game.getWidth(), game.getHeight());
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return game;
    }
    
    @Override
    public void onModuleLoad () {
        FreetypeInjector.inject(new OnCompletion() {
            public void run () {
                // Replace HtmlLauncher with the class name
                // If your class is called FooBar.java than the line should be FooBar.super.onModuleLoad();
                HtmlLauncher.super.onModuleLoad();
            }
        });
    }
}