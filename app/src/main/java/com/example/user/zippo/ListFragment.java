package com.example.user.zippo;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.greendao.ZI_NoteList;
import com.example.user.zippo.custom.SharedPreferencesUtil;
import com.example.user.zippo.data.DataNoteInformation;
import com.example.user.zippo.fragment.BackHandledFragment;
import com.example.user.zippo.store.NoteListStore;

import java.util.List;

/**
 * 列表畫面
 */
public class ListFragment extends BackHandledFragment
{
    private TextView mTextViewEmpty;
    private ProgressBar mProgressBarLoading;
    private ImageView mImageViewEmpty;
    private RecyclerView mRecyclerView;
    private ListAdapter mListAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private SyncData _taskSync; // 同步Task

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mTextViewEmpty = (TextView)view.findViewById(R.id.textViewEmpty);
        mImageViewEmpty = (ImageView)view.findViewById(R.id.imageViewEmpty);
        mProgressBarLoading = (ProgressBar)view.findViewById(R.id.progressBarLoading);
        mRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.layout_swipe_refresh);

        mRefreshLayout.setColorSchemeResources(android.R.color.holo_red_dark);
        mRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mRefreshLayout.setDistanceToTriggerSync(400);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                mRefreshLayout.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        NoteListStore noteListStore = new NoteListStore(getActivity());
                        List<ZI_NoteList> data = noteListStore.getList();
                        mListAdapter.updateList(data);
                        mRefreshLayout.setRefreshing(false);

                        mTextViewEmpty.setVisibility(mListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                        mImageViewEmpty.setVisibility(mListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                    }
                }, 3000);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        NoteListStore noteListStore = new NoteListStore(getActivity());
        List<ZI_NoteList> data = noteListStore.getList();

        mListAdapter = new ListAdapter(data);
        mRecyclerView.setAdapter(mListAdapter);

        mTextViewEmpty.setVisibility(mListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        mImageViewEmpty.setVisibility(mListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

        return view;
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {
        private List<ZI_NoteList> dataList;

        public ListAdapter(List<ZI_NoteList> data)
        {
            this.dataList = data;
        }

        public void updateList(List<ZI_NoteList> updateList)
        {
            dataList.clear();
            dataList = updateList;
            this.notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textViewText;
            TextView textViewComment;
            TextView textViewDate;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewText = (TextView) itemView.findViewById(R.id.text);
                this.textViewComment = (TextView) itemView.findViewById(R.id.comment);
                this.textViewDate = (TextView) itemView.findViewById(R.id.date);
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position)
        {
            if (dataList != null)
            {
                holder.textViewText.setText(String.valueOf(position + 1));
                holder.textViewComment.setText(dataList.get(position).getTitle());
                holder.textViewDate.setText(dataList.get(position).getDate());
                /*holder.textViewText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_size_large));
                holder.textViewComment.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_size_large));
                holder.textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_size_large));*/

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        WebFragment webFragment = new WebFragment();
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

                        ft.add(R.id.fragment_container, webFragment);
                        ft.addToBackStack("webFragment");

                        Bundle bundle = new Bundle();
                        bundle.putString("url", DataNoteInformation.urlArray[position]);
                        webFragment.setArguments(bundle);

                        ft.commit();
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }

    private class SyncData extends AsyncTask<Void, Void, Boolean>
    {
        List<ZI_NoteList> data;

        @Override
        protected void onPreExecute()
        {
            Log.i("debug", "執行");
            mProgressBarLoading.setVisibility(View.VISIBLE); // 進度條顯示
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Boolean isWork = true;
            try {
                NoteListStore noteListStore = new NoteListStore(getActivity());
                data = noteListStore.getList();
            } catch (Exception e) {
                e.printStackTrace();
                isWork = false;
            }
            return isWork;
        }

        @Override
        protected void onPostExecute(Boolean o)
        {
            if (o) {
                if (mListAdapter == null)
                {
                    mListAdapter = new ListAdapter(data);
                }
                else
                {
                    mListAdapter.updateList(data);
                }

                if (mRecyclerView.getAdapter() == null) //listView setUp
                {
                    mRecyclerView.setAdapter( mListAdapter);
                }

                mTextViewEmpty.setVisibility(mListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                mImageViewEmpty.setVisibility(mListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            } else {
                Log.i("debug","錯誤");
            }
            mProgressBarLoading.setVisibility(View.GONE); //進度條隱藏
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferencesUtil spUtil = new SharedPreferencesUtil(getActivity(), "shared_data");
        Log.i("debug",spUtil.getString("fontType", ""));

        _taskSync = new SyncData();
        _taskSync.execute();
    }


    @Override
    public boolean onBackPressed()
    {
        return false;
    }
}
