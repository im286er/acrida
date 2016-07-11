package net.gility.acrida.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.Active;
import net.gility.acrida.content.Active.ObjectReply;
import net.gility.acrida.ui.ImagePreviewActivity;
import net.gility.acrida.ui.cell.ActiveCell;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.TweetTextView;
import net.gility.acrida.ui.widget.emoji.InputHelper;
import net.gility.acrida.utils.ImageUtils;
import net.gility.acrida.utils.PlatfromUtil;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.UIHelper;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.utils.DensityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActiveAdapter extends ListBaseAdapter<Active> {
    private final static String AT_HOST_PRE = "http://my.oschina.net";
    private final static String MAIN_HOST = "http://www.oschina.net";
    private Bitmap recordBitmap;
    private final KJBitmap kjb = new KJBitmap();
    private int rectSize;

    public ActiveAdapter() {
        Context context = ApplicationLoader.getInstance();
        initImageSize(context);
        initRecordImg(context);
    }

    @Override
    @SuppressLint("InflateParams")
    protected View getRealView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.cell_active, null);
        }

        ActiveCell cell = (ActiveCell) convertView;

        final Active item = mDatas.get(position);

        cell.name.setText(item.getAuthor());
        cell.action.setText(UIHelper.parseActiveAction(item.getObjectType(),
                item.getObjectCatalog(), item.getObjectTitle()));

        if (TextUtils.isEmpty(item.getMessage())) {
            cell.body.setVisibility(View.GONE);
        } else {
            cell.body.setMovementMethod(MyLinkMovementMethod.a());
            cell.body.setFocusable(false);
            cell.body.setDispatchToParent(true);
            cell.body.setLongClickable(false);

            Spanned span = Html.fromHtml(modifyPath(item.getMessage()));

            if (!StringUtils.isEmpty(item.getTweetattach())) {
                ImageSpan recordImg = new ImageSpan(parent.getContext(), recordBitmap);
                SpannableString str = new SpannableString("c");
                str.setSpan(recordImg, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                cell.body.setText(str);
                span = InputHelper.displayEmoji(parent.getContext().getResources(), span);
                cell.body.append(span);
            } else {
                span = InputHelper.displayEmoji(parent.getContext().getResources(), span);
                cell.body.setText(span);
            }
            MyURLSpan.parseLinkText(cell.body, span);
        }

        ObjectReply reply = item.getObjectReply();
        if (reply != null) {
            cell.reply.setMovementMethod(MyLinkMovementMethod.a());
            cell.reply.setFocusable(false);
            cell.reply.setDispatchToParent(true);
            cell.reply.setLongClickable(false);
            Spanned span = UIHelper.parseActiveReply(reply.objectName, reply.objectBody);
            cell.reply.setText(span);//
            MyURLSpan.parseLinkText(cell.reply, span);
            cell.lyReply.setVisibility(TextView.VISIBLE);
        } else {
            cell.reply.setText("");
            cell.lyReply.setVisibility(TextView.GONE);
        }

        cell.time.setText(StringUtils.friendly_time(item.getPubDate()));
        PlatfromUtil.setPlatFromString(cell.from, item.getAppClient());
        cell.commentCount.setText(item.getCommentCount() + "");
        cell.avatar.setUserInfo(item.getAuthorId(), item.getAuthor());
        cell.avatar.setAvatarUrl(item.getPortrait());

        if (!TextUtils.isEmpty(item.getTweetimage())) {
            setTweetImage(parent, cell, item);
        } else {
            cell.pic.setVisibility(View.GONE);
            cell.pic.setImageBitmap(null);
        }

        return cell;
    }

    /**
     * 动态设置图片显示样式
     * 
     * @author kymjs
     */
    private void setTweetImage(final ViewGroup parent, final ActiveCell cell,
            final Active item) {
        cell.pic.setVisibility(View.VISIBLE);

        kjb.display(cell.pic, item.getTweetimage(), R.drawable.pic_bg, rectSize,
                rectSize, new BitmapCallBack() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        super.onSuccess(bitmap);
                        if (bitmap != null) {
                            float scale = (float) rectSize / bitmap.getHeight();
                            cell.pic.setImageBitmap(ImageUtils.scaleBitmap(bitmap, scale));
                        }
                    }
                });

        cell.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity.showImagePrivew(parent.getContext(), 0,
                        new String[] { getOriginalUrl(item.getTweetimage()) });
            }
        });
    }

    private void initRecordImg(Context cxt) {
        recordBitmap = BitmapFactory.decodeResource(cxt.getResources(), R.drawable.audio3);
        recordBitmap = ImageUtils.zoomBitmap(recordBitmap,
                DensityUtils.dip2px(cxt, 20f), DensityUtils.dip2px(cxt, 20f));
    }

    private void initImageSize(Context cxt) {
        if (cxt != null && rectSize == 0) {
            rectSize = (int) cxt.getResources().getDimension(R.dimen.space_100);
        } else {
            rectSize = 300;
        }
    }

    private String modifyPath(String message) {
        message = message.replaceAll("(<a[^>]+href=\")/([\\S]+)\"", "$1"
                + AT_HOST_PRE + "/$2\"");
        message = message.replaceAll(
                "(<a[^>]+href=\")http://m.oschina.net([\\S]+)\"", "$1"
                        + MAIN_HOST + "$2\"");
        return message;
    }

    private String getOriginalUrl(String url) {
        return url.replaceAll("_thumb", "");
    }

}
