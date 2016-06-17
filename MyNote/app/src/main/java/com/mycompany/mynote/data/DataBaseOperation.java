package com.mycompany.mynote.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import com.mycompany.mynote.dummy.DummyContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by delia on 16/5/19.
 */
public class DataBaseOperation {
    private MyDataBaseHelper myHelper;
    private SQLiteDatabase db;
    public MyDataBaseHelper getMyHelper() {
        return myHelper;
    }

    public void setMyHelper(MyDataBaseHelper myHelper) {
        this.myHelper = myHelper;
    }

    public DataBaseOperation(MyDataBaseHelper myHelper) {
        this.myHelper = myHelper;
        System.out.println(myHelper);
        this.db = myHelper.getWritableDatabase();//获取数据库对象
    }
    public void closeDataBaseHelper(){
        if (myHelper != null){
            if (db != null && db.isOpen()) db.close();
            myHelper.close();
        }
    }
    //向数据库中插入数据
    public int insertData(String sDetail){
        //使用execSQL方法向表中插入数据
        //db.execSQL("insert into note_info(title,detail) values('"+title+"','"+detail+"')");
        String sTitle = (sDetail == null || sDetail.length() == 0 )?"新建备忘录":getTitle(sDetail);
        String sDate =  getDate();
        System.out.println("insert into note_info values(null,?,?,?)"+new String[]{ sTitle, sDetail ,sDate });
        db.execSQL("insert into note_info values(null,?,?,?)",new String[]{ sTitle, sDetail ,sDate });
        Cursor c = db.rawQuery("select * from note_info order by date desc limit 1",null);
        while (c.moveToFirst()){
            return c.getInt(0);
        }
        return -1;
    }
    public void deleteData(String tableName,int id){
        String strSql = "delete from "+tableName+" where _id = "+id;
        db.execSQL(strSql);
    }
    //向数据库中更新数据
    public void updateData(String tableName,Integer id,String sDetail){
        String sTitle = (sDetail.length() == 0)?"新建备忘录":getTitle(sDetail);
        String sDate =  getDate();
        System.out.println("update "+tableName+" id = "+id+" title= '"+sTitle+"', detail = '"+sDetail);
        db.execSQL("update note_info set title= '"+sTitle+"', detail = '"+sDetail+"', date = '"+sDate+"' where _id = "+id);
    }
    //从数据库中查询数据
    public List<NoteItem> queryData(){
        System.out.println("queryData():select * from note_info order by date desc");
        List<NoteItem> Items = new ArrayList<NoteItem>();
        //Map<String, NoteItem> ITEM_MAP = new HashMap<String, NoteItem>();
        Cursor cursor = db.rawQuery("select * from note_info order by date desc",null);
        System.out.println("共 " + cursor.getCount() +" 条数据: ");
        while (cursor.moveToNext()){
            Integer id =  cursor.getInt(0);
            String title = cursor.getString(1);
            String details = cursor.getString(2);
            String strDate = cursor.getString(3);
            NoteItem item = new NoteItem(id,title,details,strDate);
            Items.add(item);
            System.out.println("id = "+id+ " title = "+ title + " detail = "+details+" date = " + strDate);
        }
        cursor.close();//关闭结果集
        return Items;
    }
    public void insertColumn(){
        db.execSQL("alter table note_info add column date varchar(50)");
    }
    public NoteItem getDetail(Integer id){
        //System.out.println("getDetail(Integer id):SELECT * from note_info where _id = "+id);
        NoteItem item;
        Cursor cursor = db.rawQuery("SELECT * from note_info where _id = "+id,null);
        while (cursor.moveToFirst()){
            //Integer idd =  id;
            String title = cursor.getString(1);
            String details = cursor.getString(2);
            String strDate = cursor.getString(3);
            item = new NoteItem(id,title,details,strDate);
            System.out.println("getDetail(id):id = "+id+ " title = "+ title + " detail = "+details);
            return item;
        }
        return null;
    }
    public void deleteTable(String tableName){
        db.execSQL("drop table if exists "+tableName);
    }
    public void clearDatabase(String tableName){
        String sql = "DELETE FROM "+tableName+";";
        db.execSQL(sql);
    }
    public void createTable(String name){
        String sql = "create table if not exists note_info( _id integer  primary key autoincrement," +
                        " title  varchar(50),"+
                        " detail  text(65535),"+
                        " date varchar(50))";
        db.execSQL(sql);
    }
    public void clearTable(){
        String sql = "DELETE FROM note_info;";
        db.execSQL(sql);
    }
    public void dropTable(String name){
        String sql = "DROP TABLE "+name+";";
        db.execSQL(sql);
    }

    public String getDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        String strDate = formatter.format(date);
        return strDate;
    }

    public String getTitle(String strContent){
        String strTitle;
        if(strContent.length() > 10) {
            strTitle = strContent.substring(0, 10);
        }
        else{
            strTitle = strContent;
        }
        return strTitle;
    }
}
