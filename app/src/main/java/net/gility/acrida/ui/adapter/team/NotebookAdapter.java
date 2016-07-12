package net.gility.acrida.ui.adapter.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.gility.acrida.R;
import net.gility.acrida.content.NotebookData;
import net.gility.acrida.ui.cell.NoteBookCell;
import net.gility.acrida.ui.fragment.team.NoteEditFragment;
import net.gility.acrida.ui.widget.KJDragGridView.DragGridBaseAdapter;

import org.kymjs.kjframe.utils.DensityUtils;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 便签列表适配器
 * 
 * @author kymjs (https://github.com/kymjs)
 *
 * @author Alimy
 * 
 */
public class NotebookAdapter extends BaseAdapter implements DragGridBaseAdapter {
    private List<NotebookData> datas;
    private final Activity aty;
    private int currentHidePosition = -1;
    private final int width;
    private final int height;
    private boolean dataChange = false;

    public NotebookAdapter(Activity aty, List<NotebookData> datas) {
        super();
        Collections.sort(datas);
        this.datas = datas;
        this.aty = aty;
        width = DensityUtils.getScreenW(aty) / 2;
        height = (int) aty.getResources().getDimension(R.dimen.space_35);
    }

    public void refurbishData(List<NotebookData> datas) {
        if (datas == null) {
            datas = new ArrayList<NotebookData>(1);
        }
        Collections.sort(datas);
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<NotebookData> getDatas() {
        return datas;
    }

    /**
     * 数据是否发生了改变
     * 
     * @return
     */
    public boolean getDataChange() {
        return dataChange;
    }

    static class ViewHolder {

    }

    @Override
    public View getView(int position, View converterView, ViewGroup parent) {
        datas.get(position).setIid(position);
        NotebookData data = datas.get(position);

        if (converterView == null) {
            converterView = View.inflate(aty, R.layout.cell_notebook, null);
        }

        NoteBookCell cell = (NoteBookCell) converterView;

        RelativeLayout.LayoutParams params = (LayoutParams) cell.content
                .getLayoutParams();
        params.width = width;
        params.height = (params.width - height);
        cell.content.setLayoutParams(params);

        cell.titleBar
                .setBackgroundColor(NoteEditFragment.sTitleBackGrounds[data
                        .getColor()]);
        cell.date.setText(data.getDate());
        if (data.getId() > 0) {
            cell.state.setVisibility(View.GONE);
        } else {
            cell.state.setVisibility(View.VISIBLE);
        }
        cell.thumbtack.setImageResource(NoteEditFragment.sThumbtackImgs[data
                .getColor()]);
        cell.content.setText(Html.fromHtml(data.getContent()));
        cell.content.setBackgroundColor(NoteEditFragment.sBackGrounds[data
                .getColor()]);
        if (position == currentHidePosition) {
            cell.setVisibility(View.GONE);
        } else {
            cell.setVisibility(View.VISIBLE);
        }
        return cell;
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        dataChange = true;
        if (oldPosition >= datas.size() || oldPosition < 0) {
            return;
        }
        NotebookData temp = datas.get(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(datas, i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(datas, i, i - 1);
            }
        }
        datas.set(newPosition, temp);
    }

    @Override
    public void setHideItem(int hidePosition) {
        this.currentHidePosition = hidePosition;
        notifyDataSetChanged();
    }
}
