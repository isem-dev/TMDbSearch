package com.isem.tmdbviewer.ui;

import android.util.Log;

import androidx.appcompat.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxSearchObservable {

    private static final String TAG = RxSearchObservable.class.getSimpleName();

    public static Observable<String> fromView(SearchView searchView) {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                subject.onNext(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                subject.onNext(newText);
                return false;
            }
        });

        return subject;
    }
}
