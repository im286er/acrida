package net.gility.acrida.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.Event;
import net.gility.acrida.content.EventList;
import net.gility.acrida.ui.cell.EventCell;

import org.kymjs.kjframe.KJBitmap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 活动列表适配器
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月9日 下午6:22:54
 * 
 */
public class EventAdapter extends ListBaseAdapter<Event> {

    private int eventType = EventList.EVENT_LIST_TYPE_NEW_EVENT;
    private final KJBitmap kjb = new KJBitmap();

    static class ViewHolder {



        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.cell_event, parent, false);
        }

        EventCell cell = (EventCell) convertView;
        Event item = mDatas.get(position);

        setEventStatus(item, cell);
        kjb.display(cell.img, item.getCover());

        return convertView;
    }

    private void setEventStatus(Event event, EventCell cell) {

        switch (this.eventType) {
            case EventList.EVENT_LIST_TYPE_NEW_EVENT:
                if (event.getApplyStatus() == Event.APPLYSTATUS_CHECKING
                        || event.getApplyStatus() == Event.APPLYSTATUS_CHECKED) {
                    cell.status.setImageResource(R.drawable.icon_event_status_checked);
                    cell.status.setVisibility(View.VISIBLE);
                } else {
                    cell.status.setVisibility(View.GONE);
                }
                break;
            case EventList.EVENT_LIST_TYPE_MY_EVENT:
                if (event.getApplyStatus() == Event.APPLYSTATUS_ATTEND) {
                    cell.status.setImageResource(R.drawable.icon_event_status_attend);
                } else if (event.getStatus() == Event.EVNET_STATUS_APPLYING) {
                    cell.status
                            .setImageResource(R.drawable.icon_event_status_checked);
                } else {
                    cell.status.setImageResource(R.drawable.icon_event_status_over);
                }
                cell.status.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
