package net.gility.acrida.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.Comment;
import net.gility.acrida.ui.cell.ChatCell;

public class MessageDetailAdapter extends ListBaseAdapter<Comment> {

    @Override
    protected boolean loadMoreHasBg() {
        return false;
    }

    @Override
    protected View getRealView(int position, View convertView,
            final ViewGroup parent) {
        final Comment item = mDatas.get(mDatas.size() - position - 1);
        int itemType = 0;
        if (item.getAuthorId() == ApplicationLoader.getInstance().getLoginUid()) {
            itemType = 1;
        }

        ChatCell cell = (ChatCell) convertView;

        if (cell != null && cell.getChatType() != itemType) {
            cell = (ChatCell) getLayoutInflater(parent.getContext()).inflate(
                    itemType == 0 ? R.layout.cell_chat_from
                            : R.layout.cell_chat_to, parent, false);
        }
        cell.bindTo(item);

        return cell;
    }
}
