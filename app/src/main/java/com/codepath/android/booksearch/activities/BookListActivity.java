package com.codepath.android.booksearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.adapters.BookAdapter;
import com.codepath.android.booksearch.models.Book;
import com.codepath.android.booksearch.models.converters.BookConverter;
import com.codepath.android.booksearch.models.remote.BookQueryResponse;
import com.codepath.android.booksearch.net.ApiCallback;
import com.codepath.android.booksearch.net.BookClient;
import java.util.ArrayList;
import java.util.List;


public class BookListActivity extends AppCompatActivity {
    private RecyclerView rvBooks;
    private BookAdapter bookAdapter;
    private BookClient client;
    private ArrayList<Book> abooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        rvBooks = (RecyclerView) findViewById(R.id.rvBooks);
        abooks = new ArrayList<>();

        bookAdapter = new BookAdapter(this, abooks);
        rvBooks.setAdapter(bookAdapter);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));

        client = new BookClient();

        // Fetch the data remotely
        fetchBooks("Oscar Wilde");
    }

    private void fetchBooks(String query) {
        client.getBooks(query, new ApiCallback<BookQueryResponse>(BookQueryResponse.class) {
            @Override
            public void onSuccess(BookQueryResponse response) {
                List<Book> books = BookConverter.getBooks(response);
                abooks.clear();
                // Load model objects into the adapter
                abooks.addAll(books);
                bookAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.dispose();
    }
}
