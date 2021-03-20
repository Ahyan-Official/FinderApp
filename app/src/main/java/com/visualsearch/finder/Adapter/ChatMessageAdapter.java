package com.visualsearch.finder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.visualsearch.finder.Model.ChatMessage;
import com.visualsearch.finder.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {


    public static final int MY_MESSAGE =0,OTHER_MESSAGE=1;

    public ChatMessageAdapter(@NonNull Context context, @NonNull List<ChatMessage> objects) {
        super(context, R.layout.user_messages, objects);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        ChatMessage item = getItem(position);
        if(item.isMine() && !item.isiImage()){

            return MY_MESSAGE;

        }else{

            return OTHER_MESSAGE;

        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View itemView, @NonNull ViewGroup parent) {

        int viewType = getItemViewType(position);

        if(viewType==MY_MESSAGE){

            itemView = LayoutInflater.from(getContext()).inflate(R.layout.user_messages,parent,false);

            TextView textView = itemView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());

        }else if(viewType==OTHER_MESSAGE){

            itemView = LayoutInflater.from(getContext()).inflate(R.layout.bot_messages,parent,false);
            TextView textView = itemView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        }

        return itemView;

    }


}
