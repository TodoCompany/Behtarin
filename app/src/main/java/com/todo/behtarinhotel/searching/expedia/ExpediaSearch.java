package com.todo.behtarinhotel.searching.expedia;

import com.todo.behtarinhotel.searching.ApiSearching;
import com.todo.behtarinhotel.searching.SearchCallBackListener;
import com.todo.behtarinhotel.simpleobjects.SearchParams;

import java.util.ArrayList;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class ExpediaSearch implements ApiSearching {

    private SearchCallBackListener searchCallBackListener;

    @Override
    public void search(SearchParams params) {
        //todo make some search and call back
        //on result
        if(searchCallBackListener != null) {
            searchCallBackListener.onResult(new ArrayList<Object>());
        }
    }

    @Override
    public void setOnSearchResultListener(SearchCallBackListener searchCallBackListener) {
        this.searchCallBackListener = searchCallBackListener;
    }
}
