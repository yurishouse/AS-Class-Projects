package moe.yuris.AdventureOfLife;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileSystemException;
import java.util.LinkedList;
import java.util.Random;

public class Game {
    private String gameFilePath;
    private JSONObject gameDataObject;
    private String gameName;
    private JSONObject environmentVariables;
    private JSONObject gameStages;
    private String currentStageName;

    private String currentStageText;
    private LinkedList<String> currentStageChoices;
    private LinkedList<CallBack> currentStageChoicesResults;

    public Game(String path) throws FileSystemException, JSONException {
        this.gameFilePath = path;
        this.gameDataObject = readFromFile(path);
        currentStageChoices = new LinkedList<String>();
        currentStageChoicesResults = new LinkedList<CallBack>();
        interpretingJSONAndInitGlobalVariables();

    }

    /**
     * Intepreting JSON and load into global variables
     * only called when initializing
     */
    private void interpretingJSONAndInitGlobalVariables()throws JSONException{
        gameName = gameDataObject.getString("GameName");
        environmentVariables = gameDataObject.getJSONObject("GameEnvironmentVariables");
        gameStages = gameDataObject.getJSONObject("GameStages");
        currentStageName = gameDataObject.getString("GameInitStage");
        onStageUpdate();
    }
    private void onStageUpdate() {
        try {
            interpretingStageTextAndLoad(gameStages.getJSONObject(currentStageName).getJSONArray("StageComponents"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void interpretingStageTextAndLoad(JSONArray stageComponents){
        currentStageText="";
        currentStageChoices.clear();
        currentStageChoicesResults.clear();
        for (int i = 0; i<stageComponents.length(); i++) {
            JSONObject component = null;
            try {
                component = stageComponents.getJSONObject(i);
                interpetingComponent(component);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void interpetingComponent(JSONObject component)throws JSONException{
        switch (component.getString("type")) {
            case "text":
                currentStageText+=component.getString("content");
                break;
            case "components":
                JSONArray subComponents = component.getJSONArray("contents");
                for (int i = 0; i<subComponents.length();i++){
                    interpetingComponent(subComponents.getJSONObject(i));
                }
                break;
            case "choice-link":
                currentStageChoices.add(component.getString("content"));
                currentStageChoicesResults.add(new CallBack(){
                    @Override
                    public void exec(){
                        try {
                            currentStageName = component.getString("action");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onStageUpdate();
                    }
                });
                break;
            case "conditional":
                switch (component.getJSONObject("condition").getString("type")) {
                    case "bool":
                        JSONObject variable = environmentVariables.getJSONObject(component.getJSONObject("condition").getString("varname"));
                        if(variable.getString("type")=="bool"){
                            if(variable.getBoolean("value")){
                                interpetingComponent(component.getJSONObject("component-true"));
                            } else {
                                interpetingComponent(component.getJSONObject("component-false"));
                            }
                        } else {
                            break;
                        }
                        break;
                    default:
                        throw new RuntimeException("UOFG9736-Game Data File Cannot be interpeted!");
                }
                break;
            case "random":
                int numAvilable = component.getJSONArray("contents").length();
                Random r = new Random();
                int index = r.nextInt(numAvilable);
                interpetingComponent(component.getJSONArray("contents").getJSONObject(index));
                break;
            case "command-set":
                switch (component.getJSONObject("value").getString("type")) {
                    case "bool":
                        environmentVariables.getJSONObject(component.getString("variable")).remove("value");
                        environmentVariables.getJSONObject(component.getString("variable")).put("value",component.getJSONObject("value").getString("value"));
                        break;
                    default:
                        throw new RuntimeException("STKR6611-Game Data File Cannot be interpeted!");
                }
                break;
            default:
                throw new RuntimeException("ZJOT6881-Game Data File Cannot be interpeted!");
        }
    }

    public String getCurrentStageText(){
        return currentStageText;
    }
    public LinkedList<String> getCurrentStageChoices(){
        return currentStageChoices;
    }
    public void chooseAndAdvance(int index){
        currentStageChoicesResults.get(index).exec();
    }

    public static JSONObject readFromFile(String json) throws JSONException {
        //TODO
        //return new JSONObject("{\"GameName\":\"AOL\",\"GameEnvironmentVariables\":{\"variable1\": {\"type\": \"int\", \"value\": 1}},\"GameInitStage\": \"Start\",\"GameStages\":{\"Start\":{\"StageComponents\": [{\"type\":\"text\", \"content\": \"You received a mail, it is:\"}]}}}");
        return new JSONObject(json);
    }

}



