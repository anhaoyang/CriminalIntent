package com.criminalintent.utils;

import android.content.Context;

import com.criminalintent.bean.Crime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context mContent, String mFilename) {
        this.mContext = mContent;
        this.mFilename = mFilename;
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException{
        ArrayList<Crime> crimes=new ArrayList<Crime>();
        BufferedReader reader=null;
        try {
            InputStream in=mContext.openFileInput(mFilename);
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString=new StringBuilder();
            String line=null;
            while((line=reader.readLine())!=null){
                jsonString.append(line);
            }
            JSONArray array=(JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            for(int i=0;i<array.length();i++){
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } finally {
            if(reader!=null){
                reader.close();
            }
        }
    return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException{
        JSONArray array=new JSONArray();
        for (Crime c:crimes) {
            array.put(c.toJSON());
        }
        Writer writer=null;
        try {
            OutputStream out=mContext
                    .openFileOutput(mFilename,Context.MODE_PRIVATE);
            writer=new OutputStreamWriter(out);
            writer.write(array.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
