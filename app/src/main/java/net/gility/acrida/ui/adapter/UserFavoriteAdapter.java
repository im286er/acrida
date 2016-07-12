package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.Favorite;
import net.gility.acrida.ui.cell.FavoriteCell;

public class UserFavoriteAdapter extends BaseCellAdapter<Favorite, FavoriteCell> {

	@Override
	protected int getLayoutId() {
		return R.layout.cell_favorite;
	}
}
