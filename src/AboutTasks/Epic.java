package AboutTasks;

import AboutTasks.Task;

import java.util.ArrayList;

public class Epic extends Task {
    public Epic(int id, String name, String description){
        super(id, name, description);
    }

    ArrayList<Subtask> subtasks = new ArrayList<>();


    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }


    public void addSubtasks(Subtask subtask){
        subtasks.add(subtask);
    }

    public void clearSubtasks(){
        subtasks.clear();
    }

    public void clearSubtaskById(int id){
        for(Subtask elem : subtasks){
            if (elem.getId() == id){
                subtasks.remove(elem);
            }
        }
    }


}
