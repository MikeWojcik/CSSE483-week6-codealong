package edu.rosehulman.moviequotes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements MovieQuoteAdapter.Callback {


    private edu.rosehulman.moviequotes.MovieQuoteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDialog(null);
            }
        });

        mAdapter = new edu.rosehulman.moviequotes.MovieQuoteAdapter(this);
        RecyclerView view = findViewById(R.id.recycler_view);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setHasFixedSize(true);
        view.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddEditDialog(final MovieQuote movieQuote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(movieQuote == null ? R.string.dialog_add_title : R.string.dialog_edit_title));
        View view = getLayoutInflater().inflate(R.layout.dialog_add, null, false);
        builder.setView(view);
        final EditText quoteEditText = view.findViewById(R.id.dialog_add_quote_text);
        final EditText movieEditText = view.findViewById(R.id.dialog_add_movie_text);
        if (movieQuote != null) {
            // pre-populate
            quoteEditText.setText(movieQuote.getQuote());
            movieEditText.setText(movieQuote.getMovie());

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // empty
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // empty
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String quote = quoteEditText.getText().toString();
                    String movie = movieEditText.getText().toString();
                    mAdapter.update(movieQuote, quote, movie);
                }
            };

            quoteEditText.addTextChangedListener(textWatcher);
            movieEditText.addTextChangedListener(textWatcher);
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (movieQuote == null) {
                    String quote = quoteEditText.getText().toString();
                    String movie = movieEditText.getText().toString();
                    mAdapter.add(new MovieQuote(quote, movie));
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }


    @Override
    public void onEdit(final MovieQuote movieQuote) {
        showAddEditDialog(movieQuote);
    }
}
