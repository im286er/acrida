package net.gility.acrida.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.Friend;
import net.gility.acrida.ui.cell.FriendCell;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 好友列表适配器
 *@author Alimy
 * 
 */
public class FriendAdapter extends BaseCellAdapter<Friend, FriendCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_friend;
    }
}
