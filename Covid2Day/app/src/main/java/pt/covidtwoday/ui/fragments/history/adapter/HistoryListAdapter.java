package pt.covidtwoday.ui.fragments.history.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.utils.UtilsClass;
import pt.covidtwoday.model.HistoryListModel;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.TemplateViewHolder> {
  private final Context mContext;
  private List<HistoryListModel> objectList;


  public HistoryListAdapter(Context context) {
    this.mContext = context;
    this.objectList = new ArrayList<>();
  }


  @NonNull
  @Override
  public HistoryListAdapter.TemplateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.history_list_row, viewGroup, false);
    return new HistoryListAdapter.TemplateViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull HistoryListAdapter.TemplateViewHolder viewHolder, int i) {
    viewHolder.bindView(objectList.get(i));
  }

  @Override
  public int getItemCount() {
    return objectList.size();
  }

  class TemplateViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewCases)
    TextView textViewCases;
    @BindView(R.id.textViewCasesPercentage)
    TextView textViewCasesPercentage;
    @BindView(R.id.textViewDeaths)
    TextView textViewDeaths;
    @BindView(R.id.textViewDeathsPercentage)
    TextView textViewDeathsPercentage;


    TemplateViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void bindView(HistoryListModel object) {
      textViewDate.setText(object.getDate());
      textViewCases.setText(String.valueOf(object.getCases()).substring(0,
          String.valueOf(object.getCases()).indexOf(".")));
      textViewCasesPercentage.setText(String.format("%s%%", String.valueOf(UtilsClass.getInstance().formatedDouble(object.getCasesPercentage()))));

      textViewDeaths.setText(String.valueOf(object.getDeaths()).substring(0,
          String.valueOf(object.getDeaths()).indexOf(".")));
      textViewDeathsPercentage.setText(String.format("%s%%", String.valueOf(UtilsClass.getInstance().formatedDouble(object.getDeathsPercentage()))));


      if(textViewCasesPercentage.getText().toString().contains("NaN")){
        textViewCasesPercentage.setText("0%");
      }
      if(textViewDeathsPercentage.getText().toString().contains("NaN")){
        textViewDeathsPercentage.setText("0%");
      }

    }

  }


  public void refreshList(List<HistoryListModel> objectList) {
    this.objectList = objectList;
    notifyDataSetChanged();
  }
}
