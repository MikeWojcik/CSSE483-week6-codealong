package edu.rosehulman.moviequotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt Boutell on 12/15/2015.
 */
public class MovieQuoteAdapter extends RecyclerView.Adapter<MovieQuoteAdapter.ViewHolder> {

    private List<MovieQuote> mMovieQuotes;
    private Callback mCallback;


    public MovieQuoteAdapter(Callback callback) {
        mCallback = callback;
        mMovieQuotes = new ArrayList<>();
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
        //TODO: Remove the next line(s) and use Firebase instead
        mMovieQuotes.remove(movieQuote);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mMovieQuotes.size();
    }

    public void add(MovieQuote movieQuote) {
        //TODO: Remove the next line(s) and use Firebase instead
        mMovieQuotes.add(0, movieQuote);
        notifyDataSetChanged();
    }

    public void update(MovieQuote movieQuote, String newQuote, String newMovie) {
        //TODO: Remove the next line(s) and use Firebase instead
        movieQuote.setQuote(newQuote);
        movieQuote.setMovie(newMovie);
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
}
