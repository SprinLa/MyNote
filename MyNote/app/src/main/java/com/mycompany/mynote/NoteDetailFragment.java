package com.mycompany.mynote;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mycompany.mynote.data.NoteItem;

//import com.mycompany.mynote.dummy.DummyContent;

/**
 * A fragment representing a single Note detail screen.
 * This fragment is either contained in a {@link NoteListActivity}
 * in two-pane mode (on tablets) or a {@link NoteDetailActivity}
 * on handsets.
 */
public class NoteDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String IS_TWO_PAN = "is_two_pan";
    SaveContentListener mCallback;
    private EditText editText;
    private String strBefore;

    /**
     * The dummy content this fragment is presenting.
     */
    private NoteItem mItem;
    private int isTwoPan = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //NoteListActivity.myDataBaseManager.queryData();
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            mItem = NoteListActivity.myDataBaseManager.getDetail(getArguments().getInt(ARG_ITEM_ID));
            isTwoPan = getArguments().getInt(IS_TWO_PAN);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_detail, container, false);
        editText = (EditText)rootView.findViewById(R.id.edit_content);
        if (mItem != null) {
            strBefore = mItem.details;
            editText.setText(strBefore);
//            System.out.println("sreBefore:"+strBefore);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                System.out.println("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isTwoPan == 1) {
//                    System.out.println("onTextChanged");
                    String strAfter = editText.getText().toString();
                    //System.out.println("strBefore:" + strBefore + "strAfter:" + strAfter + strAfter.equals(strBefore));
                    if (!strAfter.equals(strBefore))
//                        System.out.println("onContentChanged");
                    mCallback.onContentChanged(getArguments().getInt(ARG_ITEM_ID), strAfter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged");
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (isTwoPan == 0 && (!hasFocus)) {
//                    System.out.println("onFocusChange");
                    String strAfter = editText.getText().toString();
                    if (!strAfter.equals(strBefore))
                    mCallback.onContentChanged(getArguments().getInt(ARG_ITEM_ID),strAfter);
                }
            }
        });

        return rootView;
    }

    public interface SaveContentListener{
        public void onContentChanged(Integer id,String content);
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
           mCallback = (SaveContentListener)activity;
        }  catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implements SaveContentListener");
        }
    }
}
