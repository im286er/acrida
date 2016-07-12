package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.User;
import net.gility.acrida.ui.widget.AvatarView;

import butterknife.BindView;

/**
 *@author Alimy
 */

public class TweetLikeUserCell extends LinearCell<User> {
    @BindView(R.id.iv_avatar) AvatarView avatar;
    @BindView(R.id.tv_name) TextView name;

    public TweetLikeUserCell(Context context) {
        super(context);
    }

    public TweetLikeUserCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TweetLikeUserCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(User data) {
        avatar.setAvatarUrl(data.getPortrait());
        name.setText(data.getName());
    }
}
