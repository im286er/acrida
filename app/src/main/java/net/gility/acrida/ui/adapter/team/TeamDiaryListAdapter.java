package net.gility.acrida.ui.adapter.team;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamDiary;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 周报的ListView适配器
 *
 * @author kymjs (http://www.kymjs.com)
 */
public class TeamDiaryListAdapter extends BaseAdapter {
    private final Context cxt;
    private List<TeamDiary> list;

    public TeamDiaryListAdapter(Context cxt, List<TeamDiary> list) {
        this.cxt = cxt;
        if (list == null) {
            list = new ArrayList<TeamDiary>(1);
        }
        this.list = list;
    }

    public void refresh(List<TeamDiary> list) {
        if (list == null) {
            list = new ArrayList<TeamDiary>(1);
        }
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TeamDiary data = list.get(position);
        ViewHolder holder = null;
        if (view == null) {
            view = View.inflate(cxt, R.layout.list_cell_team_diary, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.iv_face.setAvatarUrl(data.getAuthor().getPortrait());
        holder.tv_author.setText(data.getAuthor().getName());
        holder.tv_date.setText(StringUtils.friendly_time(data.getCreateTime()));
        holder.tv_count.setText(data.getReply() + "");
        holder.tv_title.setText(Html.fromHtml(data.getTitle()).toString().trim());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_face)
        AvatarView iv_face;
        @BindView(R.id.tv_author)
        TextView tv_author;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_count)
        TextView tv_count;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
