package net.gility.acrida.ui.cell;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.Comment;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.ui.widget.MyLinkMovementMethod;
import net.gility.acrida.ui.widget.MyURLSpan;
import net.gility.acrida.ui.widget.TweetTextView;
import net.gility.acrida.ui.widget.emoji.InputHelper;
import net.gility.acrida.utils.StringUtils;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class ChatToCell extends ChatCell {

    public ChatToCell(Context context) {
        super(context);
    }

    public ChatToCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatToCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getChatType() {
       return CHAT_TO;
   }
}
