package hundun.gdxgame.idlemushroom.desktop;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import hundun.gdxgame.corelib.base.save.AbstractSaveDataSaveTool;
import hundun.gdxgame.idlemushroom.logic.RootSaveData;


/**
 * @author hundun
 * Created on 2021/11/10
 */
public class PreferencesSaveTool extends AbstractSaveDataSaveTool<RootSaveData> {
    
    private final ObjectMapper objectMapper;
    
    public PreferencesSaveTool(String preferencesName) {
        super(preferencesName);
        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                ;
        
    }


    @Override
    public void writeRootSaveData(RootSaveData saveData) {
        try {
            preferences.putString(ROOT_KEY, objectMapper.writeValueAsString(saveData));
            preferences.flush();
            Gdx.app.log(getClass().getSimpleName(), "save() done");
        } catch (Exception e) {
            Gdx.app.error(getClass().getSimpleName(), "save() error", e);
        }
    }



    @Override
    public RootSaveData readRootSaveData() {
        try {
            String date = preferences.getString(ROOT_KEY);
            RootSaveData saveData = objectMapper.readValue(date, RootSaveData.class);
            return saveData;
        } catch (IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "load() error", e);
            return null;
        }
    }
}
