package com.jotangi.baseutils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonHelper {
    protected final String TAG = this.getClass().getSimpleName();
    protected Context context;
    protected Logger logger;

    public JsonHelper(Context context, Logger logger) {
        this.context = context;
        this.logger = logger;
    }

    public Gson initGson(boolean pretty) {
        if (pretty) {
           return new GsonBuilder()
                   .excludeFieldsWithoutExposeAnnotation()
                   .setDateFormat("yyyy-MM-dd HH:mm:ss")
                   .setPrettyPrinting()
                   .create();
        } else {
            return new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
        }
    }

    public InputStream loadInputStreamFromAssetFile(Context context, String fileName){
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(fileName);
            return is;
        } catch (IOException ex) {
            ex.printStackTrace();
            writeLog(Logger.LEVEL_ERROR, "loadInputStreamFromAssetFile(), ex="+ex.toString());
        }
        return null;
    }

    public JSONObject loadJsonFromAssetFile(Context context, String path){
        JSONObject jo = null;
        try {
            InputStream is = loadInputStreamFromAssetFile(context, path);
            String data = loadContentFromInputStream(is);
            closeInputStream(is);
            jo = new JSONObject(data);
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "loadJsonFromAssetFile(), ex="+ex.toString());
            return null;
        }
        return jo;
    }

    public JSONArray loadJsonArrayFromAssetFile(Context context, String path){
        JSONArray ja = null;
        try {
            InputStream is = loadInputStreamFromAssetFile(context, path);
            String data = loadContentFromInputStream(is);
            closeInputStream(is);
            ja = new JSONArray(data);
            return ja;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "loadJsonArrayFromAssetFile(), ex="+ex.toString());
            return null;
        }
    }

    public String loadContentFromInputStream(InputStream is) {
        String content = null;
        try {
            if (is != null) {
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                content = new String(buffer, "UTF-8");
            }
        } catch (IOException ex) {
            writeLog(Logger.LEVEL_ERROR, "loadContentFromInputStream(), ex="+ex.toString());
            return null;
        }
        return content;
    }

    public JSONObject loadJsonFromInputStream(InputStream is) {
        try {
            String data = loadContentFromInputStream(is);
            JSONObject jo = new JSONObject(data);
            return jo;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "loadJsonFromInputStream(), ex="+ex.toString());
        }
        return null;
    }

    public JSONObject loadJsonFromFile(File file) {
        if (!file.exists()) return null;

        try {
            FileInputStream fis = new FileInputStream(file);
            JSONObject jo = loadJsonFromInputStream(fis);
            closeInputStream(fis);
            return jo;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "loadJsonFromFile(), ex="+ex.toString());
        }
        return null;
    }

    public JSONArray loadJsonArrayFromInputStream(InputStream is) {
        try {
            String data = loadContentFromInputStream(is);
            JSONArray ja = new JSONArray(data);
            return ja;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "loadJsonArrayFromInputStream(), ex="+ex.toString());
        }
        return null;
    }

    public JSONArray loadJsonArrayFromFile(File file) {
        if (!file.exists()) return null;

        try {
            FileInputStream fis = new FileInputStream(file);
            JSONArray ja = loadJsonArrayFromInputStream(fis);
            closeInputStream(fis);
            return ja;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "loadJsonArrayFromFile(), ex="+ex.toString());
        }
        return null;
    }

    public void closeInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception ex) {
                writeLog(Logger.LEVEL_ERROR, "closeInputStream(), ex="+ex.toString());
            }
        }
    }

    public void closeInputStreamReader(InputStreamReader isr) {
        if (isr != null) {
            try {
                isr.close();
            } catch (Exception ex) {
                writeLog(Logger.LEVEL_ERROR, "closeInputStreamReader(), ex="+ex.toString());
            }
        }
    }

    public boolean writeStringToFile(String data, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
            return true;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "writeStringToFile(), ex="+ex.toString());
        }
        return false;
    }

    public boolean writeStringArrayToJsonFile(String[] datas, File file) {
        try {
            JSONArray ja = new JSONArray();
            for (String s : datas) {
                ja.put(s);
            }
            writeStringToFile(ja.toString(), file);
            return true;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "writeStringArrayToJsonFile(), ex="+ex.toString());
            return false;
        }
    }

    public boolean writeStringArrayToJsonFile(List<String> datas, File file) {
        try {
            JSONArray ja = new JSONArray();
            for (String s : datas) {
                ja.put(s);
            }
            writeStringToFile(ja.toString(), file);
            return true;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "writeStringArrayToJsonFile(), ex="+ex.toString());
            return false;
        }
    }

    public List<String> readStringListFromJsonArray(JSONArray ja) {
        String[] ar = readStringArrayFromJsonArray(ja);
        if (ar != null) {
            return Arrays.asList(ar);
        }
        return null;
    }

    public String[] readStringArrayFromJsonArray(JSONArray ja) {
        if (ja != null) {
            try {
                if (ja.length() > 0) {
                    String[] ar = new String[ja.length()];
                        for (int i = 0; i < ja.length(); i++) {
                            ar[i] = ja.get(i).toString();
                        }
                    return ar;
                }
            } catch (Exception ex) {
                writeLog(Logger.LEVEL_ERROR, "readStringListFromJsonArray(), ex=" + ex.toString());
            }
        }
        return null;
    }

    public List<String> readStringListFromJsonString(String data) {
        try {
            JSONArray ja = new JSONArray(data);
            return readStringListFromJsonArray(ja);
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "readStringListFromJsonString(), ex="+ex.toString());
        }
        return null;
    }

    public List<String> readStringListFromJsonFile(File file) {
        JSONArray ja = loadJsonArrayFromFile(file);
        return readStringListFromJsonArray(ja);
    }

    public String stringListToJsonString(List<String> data, boolean pretty) {
        return objectToJsonString(data, pretty);
    }

    public List<Integer> readIntegerListFromJsonArray(JSONArray ja) {
        if (ja != null) {
            try {
                List<Integer> list = new ArrayList<>();
                if (ja.length() > 0) {
                    for (int i = 0; i < ja.length(); i++) {
                        String s = ja.get(i).toString();
                        list.add(Integer.parseInt(s));
                    }
                }
                return list;
            } catch (Exception ex) {
                writeLog(Logger.LEVEL_ERROR, "readIntegerListFromJsonArray(), ex=" + ex.toString());
            }
        }
        return null;
    }

    public int[] readIntegerArrayFromJsonArray(JSONArray ja) {
        if (ja != null) {
            try {
                if (ja.length() > 0) {
                    int[] ar = new int[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        String s = ja.get(i).toString();
                        ar[i] = Integer.parseInt(s);
                    }
                    return ar;
                }
            } catch (Exception ex) {
                writeLog(Logger.LEVEL_ERROR, "readIntegerArrayFromJsonArray(), ex=" + ex.toString());
            }
        }
        return null;
    }

    public Map<String, String> readMapStringStringFromJson(JSONObject jo) {
        try {
            if (jo != null) {
                Map<String, String> map = new ConcurrentHashMap<>();
                Iterator<String> iterator = jo.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String vallue = jo.getString(key);
                    map.put(key, vallue);
                }
                return map;
            }
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "readMapStringStringFromJson() , ex="+ex.toString());
        }
        return null;
    }

    public Map<String, String> readMapStringStringFromJson(String data) {
        try {
            JSONObject jo = new JSONObject(data);
            return readMapStringStringFromJson(jo);
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "readMapStringStringFromJson, ex="+ex.toString());
        }
        return null;
    }

    public Map<String, Integer> readMapStringIntegerFromJson(JSONObject jo) {
        try {
            Map<String, Integer> map = new ConcurrentHashMap<>();
            Iterator<String> iterator = jo.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                int vallue = jo.getInt(key);
                map.put(key, vallue);
            }
            return map;
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "readMapStringIntegerFromJson() , ex="+ex.toString());
            return null;
        }
    }

    public String mapStringIntegerToJsonString(Map<String, Integer> map, boolean pretty) {
        return objectToJsonString(map, pretty);
    }

    public boolean writeMapStringIntegerToFile(Map<String, Integer> map, File file) {
        String data = mapStringIntegerToJsonString(map, true);
        if (data != null) {
            return writeStringToFile(data, file);
        }
        return false;
    }

    public boolean writeObjectToJsonFile(Object obj, File file) {
        try {
            String data = objectToJsonString(obj, true);
            return writeStringToFile(data, file);
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "writeObjectToJsonFile(), ex="+ex.toString());
        }
        return false;
    }

    public String objectToJsonString(Object data, boolean pretty) {
        try {
            Gson gson = initGson(pretty);
            return gson.toJson(data);
        } catch (Exception ex) {
            writeLog(Logger.LEVEL_ERROR, "objectToJsonString(), ex="+ex.toString());
            ex.printStackTrace();
            return null;
        }
    }

    public boolean hasKey(JSONObject jo, String key) {
        if (jo.has(key) && !jo.isNull(key)) return true;
        else return false;
    }

    public boolean hasKeys(JSONObject jo, String[] keys) {
        for (String s : keys) {
            if (jo.has(s) && !jo.isNull(s)) continue;
            writeLog(Logger.LEVEL_ERROR, "hasKeys(), nokey: "+s);
            return false;
        }
        return true;
    }

    protected void writeLog(int level, String msg) {
        if (logger != null) {
            logger.writeLog(level, msg);
        } else {
            Log.d(TAG, ""+msg);
        }
    }

}
