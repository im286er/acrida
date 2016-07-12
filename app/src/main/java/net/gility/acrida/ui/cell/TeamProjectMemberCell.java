package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamMember;
import net.gility.acrida.ui.widget.AvatarView;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class TeamProjectMemberCell extends LinearCell<TeamMember> {
    @BindView(R.id.iv_avatar) AvatarView avatar;
    @BindView(R.id.tv_name) TextView name;

    public TeamProjectMemberCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TeamProjectMemberCell(Context context) {
        super(context);
    }

    public TeamProjectMemberCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void bindTo(TeamMember data) {
        avatar.setAvatarUrl(data.getPortrait());
        name.setText(data.getName());

    }
}
