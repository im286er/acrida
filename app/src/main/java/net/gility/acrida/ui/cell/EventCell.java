package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.Event;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class EventCell extends RelativeCell<Event> {

    @BindView(R.id.iv_event_status) public ImageView status;
    @BindView(R.id.iv_event_img) public ImageView img;
    @BindView(R.id.tv_event_title) TextView title;
    @BindView(R.id.tv_event_time) TextView time;
    @BindView(R.id.tv_event_spot) TextView spot;

    public EventCell(Context context) {
        super(context);
    }

    public EventCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Event event) {
        title.setText(event.getTitle());
        time.setText(event.getStartTime());
        spot.setText(event.getSpot());
    }
}

