package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.SearchResult;
import net.gility.acrida.ui.cell.SearchResultCell;

public class SearchResultAdapter extends BaseCellAdapter<SearchResult, SearchResultCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_search_result;
    }
}
