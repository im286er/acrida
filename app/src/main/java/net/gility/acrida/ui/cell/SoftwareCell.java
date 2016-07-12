package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.SoftwareDec;
import net.gility.acrida.content.SoftwareList;
import net.gility.acrida.utils.ThemeSwitchUtils;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class SoftwareCell extends LinearCell<SoftwareDec> {
    @BindView(R.id.tv_title) TextView name;
    @BindView(R.id.tv_software_des) TextView des;

    public SoftwareCell(Context context) {
        super(context);
    }

    public SoftwareCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftwareCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(SoftwareDec softwareDes) {
        name.setText(softwareDes.getName());

        if (ApplicationLoader.isOnReadedPostList(SoftwareList.PREF_READED_SOFTWARE_LIST,
                softwareDes.getName())) {
            name.setTextColor(getResources().getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            name.setTextColor(getResources().getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        des.setText(softwareDes.getDescription());
    }
}
