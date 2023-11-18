package com.uth.e23.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.uth.e23.models.cPhotograh;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBPICTURE
{
    private picture_helper oPicture_helper;
    private Context oContext;

    public DBPICTURE(Context oContext_)
    {
        oContext = oContext_;
        oPicture_helper = new picture_helper(oContext);
    }

    public ArrayList<cPhotograh> leerSignature()
    {
        ArrayList<cPhotograh> oSignatureArrayList = new ArrayList<>();
        SQLiteDatabase oSqLiteDatabase = oPicture_helper.getReadableDatabase();
        if (oSqLiteDatabase != null)
        {
            Cursor oC = oSqLiteDatabase.rawQuery("select descripcion,foto from galeria",null);

            while (oC.moveToNext())
            {
                cPhotograh oS = new cPhotograh();

                byte[] blobData = oC.getBlob(1);

                Bitmap bitmap = BitmapFactory.decodeByteArray(blobData, 0, blobData.length);

                oS.setFoto(bitmap);

                //Log.e("FIRMA",oS.getFirma_digital().toString());

                oS.setDescripcion(oC.getString(oC.getColumnIndex("descripcion")));
                oSignatureArrayList.add(oS);

            }


        }else{
            Toast.makeText(oContext, "DATABASE NO CREADA", Toast.LENGTH_SHORT).show();
        }

        return oSignatureArrayList;
    }


    public boolean createSignature(cPhotograh oS)
    {
        try {

            Log.e("FIRMA",oS.getFoto().toString());

            Bitmap firmaBitmap = oS.getFoto();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            firmaBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] firmaBytes = stream.toByteArray();

            SQLiteDatabase oSqLiteDatabase = oPicture_helper.getWritableDatabase();
            String sql = "INSERT INTO galeria (descripcion,foto) VALUES (?,?)";
            SQLiteStatement statement = oSqLiteDatabase.compileStatement(sql);

            statement.clearBindings();
            statement.bindString(1, oS.getDescripcion());
            statement.bindBlob(2, firmaBytes);
            statement.executeInsert();
            statement.close();
            oSqLiteDatabase.close();

            return true;
        }catch (Exception e){
            Log.e("DB",e.toString());
            Toast.makeText(oContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    };


}
