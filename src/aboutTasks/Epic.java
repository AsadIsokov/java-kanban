package aboutTasks;

import java.util.ArrayList;

public class Epic extends Task {
    public Epic(String name, String description){
        super(name, description);
    }

    ArrayList<Subtask> subtasks = new ArrayList<>();


    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtasks(Subtask subtask){
        subtasks.add(subtask);
    }

    public void clearSubtasks(){
        subtasks.clear();
    }
    public void delSubtask(int id){
        if(subtasks.contains(subtasks.get(id))) {
            subtasks.remove(id);
        }
    }

    @Override
    public String toString(){
        return ("Epic{ name: " + getName() + ", description: " + getDescription() + ", id:" + getId() + ", status: " + getStatus());
    }


}
