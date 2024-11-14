package AboutTasks;

import AboutTasks.Task;

public class Subtask extends Task {
    public Subtask(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Subtask(int id, String name, String description){
        super(id, name, description);
    }
}
