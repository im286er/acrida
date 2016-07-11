package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import net.gility.acrida.R;
import net.gility.acrida.content.User;

/**
 * @author Alimy
 */

public class UserCell extends PersonerCell<User> {
    public UserCell(Context context) {
        super(context);
    }

    public UserCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(User user) {
        name.setText(user.getName());
        from.setText(user.getFrom());
        desc.setVisibility(View.GONE);

        int genderIcon = R.drawable.userinfo_icon_male;
        if ("女".equals(user.getGender())) {
            genderIcon = R.drawable.userinfo_icon_female;
        }

        gender.setImageResource(genderIcon);
        avatar.setAvatarUrl(user.getPortrait());
        avatar.setUserInfo(user.getId(), user.getName());
    }
}
