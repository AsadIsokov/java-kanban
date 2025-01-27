package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private static final Map<Integer, Node<Task>> historyList= new HashMap<>();
    public Node<Task> head;
    public Node<Task> tail;
    public void linkLast(Task task){
        Node<Task> newOne = new Node<>(task);
        if (tail != null){
            tail.setNext(newOne);
            newOne.setPrev(tail);
        } else{
            head = newOne;
        }
        tail = newOne;
    }

    @Override
    public List<Task> getTasks(){
        List<Task> historyArrayList = new ArrayList<>();
        Node<Task> counter = head;
        while(counter.getNext() != null){
            historyArrayList.add(counter.getData());
            counter = counter.getNext();
        }
        historyArrayList.add(tail.getData());
        return historyArrayList;
    }

    public void removeNode(Node<Task> someNode){
        if(someNode == tail){
            tail = someNode.getPrev();
            tail.setNext(null);
        } else if (someNode == head){
            head = someNode.getNext();
            head.setPrev(null);
        } else{
            someNode.getNext().setPrev(someNode.getPrev());
            someNode.getPrev().setNext(someNode.getNext());
        }
    }
    @Override
    public void add(Task task) {
        if(historyList.containsKey(task.getId())){
            removeNode(historyList.get(task.getId()));
        }
        linkLast(task);
        historyList.put(task.getId(), tail);
    }

    @Override
    public void remove(int id){
        if(historyList.containsKey(id)){
            removeNode(historyList.get(id));
        }
    }


}
