package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.NotebookData;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class NoteBookCell extends RelativeCell<NotebookData> {
    @BindView(R.id.item_note_tv_date) public TextView date;
    @BindView(R.id.item_note_img_state) public ImageView state;
    @BindView(R.id.item_note_img_thumbtack) public ImageView thumbtack;
    @BindView(R.id.item_note_titlebar) public View titleBar;
    @BindView(R.id.item_note_content) public TextView content;

    public NoteBookCell(Context context) {
        super(context);
    }

    public NoteBookCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteBookCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(NotebookData data) {

    }
}
