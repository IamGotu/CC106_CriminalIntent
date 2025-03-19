package com.bignerdranch.android.criminalintent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            CrimeAdapter adapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(adapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final ImageView mSolvedImageView;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        private String getLocalizedFormattedDate(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
            return sdf.format(date);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDate().toString());

            // Fix the visibility issue by resetting the visibility
            if (crime.isSolved()) {
                mSolvedImageView.setVisibility(View.VISIBLE);
            } else {
                mSolvedImageView.setVisibility(View.GONE);
            }

            // Debugging Log
            Log.d("CrimeListFragment", "Binding: " + crime.getTitle() + " Solved: " + crime.isSolved());
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getID());
            startActivity(intent);

        }
    }

    private class SeriousCrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final Button mContactPoliceButton;
        private final ImageView mSolvedImageView;
        private Crime mCrime;

        public SeriousCrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
            mContactPoliceButton = itemView.findViewById(R.id.contact_police_button);

            mContactPoliceButton.setOnClickListener(v ->
                    Toast.makeText(getActivity(), "Calling police for " + mCrime.getTitle(), Toast.LENGTH_SHORT).show()
            );
        }

        private String getLocalizedFormattedDate(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
            return sdf.format(date);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDate().toString());
            if (crime.isSolved()) {
                mSolvedImageView.setVisibility(View.VISIBLE);
            } else {
                mSolvedImageView.setVisibility(View.GONE);
            }

            // Debugging Log
            Log.d("CrimeListFragment", "Binding: " + crime.getTitle() + " Solved: " + crime.isSolved());
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getID());
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_NORMAL = 0;
        private static final int TYPE_SERIOUS = 1;

        private final List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            return crime.isRequiresPolice() ? TYPE_SERIOUS : TYPE_NORMAL;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (viewType == TYPE_SERIOUS) {
                View view = layoutInflater.inflate(R.layout.list_item_serious_crime, parent, false);
                return new SeriousCrimeHolder(view);
            } else {
                View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
                return new CrimeHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if (holder instanceof SeriousCrimeHolder) {
                ((SeriousCrimeHolder) holder).bind(crime);
            } else {
                ((CrimeHolder) holder).bind(crime);
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}