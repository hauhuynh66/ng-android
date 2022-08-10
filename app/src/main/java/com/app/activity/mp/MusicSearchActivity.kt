package com.app.activity.mp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.app.fragment.HistoryChipGroupFragment
import com.app.ngn.R

class MusicSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_mp_search)

        val search = findViewById<SearchView>(R.id.search_view)
        search.apply {
            setIconifiedByDefault(true);
            isFocusable = true;
            isIconified = false;
            clearFocus();
            requestFocusFromTouch();
            setOnCloseListener {
                super.onBackPressed()
                true
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, HistoryChipGroupFragment("")).commit()
    }
}