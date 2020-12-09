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
    // init all the String, JSONObj, LinkList and other stuff
    private String gameFilePath;
    private JSONObject gameDataObject;
    private String gameName;
    private JSONObject environmentVariables;
    private JSONObject gameStages;
    private String currentStageName;

    private String currentStageText;
    private String currentStageStatusText;
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
            currentStageStatusText = interpetingComponent(gameDataObject.getJSONObject("GameStatusLine"));
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
                currentStageText += interpetingComponent(component);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String interpetingComponent(JSONObject component)throws JSONException{
        String textAccumulator = "";
        switch (component.getString("type")) {
            case "text":
                textAccumulator+=component.getString("content");
                break;
            case "value":
                JSONObject valueToBeDisplay = interpetingValue(component.getJSONObject("value"));
                switch (valueToBeDisplay.getString("type")) {
                    case "bool":
                        textAccumulator+=(valueToBeDisplay.getBoolean("value"))?"Yes":"No";
                        break;
                    case "int":
                        textAccumulator+=valueToBeDisplay.getInt("value");
                        break;
                    default:
                        throw new RuntimeException("TKJL2980-Unexpected error.");
                }
                break;
            case "currentStageName":
                textAccumulator+=currentStageName;
                break;
            case "components":
                JSONArray subComponents = component.getJSONArray("contents");
                for (int i = 0; i<subComponents.length();i++){
                    textAccumulator+=interpetingComponent(subComponents.getJSONObject(i));
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
                if(interpetingCondition(component.getJSONObject("condition"))){
                    textAccumulator+=interpetingComponent(component.getJSONObject("component-true"));
                } else {
                    textAccumulator+=interpetingComponent(component.getJSONObject("component-false"));
                }
                break;
            case "random":
                int numAvilable = component.getJSONArray("contents").length();
                Random r = new Random();
                int index = r.nextInt(numAvilable);
                textAccumulator+=interpetingComponent(component.getJSONArray("contents").getJSONObject(index));
                break;
            case "command-set":
                JSONObject value = interpetingValue(component.getJSONObject("value"));
                switch (value.getString("type")) {
                    case "bool":
                        environmentVariables.getJSONObject(component.getString("variable")).remove("value");
                        environmentVariables.getJSONObject(component.getString("variable")).put("value",value.getBoolean("value"));
                        break;
                    case "int":
                        environmentVariables.getJSONObject(component.getString("variable")).remove("value");
                        environmentVariables.getJSONObject(component.getString("variable")).put("value",value.getInt("value"));
                        break;
                    default:
                        throw new RuntimeException("STKR6611-Game Data File Cannot be interpeted!");
                }
                break;
            default:
                throw new RuntimeException("ZJOT6881-Game Data File Cannot be interpeted!");
        }
        return textAccumulator;
    }


    private boolean interpetingCondition(JSONObject conditionObject)throws JSONException{
        int value1, value2;
        switch (conditionObject.getString("type")) {
            case "bool":
                JSONObject variable = interpetingValue(conditionObject.getJSONObject("value"));
                if(variable.getString("type").equals("bool")){
                    return variable.getBoolean("value");
                } else {
                    throw new RuntimeException("CKXK9332-Condition Variable not bool");
                }
            case "not":
                return !interpetingCondition(conditionObject.getJSONObject("condition"));
            case "and":
                JSONArray arr = conditionObject.getJSONArray("conditions");
                for (int i = 0; i<arr.length(); i++){
                    if (!interpetingCondition(arr.getJSONObject(i))) {
                        return false;
                    }
                }
                return true;
            case "or":
                JSONArray arr2 = conditionObject.getJSONArray("conditions");
                for (int i = 0; i<arr2.length(); i++){
                    if (interpetingCondition(arr2.getJSONObject(i))) {
                        return true;
                    }
                }
                return false;
            case "int-greater":
                value1 = interpetingValue(conditionObject.getJSONObject("value1")).getInt("value");
                value2 = interpetingValue(conditionObject.getJSONObject("value2")).getInt("value");
                return value1>value2;
            case "int-smaller":
                value1 = interpetingValue(conditionObject.getJSONObject("value1")).getInt("value");
                value2 = interpetingValue(conditionObject.getJSONObject("value2")).getInt("value");
                return value1<value2;
            case "int-equal":
                value1 = interpetingValue(conditionObject.getJSONObject("value1")).getInt("value");
                value2 = interpetingValue(conditionObject.getJSONObject("value2")).getInt("value");
                return value1==value2;
            default:
                throw new RuntimeException("UOFG9736-Game Data File Cannot be interpeted!");
        }
    }

    private JSONObject interpetingValue(JSONObject valueObject)throws JSONException{
        switch (valueObject.getString("type")) {
            case "bool":
            case "int":
                return valueObject;
            case "variable":
                return environmentVariables.getJSONObject(valueObject.getString("variableName"));
            case "calculation":
                JSONArray array = valueObject.getJSONArray("operands");
                for (int i = 0; i<array.length(); i++) {
                    JSONObject value = array.getJSONObject(i);
                    if (!interpetingValue(value).getString("type").equals("int")) {
                        throw new RuntimeException("UDCO9314-Operands type invalid");
                    }
                }
                int accumulator = interpetingValue(valueObject.getJSONArray("operands").getJSONObject(0)).getInt("value");
                switch (valueObject.getString("operation")) {
                    case "add":
                        for (int i = 1; i<array.length(); i++) {
                            accumulator += interpetingValue(valueObject.getJSONArray("operands").getJSONObject(i)).getInt("value");
                        }
                        break;
                    case "minus":
                        for (int i = 1; i<array.length(); i++) {
                            accumulator -= interpetingValue(valueObject.getJSONArray("operands").getJSONObject(i)).getInt("value");
                        }
                        break;
                    case "multiple":
                        for (int i = 1; i<array.length(); i++) {
                            accumulator *= interpetingValue(valueObject.getJSONArray("operands").getJSONObject(i)).getInt("value");
                        }
                        break;
                    case "divide":
                        for (int i = 1; i<array.length(); i++) {
                            accumulator /= interpetingValue(valueObject.getJSONArray("operands").getJSONObject(i)).getInt("value");
                        }
                        break;
                    default:
                        throw new RuntimeException("DUEA0778-Operation "+valueObject.getString("operation")+" invalid");
                }
                return new JSONObject("{\"type\": \"int\", \"value\": "+accumulator+"}");
            default:
                throw new RuntimeException("STKR6611-Game Data File - Value Cannot be interpeted!");
        }
    }

    public String getCurrentStageText(){
        return currentStageText;
    }
    public String getCurrentStatusText(){
        return currentStageStatusText;

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



