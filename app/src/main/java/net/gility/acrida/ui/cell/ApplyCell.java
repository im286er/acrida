package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import net.gility.acrida.content.Apply;

/**
 * @author Alimy
 */

public class ApplyCell  extends PersonerCell<Apply> {
    public ApplyCell(Context context) {
        super(context);
    }

    public ApplyCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ApplyCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Apply apply) {
        name.setText(apply.getName());
        avatar.setUserInfo(apply.getId(), apply.getName());
        avatar.setAvatarUrl(apply.getPortrait());
        from.setVisibility(View.GONE);
        desc.setText(apply.getCompany() + " " + apply.getJob());
    }
}