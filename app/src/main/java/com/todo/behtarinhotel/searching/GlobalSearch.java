package com.todo.behtarinhotel.searching;

import com.todo.behtarinhotel.searching.expedia.ExpediaSearch;
import com.todo.behtarinhotel.simpleobjects.SearchParamsSO;
import com.todo.behtarinhotel.simpleobjects.SearchRoomSO;

import java.util.ArrayList;

/**
 * Created by maxvitruk on 07.07.15.
 */
public class GlobalSearch implements SearchCallBackListener{

    int iterator = 0;
    GlobalSearchCallBackListener globalSearchCallBackListener;
    ArrayList<Object> searchMargedResultList;

    public GlobalSearch() {
    }

    public GlobalSearch(GlobalSearchCallBackListener globalSearchCallBackListener) {
        this.globalSearchCallBackListener = globalSearchCallBackListener;
    }

    public void searchingHotelsByParams(SearchParamsSO params){

        ExpediaSearch eSearch = new ExpediaSearch();
        eSearch.search(new SearchParamsSO("London", "10/10/2015", "10/12/2015",new ArrayList<SearchRoomSO>(),0));
    }

    @Override
    public void onResult(ArrayList<Object> result) {
        margeMySearchResult(result);

        if(waitForAllResults() > 2){
            sendResultToAvtivity();
        }
    }


    private void margeMySearchResult(ArrayList<Object> resultList){
        if(searchMargedResultList!= null){
            searchMargedResultList.addAll(resultList);
        }else {
            searchMargedResultList = new ArrayList<>();
            searchMargedResultList.addAll(resultList);
        }
    }
    private int waitForAllResults(){
        return iterator++;
    }

    private void sendResultToAvtivity(){
        if(globalSearchCallBackListener!= null){
            globalSearchCallBackListener.onResult(searchMargedResultList);
        }
    }
    public void setGlobalSearchCallBackListener(GlobalSearchCallBackListener globalSearchCallBackListener) {
        this.globalSearchCallBackListener = globalSearchCallBackListener;
    }
    /**
     * Created by maxvitruk on 07.07.15.
     */
    public interface GlobalSearchCallBackListener {
        void onResult(ArrayList<Object> result);
    }
}
