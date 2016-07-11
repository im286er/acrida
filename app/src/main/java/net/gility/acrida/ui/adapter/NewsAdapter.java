package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.News;
import net.gility.acrida.ui.cell.NewsCell;

public class NewsAdapter extends BaseCellAdapter<News, NewsCell> {

   @Override
    protected int getLayoutId() {
       return R.layout.cell_news;
   }
}
