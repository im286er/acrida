package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * @author Alimy
 */

public abstract class LinearCell<T> extends LinearLayout implements BindCell<T>{
    public LinearCell(Context context) {
        super(context);
    }

    public LinearCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);
    }
}
