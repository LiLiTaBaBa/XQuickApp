package com.zlj.kxapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangLiJun on 2018/9/17.
 *
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 */
public class JsonUtil {
    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    private static Gson gson;

    private static void initOrCheck() {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd").create();
            //gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }
    }

    public static String Gson2Json(Object src) {
        initOrCheck();
        return gson.toJson(src);
    }

    /**
     * Json对象转Java对象
     *
     * @param json
     * @param type Example.class
     * @return
     */
    public static <T> T getBeanFromJson(String json, Type type) {
        initOrCheck();
        return gson.fromJson(json, type);
    }

    public static <T> T getBeanFromJson(String json, Class<T> type) {
        initOrCheck();
        return gson.fromJson(json, type);
    }

    public static <T> T getBeanFromJson(String json, String datePattern, Class<T> type) {
        // initOrCheck();
        gson = new GsonBuilder().setPrettyPrinting().setDateFormat(datePattern).create();
        return gson.fromJson(json, type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBeanFromJson(String json) {
        return getBeanFromJson(json, (Class<T>) JsonObject.class);
    }

    public static <T> JsonObject getJsonObjectFromBean(T bean) {
        return JsonUtil.getBeanFromJson(JsonUtil.getJsonFromBean(bean), JsonObject.class);
    }

    public static <T> JsonArray getJsonArrayFromArray(List<T> list) {
        return JsonUtil.getBeanFromJson(JsonUtil.getJsonFromArray(list), JsonArray.class);
    }

    /**
     * Json数组转Java数组（列表）
     *
     * @param json
     * @param type 数组元素的类
     * @return
     */
    public static <T> List<T> getArrayFromJson(String json, Type type) {
        initOrCheck();
        if (TextUtils.isEmpty(json)) json = "[]";
        List<JsonObject> jsonObjects = gson.fromJson(json, new TypeToken<List<JsonObject>>() {
        }.getType());
        List<T> lists = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            lists.add(gson.fromJson(jsonObject, type));
        }
        return lists;
    }

    public static <T> List<T> getArrayFromJson(String json, Class<T> type) {
        return getArrayFromJson(json, (Type) type);
    }

    public static <T> List<T> getArrayFromJson(String json, TypeToken type) {
        initOrCheck();
        return gson.fromJson(json, type.getType());
    }

   /* public static <T> List<T> getArrayFromAssets(Context context, String path, Type type) {
        return getArrayFromJson(FileHelper.getAssetsAsString(context, path), type);
    }*/

    public static JSONObject getJSONOFromJson(String s) {
        JSONObject jo = new JSONObject();
        try {
            if (s != null && s.startsWith("\ufeff")) {
                s = s.substring(1);
            }
            jo = new JSONObject(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jo;
    }

    public static String getFromJO(JSONObject jo, String s) {
        String str = "";
        try {
            str = jo.get(s).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Java对象转Json
     *
     * @param bean 对象实例
     * @return
     */
    public static <T> String getJsonFromBean(T bean) {
        initOrCheck();
        return gson.toJson(bean);
    }

    /**
     * Java对象转Json
     *
     * @param bean 对象实例
     * @return
     */
    public static <T> String getJsonFromBean(T bean, String pattern) {
        gson = new GsonBuilder().setPrettyPrinting().setDateFormat(pattern).create();
        return gson.toJson(bean);
    }


    /**
     * Java List转Json数组
     *
     * @param list List实例
     * @return
     */
    public static <T> String getJsonFromArray(List<T> list) {
        initOrCheck();
        return gson.toJson(list);
    }

    public static <F, T> T lowCopy(F source, Class<T> targetClass) {
        return getBeanFromJson(getJsonFromBean(source), targetClass);
    }

    public static class Merging {
        public static enum ConflictStrategy {

            THROW_EXCEPTION, PREFER_FIRST_OBJ, PREFER_SECOND_OBJ, PREFER_NON_NULL;
        }

        public static class JsonObjectExtensionConflictException extends Exception {

            public JsonObjectExtensionConflictException(String message) {
                super(message);
            }

        }

        public static void extendJsonObject(JsonObject destinationObject, ConflictStrategy conflictResolutionStrategy, JsonObject... objs)
                throws JsonObjectExtensionConflictException {
            for (JsonObject obj : objs) {
                extendJsonObject(destinationObject, obj, conflictResolutionStrategy);
            }
        }

        public static void extendJsonObject(JsonObject destinationObject, JsonObject... objs) {
            for (JsonObject obj : objs) {
                try {
                    extendJsonObject(destinationObject, obj, ConflictStrategy.PREFER_NON_NULL);
                } catch (JsonObjectExtensionConflictException e) {
                    e.printStackTrace();
                }
            }
        }

        private static void extendJsonObject(JsonObject leftObj, JsonObject rightObj, ConflictStrategy conflictStrategy)
                throws JsonObjectExtensionConflictException {
            for (Map.Entry<String, JsonElement> rightEntry : rightObj.entrySet()) {
                String rightKey = rightEntry.getKey();
                JsonElement rightVal = rightEntry.getValue();
                if (leftObj.has(rightKey)) {
                    //conflict
                    JsonElement leftVal = leftObj.get(rightKey);
                    if (leftVal.isJsonArray() && rightVal.isJsonArray()) {
                        JsonArray leftArr = leftVal.getAsJsonArray();
                        JsonArray rightArr = rightVal.getAsJsonArray();
                        //concat the arrays -- there cannot be a conflict in an array, it's just a collection of stuff
                        for (int i = 0; i < rightArr.size(); i++) {
                            leftArr.add(rightArr.get(i));
                        }
                    } else if (leftVal.isJsonObject() && rightVal.isJsonObject()) {
                        //recursive merging
                        extendJsonObject(leftVal.getAsJsonObject(), rightVal.getAsJsonObject(), conflictStrategy);
                    } else {//not both arrays or objects, normal merge with conflict resolution
                        handleMergeConflict(rightKey, leftObj, leftVal, rightVal, conflictStrategy);
                    }
                } else {//no conflict, add to the object
                    leftObj.add(rightKey, rightVal);
                }
            }
        }

        private static void handleMergeConflict(String key, JsonObject leftObj, JsonElement leftVal, JsonElement rightVal, ConflictStrategy conflictStrategy)
                throws JsonObjectExtensionConflictException {
            {
                switch (conflictStrategy) {
                    case PREFER_FIRST_OBJ:
                        break;//do nothing, the right val gets thrown out
                    case PREFER_SECOND_OBJ:
                        leftObj.add(key, rightVal);//right side auto-wins, replace left val with its val
                        break;
                    case PREFER_NON_NULL:
                        //check if right side is not null, and left side is null, in which case we use the right val

                        if (leftVal.isJsonNull() && !rightVal.isJsonNull()) {
                            leftObj.add(key, rightVal);
                        } else if (!leftVal.isJsonNull() && !rightVal.isJsonNull()) {
                            try {
                                if (leftVal.getAsInt() == 0 && leftVal.getAsInt() != 0) {
                                    leftObj.add(key, rightVal);
                                }
                            } catch (NumberFormatException ne) {
                                if (leftVal.getAsString().equals("") && !leftVal.getAsString().equals("")) {
                                    leftObj.add(key, rightVal);
                                }
                            }
                        }
                        //else do nothing since either the left value is non-null or the right value is null
                        break;
                    case THROW_EXCEPTION:
                        throw new JsonObjectExtensionConflictException("Key " + key + " exists in both objects and the conflict resolution strategy is " + conflictStrategy);
                    default:
                        throw new UnsupportedOperationException("The conflict strategy " + conflictStrategy + " is unknown and cannot be processed");
                }
            }
        }
    }

    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();

        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();

        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }

        return arrayList;
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}

