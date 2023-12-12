package com.zlj.utils.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zlj.utils.other.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zlj on 2018/2/11 0011.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 * Json解析工具类
 * 两种方式  ：
 * 1.基于Google的Gson开源库的方式解析
 * 2.基于Android原生自带的JSONObject类的方式做处理
 *
 *  toJson
 *  fromJson
 *  fromDataJson
 *
 *  listToJson
 *
 *
 * @Since 1.0
 */
public final class JSON {
    /**
     * 静态持有类
     */
    private static class GsonHolder {
        private static final Gson GSON = new Gson();
    }

    /**
     * 格式化json字符串
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
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
     * @param theString
     * @return 转化后的结果.
     */
    private static String decodeUnicode(String theString) {
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


    /**
     * 判断字符串是否为json格式
     *
     * @param json
     * @return
     */
    public static boolean isJsonString(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.d("bad json: " + json);
            return false;
        }
    }

    //==========================Gson方式的解析==================================================

    /**
     * 对象转json
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return GsonHolder.GSON.toJson(obj);
    }

    /**
     * json转对象
     * @param str
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        return GsonHolder.GSON.fromJson(str, type);
    }

    /**
     * 解析Json数组成ArrayList
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        Type type = new TypeToken<List<JsonObject>>() {}.getType();
        List<JsonObject> jsonObjects = GsonHolder.GSON.fromJson(json,type);
        List<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(GsonHolder.GSON.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * 解析data 格式json
     *
     * @param json
     * @param classofT
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T fromDataJson(String json, Class<T> classofT) throws JsonSyntaxException {
        return fromDataJson(json,"data",classofT);
    }

    /**
     * 解析data 格式json
     *
     * @param json
     * @param classofT
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T fromDataJson(String json, String dataKey,Class<T> classofT) throws JsonSyntaxException {
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.get(dataKey).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return GsonHolder.GSON.fromJson(data, classofT);
    }

    /**
     * 解析Json数组成ArrayList
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> fromDataJsonList(String json, Class<T> clazz) {
        return fromDataJsonList(json,"data",clazz);
    }

    /**
     * 解析Json数组成ArrayList
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> fromDataJsonList(String json,String dataKey, Class<T> clazz) {
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            data = jsonObject.get(dataKey).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<List<JsonObject>>() {}.getType();
        List<JsonObject> jsonObjects = GsonHolder.GSON.fromJson(data,type);
        List<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(GsonHolder.GSON.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }


    //=============================原生JSONObject的方式解析===============================================

    /**
     * Map转为JSONObject
     * @param data
     * @return
     */
    public static JSONObject fromJSONObject(Map<?, ?> data) {
        JSONObject object = new JSONObject();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            String key = (String) entry.getKey();
            if (key == null) {
                throw new NullPointerException("key == null");
            }
            try {
                object.put(key, wrap(entry.getValue()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return object;
    }

    /**
     * 集合转换为JSONArray
     * @param data
     * @return
     */
    public static JSONArray fromJSONArray(Collection<?> data) {
        JSONArray jsonArray = new JSONArray();
        if (data != null) {
            for (Object aData : data) {
                jsonArray.put(wrap(aData));
            }
        }
        return jsonArray;
    }

    /**
     * Object对象转换为JSONArray
     * @param data
     * @return
     * @throws JSONException
     */
    public static JSONArray fromJSONArray(Object data) throws JSONException {
        if (!data.getClass().isArray()) {
            throw new JSONException("Not a primitive data: " + data.getClass());
        }
        final int length = Array.getLength(data);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < length; ++i) {
            jsonArray.put(wrap(Array.get(data, i)));
        }

        return jsonArray;
    }

    /**
     * 包裹Object对象
     * @param o
     * @return
     */
    private static Object wrap(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return fromJSONArray((Collection<?>) o);
            } else if (o.getClass().isArray()) {
                return fromJSONArray(o);
            }
            if (o instanceof Map) {
                return fromJSONObject((Map<?, ?>) o);
            }

            if (o instanceof Boolean || o instanceof Byte || o instanceof Character || o instanceof Double || o instanceof Float || o instanceof Integer || o instanceof Long
                    || o instanceof Short || o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * json字符串生成JSONObject对象
     * @param json
     * @return
     */
    public static JSONObject fromJSONObject(String json) {
        JSONObject jsonObject = null;
        try {
            JSONTokener jsonParser = new JSONTokener(json);
            jsonObject = (JSONObject) jsonParser.nextValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 对象转换为Json
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        } else if (obj instanceof String || obj instanceof Integer
                || obj instanceof Float || obj instanceof Boolean
                || obj instanceof Short || obj instanceof Double
                || obj instanceof Long || obj instanceof BigDecimal
                || obj instanceof BigInteger || obj instanceof Byte) {
            json.append("\"").append(stringToJson(obj.toString())).append("\"");
        } else if (obj instanceof Object[]) {
            json.append(arrayToJson((Object[]) obj));
        } else if (obj instanceof List) {
            json.append(listToJson((List<?>) obj));
        } else if (obj instanceof Map) {
            json.append(mapToJson((Map<?, ?>) obj));
        } else if (obj instanceof Set) {
            json.append(setToJson((Set<?>) obj));
        }
        return json.toString();
    }

    /**
     * List集合转换为Json
     * @param list
     * @return
     */
    public static String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(objectToJson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * 对象数组转换为Json
     * @param array
     * @return
     */
    public static String arrayToJson(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(objectToJson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * Map集合转换为Json
     * @param map
     * @return
     */
    public static String mapToJson(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(objectToJson(key));
                json.append(":");
                json.append(objectToJson(map.get(key)));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    /**
     * Set集合转为Json
     * @param set
     * @return
     */
    public static String setToJson(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(objectToJson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * 字符串转换为Json
     * @param s
     * @return
     */
    public static String stringToJson(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if (ch >= '\u0000' && ch <= '\u001F') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }

}
