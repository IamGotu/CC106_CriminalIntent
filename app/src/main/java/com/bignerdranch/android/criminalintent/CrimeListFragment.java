package com.bignerdranch.android.criminalintent;

import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CrimeListFragment extends Fragment {

    public class ListRow extends RecyclerView.ViewHolder {
        public ImageView mThumbnail;
        public ListRow(View view) {
            super(view);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

}
