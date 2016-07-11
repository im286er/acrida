package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.Messages;
import net.gility.acrida.ui.cell.MessageCell;

public class MessageAdapter extends BaseCellAdapter<Messages, MessageCell> {

    @Override
    protected boolean loadMoreHasBg() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.cell_message;
    }
}
