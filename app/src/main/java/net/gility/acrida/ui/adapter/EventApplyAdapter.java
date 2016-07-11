package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.Apply;
import net.gility.acrida.ui.cell.ApplyCell;
import net.gility.acrida.ui.cell.FriendCell;

/**
 * 活动参会人员适配器
 * 
 * @author Alimy
 * 
 */
public class EventApplyAdapter extends BaseCellAdapter<Apply, ApplyCell> {

   @Override
    protected int getLayoutId() {
       return R.layout.cell_apply;
   }
}
