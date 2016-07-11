package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.Comment;
import net.gility.acrida.ui.cell.CommentCell;

public class CommentAdapter extends BaseCellAdapter<Comment, CommentCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_comment;
    }
}
