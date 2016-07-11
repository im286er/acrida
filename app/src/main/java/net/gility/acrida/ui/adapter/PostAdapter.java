package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.Post;
import net.gility.acrida.ui.cell.PostCell;
/**
 * post（讨论区帖子）适配器
 *@author Alimy
 */
public class PostAdapter extends BaseCellAdapter<Post, PostCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_post;
    }
}
