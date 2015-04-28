package ronak.com.attendance_cmrit;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ronak on 18-04-2015.
 */
public class Adapter extends ArrayAdapter {

    Context context;
    LinkedList<String> subjects;
    LinkedList<Float> cond;
    LinkedList<Float> attd;
    LinkedList<Float> per;

    public Adapter(Context context, int resource, LinkedList<String> subjects, LinkedList<Float> cond,LinkedList<Float> attd,
                   LinkedList<Float> per) {
        super(context, resource, subjects);
        this.context = context;
        this.subjects = subjects;
        this.cond = cond;
        this.attd = attd;
        this.per = per;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if( convertView != null)
            view = convertView;
        else
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.list_attendance, parent, false);
        }
        TextView sub_tv, attd_tv, cond_tv, per_tv;
        sub_tv = (TextView) view.findViewById(R.id.subject_tv);
        attd_tv = (TextView) view.findViewById(R.id.attd_tv);
        cond_tv = (TextView) view.findViewById(R.id.cond_tv);
        per_tv = (TextView) view.findViewById(R.id.per_tv);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        sub_tv.setText(subjects.get(position));
        attd_tv.setText(attd.get(position).toString());
        per_tv.setText(per.get(position).toString());
        cond_tv.setText(cond.get(position).toString());
        if( per.get(position)>=85)
            progressBar.setBackgroundColor(context.getResources().getColor(R.color.green));
        else if(per.get(position)>=60)
            progressBar.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        else
            progressBar.setBackgroundColor(context.getResources().getColor(R.color.red));

        return view;
    }
}
