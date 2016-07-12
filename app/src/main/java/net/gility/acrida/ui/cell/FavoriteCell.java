package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.Favorite;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class FavoriteCell extends LinearCell<Favorite> {
    @BindView(R.id.tv_favorite_title) TextView title;

    public FavoriteCell(Context context) {
        super(context);
    }

    public FavoriteCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Favorite data) {
        title.setText(data.getTitle());
    }
}
