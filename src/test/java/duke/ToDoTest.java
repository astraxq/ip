package test.java.duke;

import org.junit.jupiter.api.Test;

import duke.duke_exception.DukeException;
import duke.tasklist.TaskList;
import duke.tasklist.task_types.Deadline;
import duke.tasklist.task_types.Task;
import duke.tasklist.task_types.ToDo;
import duke.utility.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoTest {
    @Test
    public void taskTest() {
        try {
            TaskList list = new TaskList();
            Parser.readCommand("todo test", list);
            Task todo = new ToDo("test");
            assertEquals(list.getTask(0).toString(), todo.toString());
        } catch (DukeException e) {
            System.out.println(e);
        }

    }

    @Test
    public void markedTest() {
        try {
            TaskList list = new TaskList();
            Parser.readCommand("deadline test /by 2020-04-12", list);
            Parser.readCommand("mark 1", list);

            Task deadline = new Deadline("test", "2020-04-12");
            deadline.setStatus(true);

            assertEquals(list.getTask(0).toString(), deadline.toString());
        } catch (DukeException e) {
            System.out.println(e);
        }
    }
}