package hundun.gdxgame.idlemushroom.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.IdleMushroomGame;
import hundun.gdxgame.idlemushroom.logic.DemoScreenId;
import hundun.gdxgame.idlemushroom.logic.RootSaveData;
import hundun.gdxgame.idleshare.core.framework.BaseIdleGame;
import hundun.gdxgame.idleshare.core.starter.ui.screen.menu.BaseIdleMenuScreen;
import hundun.gdxgame.idleshare.core.starter.ui.screen.play.BaseIdleScreen;
import hundun.gdxgame.idleshare.gamelib.framework.util.text.Language;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hundun
 * Created on 2023/02/16
 */
public class DemoMenuScreen extends BaseHundunScreen<IdleMushroomGame, RootSaveData> {

    int BUTTON_WIDTH = 100;
    int BUTTON_BIG_HEIGHT = 100;
    int BUTTON_SMALL_HEIGHT = 75;

    final InputListener buttonContinueGameInputListener;
    final InputListener buttonNewGameInputListener;

    Label titleLabel;
    Image backImage;
    TextButton buttonContinueGame;
    TextButton buttonNewGame;
    TextButton buttonIntoSettingScreen;
    LanguageSwitchBoard<IdleMushroomGame, RootSaveData> languageSwitchBoardVM;

    public DemoMenuScreen(IdleMushroomGame game) {
        super(game, game.getSharedViewport());

        this.buttonContinueGameInputListener = new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.getSaveHandler().gameplayLoadOrStarter(true);
                game.getScreenManager().pushScreen(DemoScreenId.SCREEN_MAIN, null);
                game.getAudioPlayManager().intoScreen(DemoScreenId.SCREEN_MAIN);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        };
        this.buttonNewGameInputListener = new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.getSaveHandler().gameplayLoadOrStarter(false);
                game.getScreenManager().pushScreen(DemoScreenId.SCREEN_MAIN, null);
                game.getAudioPlayManager().intoScreen(DemoScreenId.SCREEN_MAIN);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        };


    }

    private void initScene2d() {
        List<String> memuScreenTexts = game.getIdleGameplayExport().getGameDictionary().getMenuScreenTexts(game.getIdleGameplayExport().getLanguage());
        this.titleLabel = new Label(
                JavaFeatureForGwt.stringFormat("     %s     ", memuScreenTexts.get(0)),
                game.getMainSkin());
        titleLabel.setFontScale(1.5f);
        this.backImage = new Image(game.getTextureManager().getMenuTexture());
        backImage.setFillParent(true);

        buttonContinueGame = new TextButton(memuScreenTexts.get(2), game.getMainSkin());
        buttonContinueGame.addListener(buttonContinueGameInputListener);

        buttonNewGame = new TextButton(memuScreenTexts.get(1), game.getMainSkin());
        buttonNewGame.addListener(buttonNewGameInputListener);


        backUiStage.clear();
        uiRootTable.clear();

        backUiStage.addActor(backImage);

        uiRootTable.add(titleLabel)
                .row();

        if (game.getSaveHandler().hasContinuedGameplaySave()) {
            uiRootTable.add(buttonContinueGame)
                    .height(BUTTON_BIG_HEIGHT)
                    .fillY()
                    .padTop(10)
                    .row();
        }

        uiRootTable.add(buttonNewGame)
                .height(game.getSaveHandler().hasContinuedGameplaySave() ? BUTTON_SMALL_HEIGHT : BUTTON_BIG_HEIGHT)
                .fillY()
                .padTop(10)
                .row();

        this.languageSwitchBoardVM = new LanguageSwitchBoard<>(
                this,
                Language.values(),
                game.getIdleGameplayExport().getLanguage(),
                memuScreenTexts.get(3),
                memuScreenTexts.get(4),
                it -> {
                    game.getIdleGameplayExport().setLanguage(it);
                    game.getSaveHandler().gameSaveCurrent();
                }
        );
        uiRootTable.add(languageSwitchBoardVM)
                .padTop(10);

        if (game.debugMode) {
            uiRootTable.debugAll();
        }

    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);

        initScene2d();
    }

    @Override
    public void dispose() {}

    @Override
    protected void create() {

    }

    public static class LanguageSwitchBoard<T_GAME extends BaseIdleGame<T_SAVE>, T_SAVE> extends Table {

        BaseHundunScreen<T_GAME, T_SAVE> parent;
        @Getter
        SelectBox<String> selectBox;
        Label restartHintLabel;
        private final Map<Language, String> languageShowNameMap;

        LanguageSwitchBoard(BaseHundunScreen<T_GAME, T_SAVE> parent,
                            Language[] values,
                            Language current,
                            String startText,
                            String hintText,
                            Consumer<Language> onSelect
        ) {
            this.parent = parent;
            this.setBackground(DrawableFactory.getSimpleBoardBackground());
            this.languageShowNameMap = parent.getGame().getIdleGameplayExport().getGameDictionary().getLanguageShowNameMap();

            this.add(new Label(startText, parent.getGame().getMainSkin()));


            this.selectBox = new SelectBox<>(parent.getGame().getMainSkin());
            selectBox.setItems(Stream.of(values)
                    .map(it -> languageShowNameMap.get(it))
                    .collect(Collectors.toList())
                    .toArray(new String[] {})
            );
            selectBox.setSelected(languageShowNameMap.get(current));
            selectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    restartHintLabel.setVisible(true);
                    Language language = languageShowNameMap.entrySet().stream()
                            .filter(x -> x.getValue().equals(selectBox.getSelected()))
                            .findFirst()
                            .get().getKey();
                    onSelect.accept(language);
                }
            });
            this.add(selectBox).row();

            this.restartHintLabel = new Label(hintText, parent.getGame().getMainSkin());
            restartHintLabel.setVisible(false);
            this.add(restartHintLabel);


        }



    }
}
