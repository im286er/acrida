package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

/**
 * @author Alimy
 */

public abstract class RelativeCell<T> extends RelativeLayout implements BindCell<T> {
    public RelativeCell(Context context) {
        super(context);
    }

    public RelativeCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);
    }
}
