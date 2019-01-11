package com.neo.xutils3demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.neo.xutils3demo.db.MyDb;
import com.neo.xutils3demo.db.Person;
import com.neo.xutils3demo.utils.LogUtils;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_db)//加载布局xml
public class DbActivity extends AppCompatActivity {

    private static final String TAG = "DbActivity";

    //Event事件注解 ，点击button后弹出button测试
    @Event(value = {R.id.query},type = View.OnClickListener.class)
    private void onQueryClick(View view){//方法需要用private
        //查询数据库表中第一条数据

        LogUtils.d(TAG,"first");

        Person first = null;
        try {
            first = MyDb.getInstance().getDb().findFirst(Person.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG,first.toString());

        LogUtils.d(TAG,"all");

        //findAll()：查询所有结果
        List<Person> personAll = null;
        try {
            personAll = MyDb.getInstance().getDb().findAll(Person.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        for(int i=0;i<personAll.size();i++){
            LogUtils.d(TAG,personAll.get(i).toString());
        }

        LogUtils.d(TAG,"Where 1");

        //添加查询条件进行查询
        WhereBuilder b = WhereBuilder.b();
        b.and("id",">",2); //构造修改的条件
        b.and("id","<",4);
        List<Person> all = null;
        try {
            all = MyDb.getInstance().getDb().selector(Person.class).where(b).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        for(int i=0;i<all.size();i++){
            LogUtils.d(TAG,all.get(i).toString());
        }
        LogUtils.d(TAG,"Where 2");

        //第二种写法：
        List<Person> all2 = null;
        try {
            all2 = MyDb.getInstance().getDb().selector(Person.class).where("id",">",2).and("id","<",4).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        for(int i=0;i<all.size();i++){
            LogUtils.i(TAG,all2.get(i).toString());
        }
    }

    @Event(value = {R.id.update},type = View.OnClickListener.class)
    private void onUpdateClick(View view){//方法需要用private

        Person first = null;
        try {
            first = MyDb.getInstance().getDb().findFirst(Person.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        //第一种写法：
        first.setName("张三01");
        try {
            MyDb.getInstance().getDb().update(first, "c_name"); //c_name：表中的字段名
        } catch (DbException e) {
            e.printStackTrace();
        }

        //第二种写法：
        WhereBuilder b = WhereBuilder.b();
        b.and("id", "=", first.getId()); //构造修改的条件
        KeyValue name = new KeyValue("c_name", "张三02");
        try {
            MyDb.getInstance().getDb().update(Person.class, b, name);
        } catch (DbException e) {
            e.printStackTrace();
        }

        //第三种写法：
        first.setName("张三修改");
        try {
            MyDb.getInstance().getDb().saveOrUpdate(first);
        } catch (DbException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"修改成功",Toast.LENGTH_LONG).show();
    }

    @Event(value = {R.id.delete},type = View.OnClickListener.class)
    private void onDeleteClick(View view){//方法需要用private
        //第一种写法：
        try {
            MyDb.getInstance().getDb().delete(Person.class); //child_info表中数据将被全部删除
        } catch (DbException e) {
            e.printStackTrace();
        }
        //第二种写法，添加删除条件：
        WhereBuilder b = WhereBuilder.b();
        b.and("id",">",2); //构造修改的条件
        b.and("id","<",4);
        try {
            MyDb.getInstance().getDb().delete(Person.class, b);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.deltable},type = View.OnClickListener.class)
    private void onDelTableClick(View view){//方法需要用private
        try {
            MyDb.getInstance().getDb().dropTable(Person.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.deldb},type = View.OnClickListener.class)
    private void onDelDbClick(View view){//方法需要用private
        try {
            MyDb.getInstance().getDb().dropDb();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.create},type = View.OnClickListener.class)
    private void onCreateDbClick(View view){//方法需要用private
        MyDb.getInstance().create();
        ArrayList<Person> person = new ArrayList<>();
        person.add(new Person("张三"));
        person.add(new Person("李四"));
        person.add(new Person("王五"));
        person.add(new Person("赵六"));
        try {
            MyDb.getInstance().getDb().save(person);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); //绑定注解
        onCreateDbClick(null);
    }
}
