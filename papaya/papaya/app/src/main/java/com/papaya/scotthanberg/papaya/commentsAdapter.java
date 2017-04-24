package com.papaya.scotthanberg.papaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ChristianLock on 4/22/17.
 */

public class commentsAdapter extends ArrayAdapter<commentPost> {

    int size;

    public commentsAdapter(Context context, ArrayList<commentPost> comments) {
        super(context, 0, comments);
        size = comments.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        commentPost comment = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView UserID = (TextView) convertView.findViewById(R.id.UserID);
        TextView commentBody = (TextView) convertView.findViewById(R.id.commentBody);

        UserID.setText(comment.USERNAME);
        commentBody.setText(comment.COMMENT);

        return convertView;
    }
}
