package com.todo.behtarinhotel.searching;

import com.todo.behtarinhotel.simpleobjects.SearchParamsSO;

/**
 * Created by maxvitruk on 07.07.15.
 */
public interface ApiSearching {

    void search(SearchParamsSO params);

    void setOnSearchResultListener(SearchCallBackListener searchCallBackListener);

}
