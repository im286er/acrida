package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Alimy
 */

public class ChatFromCell extends ChatCell {

    public ChatFromCell(Context context) {
        super(context);
    }

    public ChatFromCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatFromCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getChatType() {
       return CHAT_FROM;
   }
}
