package com.denabelarde.questionnaire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.denabelarde.questionnaire.R;
import com.denabelarde.questionnaire.models.AnswerDto;
import com.denabelarde.questionnaire.models.QuestionDto;

import java.util.ArrayList;


/**
 * Created by rygalang on 10/23/14.
 */
public class AnswersItemAdapter extends BaseAdapter {

    ArrayList<AnswerDto> answerArray;
    LayoutInflater inflater;
    Context context;

    public void refreshAdapter(ArrayList<AnswerDto> answerArray) {
        this.answerArray = answerArray;
        this.notifyDataSetChanged();
    }

    public AnswersItemAdapter(Context context, ArrayList<AnswerDto> answerArray) {
        this.context = context;
        this.answerArray = answerArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return answerArray.size();
    }

    @Override
    public AnswerDto getItem(int i) {
        return answerArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.answers_item_layout, viewGroup, false);
            holder = new ViewHolder((TextView) view.findViewById(R.id.answer_text), (TextView) view.findViewById(R.id.answer_user));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        AnswerDto answerDto = answerArray.get(position);
        holder.answerText.setText(answerDto.getAnswer());
        holder.answerUser.setText("by: " + answerDto.getUserName());

        return view;
    }

    public class ViewHolder {
        TextView answerText, answerUser;

        public ViewHolder(TextView answerText, TextView answerUser) {
            this.answerText = answerText;
            this.answerUser = answerUser;
        }
    }
}
