package com.example.mycproviderapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    static final String[] mprojection = {
            UserDictionary.Words._ID,
            UserDictionary.Words.WORD,
            UserDictionary.Words.LOCALE,
    };
    static String selectionClause = null;
    static String[] selectionArgs = { "" };
    static Cursor mcursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button searchButton = findViewById(R.id.searchButton);
        final Button insertButton = findViewById(R.id.insertButton);
        final EditText searchText = findViewById(R.id.searchTextField);


        searchButton.setOnClickListener(View ->{
            try {
                final String searchString = searchText.getText().toString();
                if (TextUtils.isEmpty(searchString)) {
                    mcursor = getContentResolver().query(
                            UserDictionary.Words.CONTENT_URI,
                            mprojection,
                            null,
                            null,
                            null
                    );
                } else {
                    selectionClause = UserDictionary.Words.WORD + " =? ";
                    selectionArgs[0] = searchString;
                    mcursor = getContentResolver().query(
                            UserDictionary.Words.CONTENT_URI,
                            mprojection,
                            selectionClause,
                            selectionArgs,
                            null
                    );
                }

                if (mcursor == null) {
                    Snackbar.make(View, "An Error occorred", Snackbar.LENGTH_LONG).show();
                } else if (mcursor.getCount() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    while (mcursor.moveToNext()) {
                        int wordIndex = mcursor.getColumnIndex(UserDictionary.Words.WORD);
                        String word = mcursor.getString(wordIndex);

                        stringBuilder.append(word).append("\n");
                    }

                    TextView textView = findViewById(R.id.wordText);
                    textView.setText(stringBuilder.toString());

                } else {
                    Snackbar.make(View, "UserDictionary is empty", Snackbar.LENGTH_LONG).show();
                }
                if (mcursor != null) {
                    mcursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        insertButton.setOnClickListener(View ->{
            try {
                Uri newUri;
                ContentValues newValues = new ContentValues();


                newValues.put(UserDictionary.Words.APP_ID, "com.example.user.mycproviderapplication");
                newValues.put(UserDictionary.Words.LOCALE, "en_US");
                newValues.put(UserDictionary.Words.WORD, searchText.getText().toString() );
                newValues.put(UserDictionary.Words.FREQUENCY, "100");

                newUri = getContentResolver().insert(
                        UserDictionary.Words.CONTENT_URI,
                        newValues
                );
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}