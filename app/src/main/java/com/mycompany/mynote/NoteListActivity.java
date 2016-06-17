package com.mycompany.mynote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


//import com.mycompany.mynote.dummy.DummyContent;

import com.mycompany.mynote.data.DataBaseOperation;
import com.mycompany.mynote.data.MyDataBaseHelper;
import com.mycompany.mynote.data.NoteItem;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NoteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class NoteListActivity extends AppCompatActivity implements NoteDetailFragment.SaveContentListener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static DataBaseOperation myDataBaseManager;
    private static boolean justChanged = false;
    private View recyclerView;
    private SimpleItemRecyclerViewAdapter.ViewHolder viewHolder;
    private final String DATABASE_NAME = "myNote.db3";
    private final String TABLE_NAME = "note_info";
    private static int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        System.out.println("NoteListActivity onCreate:"+NoteListActivity.this);
        processExtraData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        myDataBaseManager = new DataBaseOperation(new MyDataBaseHelper(this,DATABASE_NAME,1));
        myDataBaseManager.createTable(TABLE_NAME);
        recyclerView = findViewById(R.id.note_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView,this);
        if (findViewById(R.id.note_detail_container) != null) {
            mTwoPane = true;
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("insertData();");
                Integer id = -1;
                try {
                    id = myDataBaseManager.insertData(null);
                    updateRecyclerView((RecyclerView) recyclerView, NoteListActivity.this);
                }catch (SQLiteException se){
                    myDataBaseManager.createTable(TABLE_NAME);
                    id = myDataBaseManager.insertData(null);
                    updateRecyclerView((RecyclerView) recyclerView, NoteListActivity.this);
                }
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(NoteDetailFragment.ARG_ITEM_ID, id);
                    arguments.putInt(NoteDetailFragment.IS_TWO_PAN,1);
                    NoteDetailFragment fragment = new NoteDetailFragment();
                    System.out.println("fragment:"+fragment);
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.note_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(NoteListActivity.this, NoteDetailActivity.class);
                    intent.putExtra(NoteDetailFragment.ARG_ITEM_ID, id);
                    intent.putExtra(NoteDetailFragment.IS_TWO_PAN,0);
                    startActivity(intent);
                }
            }
        });


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView,Context context) {
        //数据库中读取数据
        List<NoteItem> Items = myDataBaseManager.queryData();
        SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(Items);
        recyclerView.setAdapter(adapter);
    }
    public void updateRecyclerView(@NonNull RecyclerView recyclerView,Context context) {
        //数据库中读取数据
        SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(myDataBaseManager.queryData());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<NoteItem> mNoteList;

        public SimpleItemRecyclerViewAdapter(List<NoteItem> items) {
            mNoteList = items;
        }

        //负责为Item创建视图
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            System.out.println("onCreateViewHolder");
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_list_content, parent, false);//这个方法主要生成为每个Item inflater出一个View，
            // 但是该方法返回的是一个ViewHolder。
            viewHolder = new ViewHolder(view);//把View直接封装在ViewHolder中，然后我们面向的是ViewHolder这个实例
            return viewHolder;
        }

        //负责将数据绑定到Item的视图上
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mNoteList.get(position);
            holder.mTitleView.setText(holder.mItem.title);
            holder.mDateView.setText(holder.mItem.date.toString());
//            System.out.println("onBindViewHolder title:"+holder.mItem.title+" date:"+holder.mItem.date.toString());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("onClick id = "+holder.mItem.id);
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(NoteDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        NoteDetailFragment fragment = new NoteDetailFragment();
                        System.out.println("fragment:"+fragment);
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.note_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, NoteDetailActivity.class);
                        intent.putExtra(NoteDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        context.startActivity(intent);
                    }
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder2=new AlertDialog.Builder(NoteListActivity.this);
//                    builder2.setTitle("小同学");
                    //builder2.setMessage("");
                    builder2.setPositiveButton("删除",new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int which)
                        {
                            // TODO 自动生成的方法存根
                            int id = holder.mItem.id;
                            myDataBaseManager.deleteData(TABLE_NAME,id);
                            updateRecyclerView((RecyclerView) recyclerView,NoteListActivity.this);
                            dialog.dismiss();
                        }
                    });
                    builder2.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    });
                    builder2.show();

                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mNoteList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mDateView;
            public final TextView mTitleView;
            public NoteItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mDateView = (TextView) view.findViewById(R.id.date);
                mTitleView = (TextView) view.findViewById(R.id.title);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }



        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myDataBaseManager.getMyHelper() != null){
            System.out.println("closeDatabase()");
            myDataBaseManager.closeDataBaseHelper();
        }
    }

    @Override
    public void onContentChanged(Integer id,String sDetail) {
//        System.out.println("NoteListActivity:onContentChanged");
        myDataBaseManager.updateData(TABLE_NAME,id,sDetail);
        justChanged = true;
        updateRecyclerView((RecyclerView) recyclerView,NoteListActivity.this);
    }

    protected void onNewIntent(Intent intent) {
//        System.out.println("onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
    }

    private void processExtraData(){
        Intent intent = getIntent();
        Integer id = intent.getIntExtra(NoteDetailActivity.CHANEG_ID,-1);
        String content = intent.getStringExtra(NoteDetailActivity.CHANEG_CONTENT);
        System.out.println("processExtraData:intent="+intent);
        System.out.println("processExtraData:id="+id+" content="+content);
        if (id != -1)
            onContentChanged(id,content);
    }

}
