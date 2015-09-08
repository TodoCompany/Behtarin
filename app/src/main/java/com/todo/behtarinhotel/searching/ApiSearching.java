package com.todo.behtarinhotel.searching;

import com.todo.behtarinhotel.simpleobjects.SearchParams;

/**
 * Created by maxvitruk on 07.07.15.
 */
public interface ApiSearching {

    void search(SearchParams params);

    void setOnSearchResultListener(SearchCallBackListener searchCallBackListener);

}
