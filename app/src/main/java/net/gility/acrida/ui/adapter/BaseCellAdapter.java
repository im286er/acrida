package net.gility.acrida.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gility.acrida.content.Entity;
import net.gility.acrida.ui.cell.BindCell;

/**
 * @author Alimy
 */

public abstract class BaseCellAdapter<T extends Entity, C extends BindCell<T>> extends ListBaseAdapter<T> {

    @SuppressLint({ "InflateParams", "CutPasteId" })
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(parent.getContext()).inflate(getLayoutId(), parent, false);
        }
        ((C)convertView).bindTo(mDatas.get(position));

        return convertView;
    }

    protected abstract int getLayoutId();
}
