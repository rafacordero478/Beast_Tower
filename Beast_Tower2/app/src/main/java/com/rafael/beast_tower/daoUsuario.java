package com.rafael.beast_tower;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class daoUsuario {
    Context c;
    User u;
    ArrayList<User> lista;
    SQLiteDatabase sql;
    String bd = "BDUsuario";
    String tabla = "create table if not exists usuario(id integer primary key autoincrement, usuario text, pass text)";

    public daoUsuario(Context c){
        this.c = c;
        sql = c.openOrCreateDatabase(bd, c.MODE_PRIVATE, null);
        sql.execSQL(tabla);
        u=new User();
    }

    public Boolean insertUsuario(User u){
        if(buscar(u.getUsuario())==0){
            ContentValues cv=new ContentValues();
            cv.put("usuario",u.getUsuario());
            cv.put("pass",u.getPass());
            Boolean result = (sql.insert("usuario",null,cv)>0);
            return result;
        }
        else {
            return false;
        }
    }

    public int buscar(String u){
        int x=0;
        lista=selectUsuario();
        for(User us:lista){
            if(us.getUsuario().equals(u)){
                x++;
                break;
            }
        }
        return x;
    }

    public ArrayList<User>  selectUsuario() {
        ArrayList<User> lista=new ArrayList<User>();
        lista.clear();
        Cursor cr=sql.rawQuery("select * from usuario", null);
        if(cr!=null && cr.moveToFirst()){
            do{
                User u= new User();
                u.setId(cr.getInt(0));
                u.setUsuario(cr.getString(1));
                u.setPass(cr.getString(2));
                lista.add(u);
            }while (cr.moveToNext());
        }
        return lista;
    }

    public int login(String us, String pa){
        int a = 0;
        Cursor cr = sql.rawQuery("select * from usuario",null);
        if(cr != null && cr.moveToFirst()){
            do {
                if(cr.getString(1).equals(us) && cr.getString(2).equals(pa)){
                    a++;
                }
            }while(cr.moveToNext());
        }
        return a;
    }

    public User getUsuario(String user, String pass){
        lista=selectUsuario();
        for(User us : lista){
            if (us.getUsuario().equals(user) && us.getPass().equals(pass)){
                return us;
            }
        }
        return null;
    }
    public void cleanTable(){

        // Ejecutar el comando
        sql.execSQL("DELETE FROM usuario");
    }


}
