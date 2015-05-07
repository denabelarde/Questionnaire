package com.denabelarde.questionnaire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.denabelarde.questionnaire.R;
import com.denabelarde.questionnaire.models.QuestionDto;

import java.util.ArrayList;


/**
 * Created by rygalang on 10/23/14.
 */
public class QuestionsItemAdapter extends BaseAdapter {

    ArrayList<QuestionDto> questionArray;
    LayoutInflater inflater;
    Context context;

    public void refreshAdapter(ArrayList<QuestionDto> questionArray) {
        this.questionArray = questionArray;
        this.notifyDataSetChanged();
    }

    public QuestionsItemAdapter(Context context, ArrayList<QuestionDto> questionArray) {
        this.context = context;
        this.questionArray = questionArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return questionArray.size();
    }

    @Override
    public QuestionDto getItem(int i) {
        return questionArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.questions_item_layout, viewGroup, false);
            holder = new ViewHolder((TextView) view.findViewById(R.id.question_title), (TextView) view.findViewById(R.id.question_desc));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        QuestionDto questionDto = questionArray.get(position);
        holder.questionTitle.setText(questionDto.getTitle());
        holder.questionDescription.setText(questionDto.getDescription());

        return view;
    }

    public class ViewHolder {
        TextView questionTitle, questionDescription;

        public ViewHolder(TextView questionTitle, TextView questionDescription) {
            this.questionTitle = questionTitle;
            this.questionDescription = questionDescription;
        }
    }
}
