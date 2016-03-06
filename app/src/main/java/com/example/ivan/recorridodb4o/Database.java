package com.example.ivan.recorridodb4o;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.example.ivan.recorridodb4o.pojo.Position;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by ivan on 3/6/2016.
 */
public class Database {
    private Context ctx;
    private ObjectContainer db;



    public void create(Context ctx){
        db = Db4oEmbedded.openFile
                (Db4oEmbedded.newConfiguration(), ctx.getExternalFilesDir(null) +
                        "/recorridobd.db4o");

    }

    public void close(){
        db.close();
    }

    public void addPosition(Position p ){
        db.store(p);
        db.commit();
    }

    public List<Position> getPositions(String day){
        List<Position> positionsReturn = new ArrayList<>();
        Query query = db.query();
        query.constrain(Position.class);
        ObjectSet<Position> positions = query.execute();

        for(Position p : positions){
            Log.v("iguales", p.getDay() + " que " + day);

            if(day.compareTo(p.getDay())==0) {
                positionsReturn.add(p);
                Log.v("iguales", "son iguales");
            }else{
                Log.v("iguales", "diferentes");
            }


        }
        return positionsReturn;

    }
    public void deletePosition(Position p){
        db.delete(p);
        db.commit();
    }




}
