package net.gility.acrida.ui.adapter.team;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.gility.acrida.R;
import net.gility.acrida.content.team.TeamActive;
import net.gility.acrida.ui.ImagePreviewActivity;
import net.gility.acrida.ui.adapter.ListBaseAdapter;
import net.gility.acrida.ui.cell.TeamActiveCell;
import net.gility.acrida.utils.ImageUtils;
import net.gility.acrida.utils.StringUtils;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;

/**
 * Team动态界面ListView适配器 (kymjs123@gmail.com)
 * 
 * @author kymjs (https://github.com/kymjs)
 *
 * @author Alimy
 * 
 */
public class TeamActiveAdapter extends ListBaseAdapter<TeamActive> {
    private final Context context;
    private final KJBitmap kjb = new KJBitmap();

    public TeamActiveAdapter(Context cxt) {
        this.context = cxt;
    }

    @Override
    protected View getRealView(int position, View v, ViewGroup parent) {
        super.getRealView(position, v, parent);

        if (v == null) {
            v = View.inflate(context, R.layout.cell_team_active, null);
        }

        TeamActive data = mDatas.get(position);
        TeamActiveCell cell = (TeamActiveCell) v;

        cell.img_head.setAvatarUrl(data.getAuthor().getPortrait());
        cell.img_head.setUserInfo(data.getAuthor().getId(), data.getAuthor()
                .getName());
        cell.tv_name.setText(data.getAuthor().getName());
        setContent(cell.tv_content, stripTags(data.getBody().getTitle()));

        String date = StringUtils.friendly_time2(data.getCreateTime());
        String preDate = "";
        if (position > 0) {
            preDate = StringUtils.friendly_time2(mDatas.get(position - 1)
                    .getCreateTime());
        }
        if (preDate.equals(date)) {
            cell.tv_title.setVisibility(View.GONE);
        } else {
            cell.tv_title.setText(date);
            cell.tv_title.setVisibility(View.VISIBLE);
        }

        cell.tv_content.setMaxLines(3);
        cell.tv_date.setText(StringUtils.friendly_time(data.getCreateTime()));
        cell.tv_commit.setText(data.getReply());

        String imgPath = data.getBody().getImage();
        if (!StringUtils.isEmpty(imgPath)) {
            cell.iv_pic.setVisibility(View.VISIBLE);
            setTweetImage(cell.iv_pic, imgPath);
        } else {
            cell.iv_pic.setVisibility(View.GONE);
        }
        return v;
    }

    /**
     * 移除字符串中的Html标签
     * 
     * @author kymjs (https://github.com/kymjs)
     * @param pHTMLString
     * @return
     */
    public static String stripTags(final String pHTMLString) {
        // String str = pHTMLString.replaceAll("\\<.*?>", "");
        String str = pHTMLString.replaceAll("\\t", "");
        str = str.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "").trim();
        return str;
    }

    @Override
    public TeamActive getItem(int arg0) {
        super.getItem(arg0);
        return mDatas.get(arg0);
    }

    /**
     * 动态设置图片显示样式
     * 
     * @author kymjs
     */
    private void setTweetImage(final ImageView pic, final String url) {
        pic.setVisibility(View.VISIBLE);

        kjb.display(pic, url, R.drawable.pic_bg, 0, 0, new BitmapCallBack() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                super.onSuccess(bitmap);
                if (bitmap != null) {
                    float scale = 360 / bitmap.getHeight();
                    pic.setImageBitmap(ImageUtils.scaleBitmap(bitmap, scale));
                }
            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity.showImagePrivew(context, 0,
                        new String[] { url });
            }
        });
    }
}