package net.gility.acrida.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.content.User;
import net.gility.acrida.ui.cell.UserCell;
import net.gility.acrida.ui.widget.AvatarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 好友列表适配器
 *@author Alimy
 * 
 */
public class FindUserAdapter extends BaseCellAdapter<User, UserCell> {

   @Override
	protected int getLayoutId() {
	   return R.layout.cell_user;
   }
}
