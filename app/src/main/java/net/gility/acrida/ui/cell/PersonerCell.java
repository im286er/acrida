package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.ui.widget.AvatarView;

import butterknife.BindView;

/**
 * @author Alimy
 */

public abstract class PersonerCell<T> extends RelativeCell<T> {
    @BindView(R.id.tv_name) TextView name;
    @BindView(R.id.tv_from) TextView from;
    @BindView(R.id.tv_desc) TextView desc;
    @BindView(R.id.iv_gender) ImageView gender;
    @BindView(R.id.iv_avatar) AvatarView avatar;

    public PersonerCell(Context context) {
        super(context);
    }

    public PersonerCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonerCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
