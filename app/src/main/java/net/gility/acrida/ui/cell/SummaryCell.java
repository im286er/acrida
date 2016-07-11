package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;

import butterknife.BindView;

/**
 * @author Alimy
 */

public abstract class SummaryCell<T> extends RelativeCell<T> {
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_description) TextView description;
    @BindView(R.id.tv_source) TextView source;
    @BindView(R.id.tv_time) TextView time;
    @BindView(R.id.tv_comment_count) TextView comment_count;
    @BindView(R.id.iv_tip) ImageView tip;

    public SummaryCell(Context context) {
        super(context);
    }

    public SummaryCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SummaryCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
