package edu.rosehulman.moviequotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt Boutell on 12/15/2015.
 */
public class MovieQuoteAdapter extends RecyclerView.Adapter<MovieQuoteAdapter.ViewHolder> {

    private List<MovieQuote> mMovieQuotes;
    private DatabaseReference mMovieQuotesRef;
    private Callback mCallback;


    public MovieQuoteAdapter(Callback callback) {
        mCallback = callback;
        mMovieQuotesRef = FirebaseDatabase.getInstance().getReference().child("quotes");
        mMovieQuotes = new ArrayList<>();
        mMovieQuotesRef.addChildEventListener(new QuotesChildEventListener());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_quote_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MovieQuote movieQuote = mMovieQuotes.get(position);
        holder.mQuoteTextView.setText(movieQuote.getQuote());
        holder.mMovieTextView.setText(movieQuote.getMovie());
    }

    public void remove(MovieQuote movieQuote) {
        mMovieQuotesRef.child(movieQuote.getKey()).removeValue();
    }


    @Override
    public int getItemCount() {
        return mMovieQuotes.size();
    }

    public void add(MovieQuote movieQuote) {
        mMovieQuotesRef.push().setValue(movieQuote);
        notifyDataSetChanged();
    }

    public void update(MovieQuote movieQuote, String newQuote, String newMovie) {
        movieQuote.setQuote(newQuote);
        movieQuote.setMovie(newMovie);
        mMovieQuotesRef.child(movieQuote.getKey()).setValue(movieQuote);

        notifyDataSetChanged();
    }

    public interface Callback {
        void onEdit(MovieQuote movieQuote);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mQuoteTextView;
        private TextView mMovieTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mQuoteTextView = itemView.findViewById(R.id.quote_text);
            mMovieTextView = itemView.findViewById(R.id.movie_text);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MovieQuote movieQuote = mMovieQuotes.get(getAdapterPosition());
            mCallback.onEdit(movieQuote);
        }

        @Override
        public boolean onLongClick(View v) {
            remove(mMovieQuotes.get(getAdapterPosition()));
            return true;
        }
    }

    private class QuotesChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            MovieQuote quote = dataSnapshot.getValue(MovieQuote.class);
            quote.setKey(dataSnapshot.getKey());
            mMovieQuotes.add(0,quote);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            MovieQuote newMovieQuote = dataSnapshot.getValue(MovieQuote.class);
            for(MovieQuote movieQuote: mMovieQuotes){
                if(movieQuote.getKey().equals(key)){
                  movieQuote.setVals(newMovieQuote);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();

            for(MovieQuote movieQuote : mMovieQuotes){
                if(movieQuote.getKey().equals(key)){
                    mMovieQuotes.remove(movieQuote);
                    notifyDataSetChanged();
                    return;
                }
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
