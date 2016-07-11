package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import net.gility.acrida.R;
import net.gility.acrida.content.Friend;
import net.gility.acrida.utils.StringUtils;

/**
 * @author Alimy
 */

public class FriendCell extends PersonerCell<Friend> {
    public FriendCell(Context context) {
        super(context);
    }

    public FriendCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FriendCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Friend friend) {
        name.setText(friend.getName());
        String from = friend.getFrom();
        if (from != null || !StringUtils.isEmpty(from)) {
            this.from.setText(from);
        } else {
            this.from.setVisibility(View.GONE);
        }
        String desc = friend.getExpertise();
        if (desc != null || !StringUtils.isEmpty(from) || !"<无>".equals(desc)) {
            this.desc.setText(friend.getExpertise());
        } else {
            this.desc.setVisibility(View.GONE);
        }

        gender.setImageResource(friend.getGender() == 1 ? R.drawable.userinfo_icon_male
                        : R.drawable.userinfo_icon_female);

        avatar.setAvatarUrl(friend.getPortrait());
        avatar.setUserInfo(friend.getUserid(), friend.getName());
    }
}
