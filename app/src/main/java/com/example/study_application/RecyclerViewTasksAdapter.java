package com.example.study_application;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTasksAdapter extends RecyclerView.Adapter<RecyclerViewTasksAdapter.viewHolder> {
    public static final String EXTRA_NUMBER = "package com.example.study_application";
    private static boolean deletingTask = false;

    // arrays
    private List<TasksList> tasksListList;
    private final ArrayList<TasksList> SELECTED_ITEMS = new ArrayList<>();

    private final Context CONTEXT;

    //already explain in other java file
    public RecyclerViewTasksAdapter(List<TasksList> tasksListList, Context Context) {
        this.tasksListList = tasksListList;
        this.CONTEXT = Context;
    }

    public static void deletingTasks(boolean b) {
        deletingTask = b;
    }

    //already explain in other java file
    @Override
    @NotNull
    public RecyclerViewTasksAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_items, parent, false);
        return new viewHolder(view);
    }

    //already explain in other java file
    @Override
    public void onBindViewHolder(@NotNull viewHolder holder, int position) {
        TasksList tasksList = tasksListList.get(position);

        // this iterates through every task once taskAdapter.notifyDataSetChanged(); is called
        // and changes the arrows to a TextBox
        if (deletingTask) {
            holder.ARROW_BUTTON.setVisibility(View.INVISIBLE);
            holder.CHECK_BOX.setVisibility(View.VISIBLE);

        } else {
            holder.ARROW_BUTTON.setVisibility(View.VISIBLE);
            holder.CHECK_BOX.setVisibility(View.INVISIBLE);
        }

        // checks if the task has been clicked
        holder.TASK_TITLE_TEXT_VIEW.setOnClickListener(view -> {
            tasksList.setExpanded(!tasksList.isExpanded());
            //reloads the task
            notifyItemChanged(position);
        });

        //checks if the checkbox has been clicked
        // this needs to be here as a glitch occurs with the search where it would unTick itself
        holder.CHECK_BOX.setOnClickListener(v -> {
            tasksList.setSelected(!tasksList.isSelected());
            notifyItemChanged(position);
        });

        // replaces the underscores with spaces to make it look better
        holder.TASK_TITLE_TEXT_VIEW.setText(tasksList.getTITLE().replace("_", " "));
        holder.TASK_STATUS_TEXT_VIEW.setText(tasksList.getSTATUS().replace("_", " "));
        holder.SPECIFICATION_TEXT_VIEW.setText(tasksList.getSPECIFICATIONS().replace("_", " "));

        //this makes sure that the textBox stays selected even after a filtered search
        holder.CHECK_BOX.setChecked(tasksList.isSelected());
        if (holder.CHECK_BOX.isChecked()) {
            if (!SELECTED_ITEMS.contains(tasksListList.get(position))) {
                SELECTED_ITEMS.add(tasksListList.get(position));
            }
        } else {
            SELECTED_ITEMS.remove(tasksListList.get(position));
        }

        //makes sure that the task stays expanded after a filtered search
        boolean isExpanded = tasksList.isExpanded();
        holder.EXPANDABLE_LAYOUT.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        ArrowAnimation.toggleArrow(holder.ARROW_BUTTON, tasksList.isExpanded());

        //listens if the start button has been pressed
        holder.START_TASK.setOnClickListener(v -> {
            Intent toTaskScreen = new Intent(CONTEXT, TaskScreen.class);
            toTaskScreen.putExtra(EXTRA_NUMBER, tasksList.getIDS());
            CONTEXT.startActivity(toTaskScreen);
        });
    }

    //already explain in other java file
    @Override
    public int getItemCount() {
        return tasksListList.size();
    }

    // this is used to configure the data to the filtered Data
    public void filterList(ArrayList<TasksList> filteredList) {
        tasksListList = filteredList;
        notifyDataSetChanged();
    }

    //already explain in other java file
    static class viewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout EXPANDABLE_LAYOUT;
        final TextView TASK_TITLE_TEXT_VIEW;
        final TextView TASK_STATUS_TEXT_VIEW;
        final TextView SPECIFICATION_TEXT_VIEW;
        final ImageView ARROW_BUTTON;
        final Button START_TASK;
        final CheckBox CHECK_BOX;

        public viewHolder(View itemView) {
            super(itemView);
            EXPANDABLE_LAYOUT = itemView.findViewById(R.id.expandableLayout);
            TASK_TITLE_TEXT_VIEW = itemView.findViewById(R.id.taskTitleTextView);
            TASK_STATUS_TEXT_VIEW = itemView.findViewById(R.id.status);
            SPECIFICATION_TEXT_VIEW = itemView.findViewById(R.id.specificationTextTextView);
            ARROW_BUTTON = itemView.findViewById(R.id.viewMoreBtn);
            START_TASK = itemView.findViewById(R.id.StartTask);
            CHECK_BOX = itemView.findViewById(R.id.checkBox);
            CHECK_BOX.bringToFront();
        }
    }

    //sends the information from the tasks
    // selected from the the textBoxes to the taskList screen to be deleted
    public List<TasksList> getSELECTED_ITEMS() {
        ArrayList<TasksList> selectedItemsList;
        selectedItemsList = SELECTED_ITEMS;
        return selectedItemsList;
    }
}
