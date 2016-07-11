package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.Blog;
import net.gility.acrida.ui.cell.BlogCell;

/**
 * @author Alimy
 */
public class BlogAdapter extends BaseCellAdapter<Blog, BlogCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_blog;
    }
}
